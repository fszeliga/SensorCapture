package com.telepresence.example.videosensors.activities.data;

import com.telepresence.example.videosensors.activities.util.BeaconUtil;
import com.telepresence.example.videosensors.activities.util.WifiUtil;

/**
 * Created by telepresence on 10.05.2016.
 */
public class DataModel {

    // sensor values
    private float orientation = 0;
    private float linearAccX = 0;
    private float linearAccY = 0;
    private float linearAccZ = 0;
    private float accX = 0;
    private float accY = 0;
    private float accZ = 0;
    private float gyroX = 0;
    private float gyroY = 0;
    private float gyroZ = 0;

    // beacondata
    private double[] beaconValues = new double[BeaconUtil.beacons.length];

    // wifi data
    private double[] wifiData = new double[WifiUtil.accessPoints.length];


    public String getFileHeader() {
        String header = "Time (ns)" + "," + "Acc X (m/s^2)" + "," + "Acc Y (m/s^2)" + "," + "Acc Z (m/s^2)" + "," + "linearAcc X (m/s^2)" + "," + "linearAcc Y (m/s^2)" + "," + "linearAcc Z (m/s^2)" + "," + "Orientation (deg)" + "," + "gyro_x (rad/s)" + "," + "gyro_y (rad/s)" + "," + "gyro_z (rad/s)"
                + "," + BeaconUtil.beacons[BeaconUtil.BEACON_1]
                + "," + BeaconUtil.beacons[BeaconUtil.BEACON_2]
                + "," + BeaconUtil.beacons[BeaconUtil.BEACON_3]
                + "," + BeaconUtil.beacons[BeaconUtil.BEACON_4]
                + "," + BeaconUtil.beacons[BeaconUtil.BEACON_5]
                + "," + BeaconUtil.beacons[BeaconUtil.BEACON_6]
               /* + "," + WifiUtil.accessPoints[WifiUtil.AP_1]
                + "," + WifiUtil.accessPoints[WifiUtil.AP_2]
                + "," + WifiUtil.accessPoints[WifiUtil.AP_3]
                + "," + WifiUtil.accessPoints[WifiUtil.AP_4]
                + "," + WifiUtil.accessPoints[WifiUtil.AP_5]
                + "," + WifiUtil.accessPoints[WifiUtil.AP_6]
                + "," + WifiUtil.accessPoints[WifiUtil.AP_7]
                + "," + WifiUtil.accessPoints[WifiUtil.AP_8]*/;

        return header;
    }

    public String getDataString(long elapsedMillis) {
        String data = elapsedMillis
                + "," + accX
                + "," + accY
                + "," + accZ
                + "," + linearAccX
                + "," + linearAccY
                + "," + linearAccZ
                + "," + orientation
                + "," + gyroX
                + "," + gyroY
                + "," + gyroZ
                + "," + beaconValues[BeaconUtil.BEACON_1]
                + "," + beaconValues[BeaconUtil.BEACON_2]
                + "," + beaconValues[BeaconUtil.BEACON_3]
                + "," + beaconValues[BeaconUtil.BEACON_4]
                + "," + beaconValues[BeaconUtil.BEACON_5]
                + "," + beaconValues[BeaconUtil.BEACON_6]
                /*+ "," + wifiData[WifiUtil.AP_1]
                + "," + wifiData[WifiUtil.AP_2]
                + "," + wifiData[WifiUtil.AP_3]
                + "," + wifiData[WifiUtil.AP_4]
                + "," + wifiData[WifiUtil.AP_5]
                + "," + wifiData[WifiUtil.AP_6]
                + "," + wifiData[WifiUtil.AP_7]
                + "," + wifiData[WifiUtil.AP_8]*/;

        return data;
    }

    // getter & setter
    public double[] getBeaconValues() {
        return beaconValues;
    }

    public void setBeaconValues(double[] beaconValues) {
        this.beaconValues = beaconValues;
    }

    public double[] getWifiData() {
        return wifiData;
    }

    public void setWifiData(double[] wifiData) {
        this.wifiData = wifiData;
    }

    public float getOrientation() {
        return orientation;
    }

    public void setOrientation(float orientation) {
        this.orientation = orientation;
    }

    public float getLinearAccX() {
        return linearAccX;
    }

    public void setLinearAccX(float linearAccX) {
        this.linearAccX = linearAccX;
    }

    public float getLinearAccY() {
        return linearAccY;
    }

    public void setLinearAccY(float linearAccY) {
        this.linearAccY = linearAccY;
    }

    public float getLinearAccZ() {
        return linearAccZ;
    }

    public void setLinearAccZ(float linearAccZ) {
        this.linearAccZ = linearAccZ;
    }

    public void setLinearAcc(float[] data) {
        linearAccX = data[0];
        linearAccY = data[1];
        linearAccZ = data[2];
    }

    public float getAccX() {
        return accX;
    }

    public void setAccX(float accX) {
        this.accX = accX;
    }

    public float getAccY() {
        return accY;
    }

    public void setAccY(float accY) {
        this.accY = accY;
    }

    public float getAccZ() {
        return accZ;
    }

    public void setAccZ(float accZ) {
        this.accZ = accZ;
    }

    public void setAcc(float[] data) {
        accX = data[0];
        accY = data[1];
        accZ = data[2];
    }

    public float getGyroX() {
        return gyroX;
    }

    public void setGyroX(float gyroX) {
        this.gyroX = gyroX;
    }

    public float getGyroY() {
        return gyroY;
    }

    public void setGyroY(float gyroY) {
        this.gyroY = gyroY;
    }

    public float getGyroZ() {
        return gyroZ;
    }

    public void setGyroZ(float gyroZ) {
        this.gyroZ = gyroZ;
    }

    public void setGyro(float[] data) {
        gyroX = data[0];
        gyroY = data[1];
        gyroZ = data[2];
    }
}
