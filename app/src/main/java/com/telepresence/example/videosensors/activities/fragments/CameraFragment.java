package com.telepresence.example.videosensors.activities.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.CamcorderProfile;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.telepresence.example.videosensors.R;
import com.telepresence.example.videosensors.activities.activities.MainActivity;
import com.telepresence.example.videosensors.activities.data.SettingModel;
import com.telepresence.example.videosensors.activities.util.FileWriterUtil;
import com.telepresence.example.videosensors.activities.util.MenuUtil;
import com.telepresence.example.videosensors.activities.util.PrintUtil;

import org.mp4parser.IsoFile;
import org.mp4parser.tools.Mp4Arrays;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static com.telepresence.example.videosensors.activities.fragments.CameraFragment.state.*;

public class CameraFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "CameraFragment";

    private static final int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90;
    private static final int SENSOR_ORIENTATION_INVERSE_DEGREES = 270;
    private static final SparseIntArray DEFAULT_ORIENTATIONS = new SparseIntArray();
    private static final SparseIntArray INVERSE_ORIENTATIONS = new SparseIntArray();

    static {
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_0, 90);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_90, 0);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_180, 270);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    static {
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_0, 270);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_90, 180);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_180, 90);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_270, 0);
    }

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    // GUI components
    private TextureView cameraPreview;
    private ImageButton qualityButton;
    private ImageButton frameRateButton;

    private FileWriterUtil fileWriter;

    // menu
    private MenuUtil menuManager;
    private int quality;
    private int frameRate;

    //private SettingModel settings;

    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener  = new ImageReader.OnImageAvailableListener() {

        @Override
        public void onImageAvailable(ImageReader reader) {
            Log.d(TAG, System.currentTimeMillis() + ": image available");

            if (videoFile != null) {
                Image image = reader.acquireNextImage();
                //mBackgroundHandler.post(new ImageSaver(image, videoFile));
                ImageSaver saver = new ImageSaver(image, videoFile);
                saver.run();
                image.close();
            }
        }

    };

    private CameraDevice mCameraDevice;
    private CameraCaptureSession mPreviewSession;
    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture,
                                              int width, int height) {
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture,
                                                int width, int height) {
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

    };
    private Size mPreviewSize;
    private Size mVideoSize;
    //private MediaRecorder mMediaRecorder;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);
    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(CameraDevice cameraDevice) {
            mCameraDevice = cameraDevice;
            startPreview();
            mCameraOpenCloseLock.release();
            if (null != cameraPreview) {
                configureTransform(cameraPreview.getWidth(), cameraPreview.getHeight());
            }
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(CameraDevice cameraDevice, int error) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
            Activity activity = getActivity();
            if (null != activity) {
                activity.finish();
            }
        }

    };
    private CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber) {
            //Log.d(TAG, "onCaptureStarted: " + timestamp + ", " + frameNumber);
            fileWriter.writeVideoTimestampFile(timestamp, frameNumber);
        }

        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            //Log.d(TAG, "onCaptureCompleted: " + result.getFrameNumber() + ", " + result.get(CaptureResult.SENSOR_EXPOSURE_TIME) + " , " + result.get(CaptureResult.SENSOR_FRAME_DURATION));
            //40014000 is the SENSOR_FRAME_DURATION => this*30 = 1.2 seconds per 30 frames....too low
        }
    };
    private Integer mSensorOrientation;
    private String mNextVideoAbsolutePath;
    private CaptureRequest.Builder mPreviewBuilder;
    private Surface mRecorderSurface;
    private StreamConfigurationMap map;

    private ImageReader reader;
    private File videoFile;


    private state current_state = STATE_PREVIEW;

    enum state{
        STATE_PREVIEW, STATE_RECORDING
    }

    public static CameraFragment newInstance() {
        return new CameraFragment();
    }

    private static Size chooseVideoSize(Size[] choices) {
        for (Size size : choices) {
            if (size.getWidth() == size.getHeight() * 4 / 3 && size.getWidth() <= 1080) {
                return size;
            }
        }
        return choices[choices.length - 1];
    }
    private static Size chooseOptimalSize(Size[] choices, int width, int height, Size aspectRatio) {
        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<Size>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getHeight() == option.getWidth() * h / w &&
                    option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }

        // Pick the smallest of those, assuming we found any
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else {
            return choices[0];
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        // initialize GUI components
        cameraPreview = (TextureView) view.findViewById(R.id.camera_preview);
        //qualityButton = (ImageButton) view.findViewById(R.id.button_quality);
        //qualityButton.setOnClickListener(this);
      /*  frameRateButton = (ImageButton) view.findViewById(R.id.button_frameRate);
        frameRateButton.setOnClickListener(this);
        qualityButton = (ImageButton) view.findViewById(R.id.button_quality);
        qualityButton.setOnClickListener(this);*/

        // initialize values
        menuManager = new MenuUtil(getActivity());
        frameRate = menuManager.getFrameRate();
        quality = menuManager.getQuality();

        fileWriter = ((MainActivity) getActivity()).getFileWriter();
    }

    @Override
    public void onResume() {
        super.onResume();
        startBackgroundThread();
        if (cameraPreview.isAvailable()) {
            openCamera(cameraPreview.getWidth(), cameraPreview.getHeight());
        } else {
            cameraPreview.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    @Override
    public void onPause() {

        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void openCamera(int width, int height) {

        final Activity activity = getActivity();
        if (null == activity || activity.isFinishing()) {
            return;
        }
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }

            String cameraId = manager.getCameraIdList()[0];

            // Choose the sizes for camera preview and video recording
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

            map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            /**
             * The range of image exposure times for android.sensor.exposureTime supported by this camera device.

             Units: Nanoseconds

             Range of valid values:
             The minimum exposure time will be less than 100 us. For FULL capability devices (android.info.supportedHardwareLevel == FULL), the maximum exposure time will be greater than 100ms.

             Optional - This value may be null on some devices.

             Full capability - Present on all camera devices that report being HARDWARE_LEVEL_FULL devices in the android.info.supportedHardwareLevel key

             See also:

             INFO_SUPPORTED_HARDWARE_LEVEL
             SENSOR_EXPOSURE_TIME
             */
            Range<Long> exposureTimes = characteristics.get(CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE);
            Log.e("CameraFragment", "Exposure: [" + exposureTimes.getLower() + " - " + exposureTimes.getUpper() + "]");

            PrintUtil.printRanges(characteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES), TAG, "FPS");


            /**
             All camera devices support ON, and all camera devices with flash units support ON_AUTO_FLASH and ON_ALWAYS_FLASH.
             FULL mode camera devices always support OFF mode, which enables application control of camera exposure time, sensitivity, and frame duration.
             LEGACY mode camera devices never support OFF mode. LIMITED mode devices support OFF if they support the MANUAL_SENSOR capability.
             OFF
             ON
             ON_AUTO_FLASH
             ON_ALWAYS_FLASH
             ON_AUTO_FLASH_REDEYE
             */
            int[] aeModes = characteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES);
            Log.e(TAG, "AE MODES: ");
            for(int ae : aeModes) Log.e(TAG, "           " + ae);

            /**
             * Print the supported fps settings
             */

            mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            mVideoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
            Log.i(TAG, "ImageReader size: " + mVideoSize.toString());

            mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height, mVideoSize);
            Log.i(TAG, "Preview size: " + mVideoSize.toString());

            // imagereader
            reader = ImageReader.newInstance(mVideoSize.getWidth(), mVideoSize.getHeight(), ImageFormat.JPEG, 2);
            reader.setOnImageAvailableListener(mOnImageAvailableListener, mBackgroundHandler);

         //   int orientation = getResources().getConfiguration().orientation;
            configureTransform(width, height);
            //mMediaRecorder = new MediaRecorder();
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            manager.openCamera(cameraId, mStateCallback, null);
        } catch (CameraAccessException e) {
            Toast.makeText(activity, "Cannot access the camera.", Toast.LENGTH_SHORT).show();
            activity.finish();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.");
        }
    }

    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            closePreviewSession();
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            //if (null != mMediaRecorder) {
            //    mMediaRecorder.release();
             //   mMediaRecorder = null;
            //}
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.");
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    private void startPreview() {
        if (null == mCameraDevice || !cameraPreview.isAvailable() || null == mPreviewSize) {
            return;
        }
        try {
            closePreviewSession();
            SurfaceTexture texture = cameraPreview.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            ArrayList<Surface> sessionSurfaces = new ArrayList<>();

            Surface previewSurface = new Surface(texture);
            mPreviewBuilder.addTarget(previewSurface);
            sessionSurfaces.add(previewSurface);

            Surface imageReaderSurface;
            if(current_state==STATE_RECORDING){
                imageReaderSurface = reader.getSurface();
                mPreviewBuilder.addTarget(imageReaderSurface);
                sessionSurfaces.add(imageReaderSurface);
            }

            mCameraDevice.createCaptureSession(sessionSurfaces, new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    mPreviewSession = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                    Activity activity = getActivity();
                    if (null != activity) {
                        Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void updatePreview() {
        if (null == mCameraDevice) {
            return;
        }
        try {
            setUpCaptureRequestBuilder(mPreviewBuilder);
            HandlerThread thread = new HandlerThread("CameraPreview");
            thread.start();
            mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), captureCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setUpCaptureRequestBuilder(CaptureRequest.Builder builder) {
        //CONTROL_AE_MODE is only effective if android.control.mode is AUTO.
        builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        builder.set(CaptureRequest.CONTROL_AE_MODE , CameraMetadata.CONTROL_AE_MODE_OFF);
        builder.set(CaptureRequest.SENSOR_EXPOSURE_TIME, (long) 20000000);//usual is 40 014 000
    }

    private void configureTransform(int viewWidth, int viewHeight) {
        Activity activity = getActivity();
        if (null == cameraPreview || null == mPreviewSize || null == activity) {
            return;
        }
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        }
        cameraPreview.setTransform(matrix);
    }

    public void startRecording(File videoFile) {


        this.videoFile = videoFile;

        this.current_state = STATE_RECORDING;

        startPreview();

    }

    public void stopRecording() {
        this.current_state = STATE_PREVIEW;
        videoFile = null;
        startPreview();
    }


    private void closePreviewSession() {
        if (mPreviewSession != null) {
            mPreviewSession.close();
            mPreviewSession = null;
        }
    }


    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }
    }

    // onClickListener
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
/*
            case R.id.button_frameRate: {
                frameRate = menuManager.openFrameRateMenu();
                break;
            }
            case R.id.button_quality: {
                quality = menuManager.openQualityMenu();
                break;
            }*/

            default:
                break;
        }
    }
/*
    public Image getImg() {
        return img;
    }

    public byte[] getImgBytes() {
        return imgBytes;
    }*/

    private byte[] convertToBytes(Image image) {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        return bytes;
    }

    private int[] getVideoQuality(int quality) {

        int[] tmp = new int[2];
        switch (quality) {
            case MenuUtil.QUALITY_1080P: {
                tmp[0] = 1920;
                tmp[1] = 1080;
                return tmp;
            }
            case MenuUtil.QUALITY_720P: {
                tmp[0] = 1280;
                tmp[1] =  720;
                return tmp;
            }
            case MenuUtil.QUALITY_480P: {
                tmp[0] = 640;
                tmp[1] = 480;
                return tmp;
            }
        }
        return null;
    }
    public StreamConfigurationMap getMap() {
        return map;
    }

    public void setMap(StreamConfigurationMap map) {
        this.map = map;
    }

    private static class ImageSaver implements Runnable {

        /**
         * The JPEG image
         */
        private final Image mImage;
        /**
         * The file we save the image into.
         */
        private final File mFile;

        public ImageSaver(Image image, File imageFile) {
            mImage = image;
            mFile = imageFile;
        }

        @Override
        public void run() {
            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            FileOutputStream output = null;
            try {
                output = new FileOutputStream(mFile + "/" + System.currentTimeMillis() + ".jpeg");
                output.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //mImage.close();
                if (null != output) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
