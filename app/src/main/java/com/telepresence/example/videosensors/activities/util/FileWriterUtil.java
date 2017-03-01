package com.telepresence.example.videosensors.activities.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.media.Image;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by telepresence on 07.04.2016.
 */
public class FileWriterUtil {

    // sensor types
    public static final int TYPE_BEACON = 6;
    public static final int TYPE_WIFI = 7;

    // directory names
    private static final String DIRECTORY_ROOT = "/videosensors";
    private static final String DIRECTORY_SENSOR = "/sensordata";
    private static final String DIRECTORY_IMAGE  = "/imagedata";
    private static final String DIRECTORY_VIDEO = "/videodata";

    // file names
    // motion sensors
    private static final String FILE_ACCELEROMETER = "accelerometer.csv";
    private static final String FILE_GRAVITY = "gravity.csv";
    private static final String FILE_GYROSCOPE = "gyroscope.csv";
    private static final String FILE_GYROSCOPE_UNCALIBRATED = "gyroscope_uncalibrated.csv";
    private static final String FILE_LINEAR_ACCELERATION = "linear_acceleration.csv";
    private static final String FILE_ROTATION_VECTOR = "rotation_vector.csv";
    // position sensors
    private static final String FILE_GAME_ROTATION_VECTOR = "game_rotation_vector.csv";
    private static final String FILE_GEOMAGNETIC_ROTATION_VECTOR = "geomagnetic_rotation_vector.csv";
    private static final String FILE_MAGNETIC_FIELD = "magnetic_field.csv";
    private static final String FILE_MAGNETIC_FIELD_UNCALIBRATED = "magentic_field_uncalibrated.csv";
    private static final String FILE_ORIENTATION = "orientation.csv";
    private static final String FILE_PROXIMITY = "proximity.csv";
    private static final String FILE_POSE_6DOF = "pose_6dof.csv";
    // wireless network signal strength
    private static final String FILE_BEACON = "beacons.csv";
    private static final String FILE_WIFI = "wifi.csv";

    // file headers
    // motion sensors
    private static final String HEADER_ACCELEROMETER = "Timestamp,Acc X (m/s^2),Acc Y (m/s^2),Acc Z (m/s^2)";
    private static final String HEADER_GRAVITY = "Timestamp,Force X (m/s^2),Force Y (m/s^2),Force Z (m/s^2)";
    private static final String HEADER_GYROSCOPE = "Timestamp,Gyro X (rad/s),Gyro Y (rad/s),Gyro Z (rad/s)";
    private static final String HEADER_GYROSCOPE_UNCALIBRATED = "Timestamp,Gyro X (rad/s),Gyro Y (rad/s),Gyro Z (rad/s),Estimated Drift X (rad/s),Estimated Drift Y (rad/s),Estimated Drift Z (rad/s)";
    private static final String HEADER_LINEAR_ACCELERATION = "Timestamp,linearAcc X (m/s^2),linearAcc Y (m/s^2),linearAcc Z (m/s^2)";
    private static final String HEADER_ROTATION_VECTOR = "Timestamp,Rot X (x * sin(theta/2)), Rot Y (y * sin(theta/2)),Rot Z (z * sin(theta/2)),cos(θ/2)";
    // position sensors
    private static final String HEADER_GAME_ROTATION_VECTOR = "Timestamp,Rot X (x * sin(theta/2)), Rot Y (y * sin(theta/2)),Rot Z (z * sin(theta/2)),cos(θ/2)";
    private static final String HEADER_GEOMAGNETIC_ROTATION_VECTOR = "Timestamp,Rot X (x * sin(theta/2)), Rot Y (y * sin(theta/2)),Rot Z (z * sin(theta/2))";
    private static final String HEADER_MAGNETIC_FIELD = "Timestamp,fieldStrength X (microT),fieldStrength Y (microT),fieldStrength Z (microT)";
    private static final String HEADER_MAGNETIC_FIELD_UNCALIBRATED = "Timestamp,fieldStrength X (microT),fieldStrength Y (microT),fieldStrength Z (microT),Estimated Bias X (microT),Estimated Bias Y (microT),Estimated Bias Z (microT)";
    private static final String HEADER_ORIENTATION = "Timestamp,Orientation (deg)";
    private static final String HEADER_PROXIMITY  = "Timestamp,Proximity (cm)";
    private static final String HEADER_POSE_6DOF = "Timestamp, x*sin(θ/2),y*sin(θ/2),z*sin(θ/2),cos(θ/2)" +
            "Translation along x axis from an arbitrary origin,Translation along y axis from an arbitrary origin,Translation along z axis from an arbitrary origin," +
            "Delta quaternion rotation x*sin(θ/2),Delta quaternion rotation y*sin(θ/2),Delta quaternion rotation z*sin(θ/2),Delta quaternion rotation cos(θ/2)" +
            "Delta translation along x axis,Delta translation along y axis,Delta translation along z axis," +
            "Sequence number";

    // wireless network signal strength
    private static final String HEADER_BEACON = "Timestamp";
    private static final String HEADER_WIFI = "Timestamp";

    //files
    private File rootDirectory;
    private File sensorDirectory;
    private File imageDirectory;
    private File videoDirectory;

    // filewriter
    private HashMap<Integer,PrintWriter> printWriters = new HashMap<>();
    private PrintWriter videoTimestampWriter;

    public FileWriterUtil() {
        openDirectories();
    }

    private void openDirectories() {
        rootDirectory = new File(Environment.getExternalStorageDirectory().getPath() + DIRECTORY_ROOT);
        rootDirectory.mkdirs();
        //openSensorDirectory();
    //    openImageDirectory();
        //openVideoDirectory();
    }

    public void openMeasDirectory(String timestamp, Set<Integer> activeSensors) {
        File measDirectory = new File(rootDirectory.getPath() + "/" + timestamp);
        sensorDirectory = new File(measDirectory.getPath() + DIRECTORY_SENSOR);
        imageDirectory = new File(measDirectory.getPath() + DIRECTORY_IMAGE);
        videoDirectory = new File(measDirectory.getPath() + DIRECTORY_VIDEO);

        measDirectory.mkdirs();
        sensorDirectory.mkdirs();
        imageDirectory.mkdirs();
        videoDirectory.mkdirs();

        for (int key : activeSensors) {
                buildWriter(key);
        }

        /*
        Set<Integer> keys = sensorOn.keySet();
        for (int key : keys) {
            if (sensorOn.get(key))
                buildWriter(key);
        }*/

        try {
            videoTimestampWriter = new PrintWriter(videoDirectory.getAbsoluteFile() + "/frame_timestamps.csv");
            videoTimestampWriter.println("Timestamp,frameNumber");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void buildWriter(int type) {
        switch (type) {
            case Sensor.TYPE_ACCELEROMETER: {
                PrintWriter writer = getWriter(sensorDirectory + "/" + FILE_ACCELEROMETER);
                writer.println(HEADER_ACCELEROMETER);
                printWriters.put(Sensor.TYPE_ACCELEROMETER, writer);
                break;
            }
            case Sensor.TYPE_GRAVITY: {
                PrintWriter writer = getWriter(sensorDirectory + "/" + FILE_GRAVITY);
                writer.println(HEADER_GRAVITY);
                printWriters.put(Sensor.TYPE_GRAVITY, writer);
                break;
            }
            case Sensor.TYPE_GYROSCOPE: {
                PrintWriter writer = getWriter(sensorDirectory + "/" + FILE_GYROSCOPE);
                writer.println(HEADER_GYROSCOPE);
                printWriters.put(Sensor.TYPE_GYROSCOPE, writer);
                break;
            }
            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED: {
                PrintWriter writer = getWriter(sensorDirectory + "/" + FILE_GYROSCOPE_UNCALIBRATED);
                writer.println(HEADER_GYROSCOPE_UNCALIBRATED);
                printWriters.put(Sensor.TYPE_GYROSCOPE_UNCALIBRATED, writer);
                break;
            }
            case Sensor.TYPE_LINEAR_ACCELERATION: {
                PrintWriter writer = getWriter(sensorDirectory + "/" + FILE_LINEAR_ACCELERATION);
                writer.println(HEADER_LINEAR_ACCELERATION);
                printWriters.put(Sensor.TYPE_LINEAR_ACCELERATION, writer);
                break;
            }
            case Sensor.TYPE_ROTATION_VECTOR: {
                PrintWriter writer = getWriter(sensorDirectory + "/" + FILE_ROTATION_VECTOR);
                writer.println(HEADER_ROTATION_VECTOR);
                printWriters.put(Sensor.TYPE_ROTATION_VECTOR, writer);
                break;
            }
            case Sensor.TYPE_GAME_ROTATION_VECTOR: {
                PrintWriter writer = getWriter(sensorDirectory + "/" + FILE_GAME_ROTATION_VECTOR);
                writer.println(HEADER_GAME_ROTATION_VECTOR);
                printWriters.put(Sensor.TYPE_GAME_ROTATION_VECTOR, writer);
                break;
            }
            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR: {
                PrintWriter writer = getWriter(sensorDirectory + "/" + FILE_GEOMAGNETIC_ROTATION_VECTOR);
                writer.println(HEADER_GEOMAGNETIC_ROTATION_VECTOR);
                printWriters.put(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR, writer);
                break;
            }
            case Sensor.TYPE_MAGNETIC_FIELD: {
                PrintWriter writer = getWriter(sensorDirectory + "/" + FILE_MAGNETIC_FIELD);
                writer.println(HEADER_MAGNETIC_FIELD);
                printWriters.put(Sensor.TYPE_MAGNETIC_FIELD, writer);
                break;
            }
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED: {
                PrintWriter writer = getWriter(sensorDirectory + "/" + FILE_MAGNETIC_FIELD_UNCALIBRATED);
                writer.println(HEADER_MAGNETIC_FIELD_UNCALIBRATED);
                printWriters.put(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED, writer);
                break;
            }
            case Sensor.TYPE_ORIENTATION: {
                PrintWriter writer = getWriter(sensorDirectory + "/" + FILE_ORIENTATION);
                writer.println(HEADER_ORIENTATION);
                printWriters.put(Sensor.TYPE_ORIENTATION, writer);
                break;
            }
            case Sensor.TYPE_PROXIMITY: {
                PrintWriter writer = getWriter(sensorDirectory + "/" + FILE_PROXIMITY);
                writer.println(HEADER_PROXIMITY);
                printWriters.put(Sensor.TYPE_PROXIMITY, writer);
                break;
            }
            case Sensor.TYPE_POSE_6DOF: {
                PrintWriter writer = getWriter(sensorDirectory + "/" + FILE_POSE_6DOF);
                writer.println(HEADER_POSE_6DOF);
                printWriters.put(Sensor.TYPE_POSE_6DOF, writer);
                break;
            }
        }

    }

    public void writeSensorFile(int type, String data) throws Exception {
        printWriters.get(type).println(data);
        /*switch(type) {
            case Sensor.TYPE_ORIENTATION: {
                if (orientationWriter != null) {
                    orientationWriter.println(data);
                    break;
                } else
                    throw new Exception("Orientation writer is null");
            }
            case Sensor.TYPE_ACCELEROMETER: {
                if (accWriter != null) {
                    accWriter.println(data);
                    break;
                } else
                    throw new Exception("Acceleration writer is null");
            }
            case Sensor.TYPE_LINEAR_ACCELERATION: {
                if (linearAccWriter != null) {
                    linearAccWriter.println(data);
                    break;
                } else
                    throw new Exception("Linear acceleration writer is null");
            }
            case Sensor.TYPE_GYROSCOPE: {
                if (gyroWriter != null) {
                    gyroWriter.println(data);
                    break;
                } else
                    throw new Exception("Gyro writer is null");
            }
            case Sensor.TYPE_MAGNETIC_FIELD: {
                if (magWriter != null) {
                    magWriter.println(data);
                    break;
                } else
                    throw new Exception("Magnetic field writer is null");
            }
            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR: {
                if (magRotWriter != null) {
                    magRotWriter.println(data);
                    break;
                } else
                    throw new Exception("Geomagnetic rotation writer is null");
            }
            case TYPE_BEACON: {
                if (beaconWriter != null) {
                    beaconWriter.println(data);
                    break;
                } else
                    throw new Exception("Beacon writer is null");
            }
            case TYPE_WIFI: {
                if (wifiWriter != null) {
                    wifiWriter.println(data);
                    break;
                } else
                    throw new Exception("Wifi writer is null");
            }
            default: throw new Exception("Invalid sensor type");
        }*/
    }

    public void writeVideoTimestampFile(long timestamp, long frameNumber) {
        if (videoTimestampWriter != null) {
            String data = timestamp + "," + frameNumber;
            videoTimestampWriter.println(data);
        }
    }

    public File openVideoFile(String name) {
        File videoFile = new File(videoDirectory, name + ".mp4");
        //videoFile.getParentFile().mkdirs();

        return videoFile;
    }

    public void writeImageFile(Image image, long timestamp) {
        File imageFile = new File(imageDirectory, timestamp + ".jpeg");
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(imageFile);
            output.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            image.close();
            if (null != output) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private PrintWriter getWriter(String filePath) {
        PrintWriter mWriter;
        try {
            File sensorFile = new File(filePath);
            sensorFile.getParentFile().mkdirs();
            mWriter = new PrintWriter(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        return mWriter;
    }

    public void closeWriters() {
        Set<Integer> keys = printWriters.keySet();
        for (int key : keys) {
            PrintWriter writer = printWriters.get(key);
            if (writer != null) {
                writer.close();
                printWriters.put(key, null);
            }
        }

        if (videoTimestampWriter != null) {
            videoTimestampWriter.close();
            videoTimestampWriter = null;
        }
    }

    public File getImageDirectory() {
        return imageDirectory;
    }

    public File getVideoDirectory() {
        return videoDirectory;
    }
}
