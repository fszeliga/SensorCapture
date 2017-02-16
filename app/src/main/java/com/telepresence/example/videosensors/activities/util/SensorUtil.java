package com.telepresence.example.videosensors.activities.util;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.telepresence.example.videosensors.activities.activities.MainActivity;
import com.telepresence.example.videosensors.activities.data.SettingModel;

import java.util.HashMap;

public class SensorUtil {

    // sensor
    private SensorManager manager;

    // sensor listeners
    private SensorEventListener accListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            try {
                fileWriter.writeSensorFile(Sensor.TYPE_ACCELEROMETER, getDataString(sensorEvent, 3));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
    private SensorEventListener gravityListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            try {
                fileWriter.writeSensorFile(Sensor.TYPE_GRAVITY, getDataString(sensorEvent, 3));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
    private SensorEventListener gyroListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            try {
                fileWriter.writeSensorFile(Sensor.TYPE_GYROSCOPE, getDataString(sensorEvent, 3));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
    private SensorEventListener gyroUncalibListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            try {
                fileWriter.writeSensorFile(Sensor.TYPE_GYROSCOPE_UNCALIBRATED, getDataString(sensorEvent, 6));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
    private SensorEventListener linearAccListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            try {
                fileWriter.writeSensorFile(Sensor.TYPE_LINEAR_ACCELERATION, getDataString(sensorEvent, 3));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
    private SensorEventListener rotVecListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            try {
                fileWriter.writeSensorFile(Sensor.TYPE_ROTATION_VECTOR, getDataString(sensorEvent, 3));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
    private SensorEventListener gameRotVecListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            try {
                fileWriter.writeSensorFile(Sensor.TYPE_GAME_ROTATION_VECTOR, getDataString(sensorEvent, 3));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
    private SensorEventListener geoRotVecListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            try {
                fileWriter.writeSensorFile(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR, getDataString(sensorEvent, 3));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
    private SensorEventListener magListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            try {
                fileWriter.writeSensorFile(Sensor.TYPE_MAGNETIC_FIELD, getDataString(sensorEvent, 3));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
    private SensorEventListener magUncalibVecListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            try {
                fileWriter.writeSensorFile(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED, getDataString(sensorEvent, 6));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
    private SensorEventListener orientationListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            try {
                fileWriter.writeSensorFile(Sensor.TYPE_ORIENTATION, getDataString(sensorEvent, 1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
    private SensorEventListener proxListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            try {
                fileWriter.writeSensorFile(Sensor.TYPE_PROXIMITY, getDataString(sensorEvent, 1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private HashMap<Integer,SensorEventListener> sensorListeners = new HashMap<>();

    // file writer
    private FileWriterUtil fileWriter;

    public SensorUtil(SensorManager manager, FileWriterUtil fileWriter) {
        this.manager = manager;
        buildListenerHashMap();
        this.fileWriter = fileWriter;
    }

    private void buildListenerHashMap() {
        sensorListeners.put(Sensor.TYPE_ACCELEROMETER, accListener);
        sensorListeners.put(Sensor.TYPE_GRAVITY, gravityListener);
        sensorListeners.put(Sensor.TYPE_GYROSCOPE, gyroListener);
        sensorListeners.put(Sensor.TYPE_GYROSCOPE_UNCALIBRATED, gyroUncalibListener);
        sensorListeners.put(Sensor.TYPE_LINEAR_ACCELERATION, linearAccListener);
        sensorListeners.put(Sensor.TYPE_ROTATION_VECTOR, rotVecListener);
        sensorListeners.put(Sensor.TYPE_GAME_ROTATION_VECTOR, gameRotVecListener);
        sensorListeners.put(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR, geoRotVecListener);
        sensorListeners.put(Sensor.TYPE_MAGNETIC_FIELD, magListener);
        sensorListeners.put(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED, magUncalibVecListener);
        sensorListeners.put(Sensor.TYPE_ORIENTATION, orientationListener);
        sensorListeners.put(Sensor.TYPE_PROXIMITY, proxListener);
    }

    public void registerListener(int type) {
        Sensor sensor = manager.getDefaultSensor(type);
        manager.registerListener(sensorListeners.get(type), sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void unregisterListener(int type) {
        manager.unregisterListener(sensorListeners.get(type));
    }

    private String getDataString(SensorEvent event, int cnt) {
        long timestamp = System.currentTimeMillis();
        String data = timestamp + "";
        for (int i = 0; i < cnt; i++)
            data = data + "," + event.values[i];
        return data;
    }
}
