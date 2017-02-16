package com.telepresence.example.videosensors.activities.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.ActivityCompat;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

/**
 * Created by telepresence on 14.04.2016.
 */
public class CameraUtil {

    private Activity activity;

    private CameraManager manager;
    private CameraDevice cameraDevice;
    private CameraCharacteristics characteristics;
    private Size previewSize;
    private CameraCaptureSession previewSession;
    private HandlerThread backgroundThread;
    private Handler backgroundHandler;

    private CaptureRequest.Builder previewBuilder;

    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(CameraDevice camera) {
            cameraDevice = camera;
            //startPreview();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            camera.close();
            cameraDevice = null;
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            camera.close();
            cameraDevice = null;
        }
    };
    private CameraCaptureSession.StateCallback previewStateCallback = new CameraCaptureSession.StateCallback() {

        @Override
        public void onConfigured(CameraCaptureSession session) {
            previewSession = session;
            updatePreview();
        }

        @Override
        public void onConfigureFailed(CameraCaptureSession session) {
            Toast.makeText(activity, "CameraCaptureSession: Configure failed!", Toast.LENGTH_LONG).show();
        }
    };

    public CameraUtil(CameraManager manager, Activity activity) {
        this.manager = manager;
        this.activity = activity;
    }

    public boolean openCamera() {
        String cameraID = findBackFacingCamera();

        if (cameraID == null) {
            return false;
        }

        StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        previewSize = map.getOutputSizes(SurfaceTexture.class)[0];
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return false;
        }
        try {
            manager.openCamera(cameraID, stateCallback, null);
        } catch (CameraAccessException e) {
            return false;
        }
        return true;
    }

    public void closeCamera() {
        if(cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    public boolean startPreview(TextureView cameraPreview) {
        if(cameraDevice == null || !cameraPreview.isAvailable() || previewSize == null) {
            return false;
        }

        SurfaceTexture texture = cameraPreview.getSurfaceTexture();
        if(texture == null) {
                return false;
        }

        texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
        Surface previewSurface = new Surface(texture);

        try {
            previewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            previewBuilder.addTarget(previewSurface);
            //cameraDevice.createCaptureSession(previewSurface, previewStateCallback, backgroundHandler);
        } catch (CameraAccessException e) {
            return false;
        }

        return true;
    }

    private boolean updatePreview() {
        if(cameraDevice == null) {
            return false;
        }

        previewBuilder.set(CaptureRequest.CONTROL_AF_MODE, CameraCharacteristics.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
        return true;
    }

    private String findBackFacingCamera() {
        String[] cameras = new String[0];
        try {
            cameras = manager.getCameraIdList();
            for(String camera : cameras) {
                CameraCharacteristics charac = manager.getCameraCharacteristics(camera);
                if(charac.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                    characteristics = charac;
                    return camera;
                }
            }
        } catch (CameraAccessException e) {
            return null;
        }
        return null;
    }

    private void startBackgroundThread() {
        backgroundThread = new HandlerThread("Camera Background");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        backgroundThread.quitSafely();
        try {
            backgroundThread.join();
            backgroundThread = null;
            backgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
