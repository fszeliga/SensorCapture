package com.telepresence.example.videosensors.activities.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.telepresence.example.videosensors.activities.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by telepresence on 07.04.2016.
 */
public class BeaconUtil {

    //activity
    public MainActivity activity;
    // constants
    public static final int BEACON_1 = 0;
    public static final int BEACON_2 = 1;
    public static final int BEACON_3 = 2;
    public static final int BEACON_4 = 3;
    public static final int BEACON_5 = 4;
    public static final int BEACON_6 = 5;
    public static final String[] beacons = {"Enk3", "VmP7", "KxHw", "e4m4", "HJZB", "iObH"};

    private double[] beaconValues = new double[beacons.length];

    private static final int SCAN_INTERVAL_MS = 100;
    private Handler scanHandler = new Handler();
    private List<ScanFilter> scanFilters = new ArrayList<ScanFilter>();
    private ScanSettings scanSettings;
    private boolean isScanning = false;

    private BluetoothAdapter btAdapter;

    public BeaconUtil(MainActivity activity) {
        this.activity = activity;
    }

    public void beginScanning() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        enableBT();

        ScanSettings.Builder scanSettingsBuilder = new ScanSettings.Builder();
        scanSettingsBuilder.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
        scanSettings = scanSettingsBuilder.build();

        scanHandler.post(scanRunnable);
    }

    private Runnable scanRunnable = new Runnable() {
        @Override
        public void run() {
            BluetoothLeScanner scanner = btAdapter.getBluetoothLeScanner();

            if (isScanning && scanner != null) {
                scanner.stopScan(scanCallback);
            } else if(scanner != null) {
                scanner.startScan(scanFilters, scanSettings, scanCallback);
            }

            isScanning = !isScanning;

            scanHandler.postDelayed(this, SCAN_INTERVAL_MS);
        }
    };

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            String tmp = result.getDevice().getName();
        //    Log.e("Beacon", tmp);
            if(tmp == null)
                return;

            if(tmp.equals(beacons[BEACON_1]))
                beaconValues[BEACON_1] = calculateDistance(result.getRssi());
            if(tmp.equals(beacons[BEACON_2]))
                beaconValues[BEACON_2] = calculateDistance(result.getRssi());
            if(tmp.equals(beacons[BEACON_3]))
                beaconValues[BEACON_3] = calculateDistance(result.getRssi());
            if(tmp.equals(beacons[BEACON_4]))
                beaconValues[BEACON_4] = calculateDistance(result.getRssi());
            if(tmp.equals(beacons[BEACON_5]))
                beaconValues[BEACON_5] = calculateDistance(result.getRssi());
            if(tmp.equals(beacons[BEACON_6]))
                beaconValues[BEACON_6] = calculateDistance(result.getRssi());

            //activity.updateBeaconData(beaconValues);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };

    private double calculateDistance(int rssi) {
        //double result = Math.exp((-80 - 3)/ (-42));
        double result = Math.pow(10, (Double.valueOf(rssi) + 12) / (-42));
        //return result;
        return rssi;
    }

    public double[] getBeaconValues() {
        return beaconValues;
    }

    private void enableBT() {
        if(!btAdapter.isEnabled()) {
            btAdapter.enable();
        }
    }

    public boolean isScanning() {
        return isScanning;
    }

    public void setIsScanning(boolean isScanning) {
        this.isScanning = isScanning;
    }
}
