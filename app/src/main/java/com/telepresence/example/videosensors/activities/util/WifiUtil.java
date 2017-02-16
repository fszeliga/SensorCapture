package com.telepresence.example.videosensors.activities.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.telepresence.example.videosensors.activities.activities.MainActivity;

import java.util.List;

/**
 * Created by telepresence on 14.04.2016.
 */
public class WifiUtil {
    private WifiManager manager;
    private WifiScanReceiver receiverWifi;
    private Activity activity;

    private static final int SCANRATE = 100;
    Thread scanThread = new Thread() {
        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    Thread.sleep(SCANRATE);
                    activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            manager.startScan();
                        }
                    });
                }

                //activity.unregisterReceiver(receiverWifi);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    // constants
    private static final String TARGET_SSID = "eduroam";
    public static final int AP_1 = 0;
    public static final int AP_2 = 1;
    public static final int AP_3 = 2;
    public static final int AP_4 = 3;
    public static final int AP_5 = 4;
    public static final int AP_6 = 5;
    public static final int AP_7 = 6;
    public static final int AP_8 = 7;
    public static final String[] accessPoints = {   "00:0b:0e:ee:67:84", "00:0b:0e:ee:67:85",
                                                    "00:0b:0e:ee:86:85", "00:0b:0e:ee:86:84",
                                                    "00:0b:0e:ee:8c:c4", "00:0b:0e:ee:5d:44",
                                                    "00:0b:0e:ee:88:84", "06:07:d5:01:11:07"};

    private double[] wifiData = new double[accessPoints.length];

    public WifiUtil(WifiManager manager, Activity activity) {
        this.activity = activity;
        this.manager = manager;
        enableWifi();
        receiverWifi = new WifiScanReceiver();
        activity.registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    public void startWifiScan() {
        scanThread.start();
    }

    public class WifiScanReceiver extends BroadcastReceiver {
        // This method call when number of wifi connections changed
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> resultList = manager.getScanResults();
            ScanResult result;
            for (int i = 0; i < resultList.size(); i++) {
                result = resultList.get(i);
                if(result.SSID.equals(TARGET_SSID)) {
                    int resultID = checkBSSID(result);
                    if(resultID != -1) {
                        wifiData[resultID] = result.level;
                    }
                }
            }

            //((MainActivity) activity).updateWifiData(wifiData);
        }
    }

    private int checkBSSID(ScanResult result) {
        if (result.BSSID.equals(accessPoints[AP_1])) {
            return AP_1;
        }
        if (result.BSSID.equals(accessPoints[AP_2])) {
            return AP_2;
        }
        if (result.BSSID.equals(accessPoints[AP_3])) {
            return AP_3;
        }
        if (result.BSSID.equals(accessPoints[AP_4])) {
            return AP_4;
        }
        if (result.BSSID.equals(accessPoints[AP_5])) {
            return AP_5;
        }
        if (result.BSSID.equals(accessPoints[AP_6])) {
            return AP_6;
        }
        if (result.BSSID.equals(accessPoints[AP_7])) {
            return AP_7;
        }
        if (result.BSSID.equals(accessPoints[AP_8])) {
            return AP_8;
        }

        return -1;
    }

    public double[] getWifiData() {
        return wifiData;
    }

    private void enableWifi() {
        if(!manager.isWifiEnabled()) {
            manager.setWifiEnabled(true);
        }
    }
}
