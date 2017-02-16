package com.telepresence.example.videosensors.activities.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.telepresence.example.videosensors.R;
import com.telepresence.example.videosensors.activities.util.BeaconUtil;
import com.telepresence.example.videosensors.activities.util.WifiUtil;

/**
 * Created by telepresence on 07.04.2016.
 */
public class DistanceFragment extends Fragment {

    private TextView beaconView;
    private TextView wifiView;

    public static DistanceFragment newInstance() {
        return new DistanceFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_distance, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        beaconView = (TextView) getActivity().findViewById(R.id.tv_beacon);
        beaconView.setTextColor(Color.RED);
        wifiView = (TextView) getActivity().findViewById(R.id.tv_wifi);
        wifiView.setTextColor(Color.RED);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void updateViews(double[] beaconData, double[] wifiData) {
        beaconView.setText(     BeaconUtil.beacons[BeaconUtil.BEACON_1] + ": " + beaconData[BeaconUtil.BEACON_1] + "\n"
                            +   BeaconUtil.beacons[BeaconUtil.BEACON_2] + ": " + beaconData[BeaconUtil.BEACON_2] + "\n"
                            +   BeaconUtil.beacons[BeaconUtil.BEACON_3] + ": " + beaconData[BeaconUtil.BEACON_3] + "\n"
                            +   BeaconUtil.beacons[BeaconUtil.BEACON_4] + ": " + beaconData[BeaconUtil.BEACON_4]);

        wifiView.setText(       WifiUtil.accessPoints[WifiUtil.AP_1] + ": " + wifiData[WifiUtil.AP_1] + "\n"
                            +   WifiUtil.accessPoints[WifiUtil.AP_2] + ": " + wifiData[WifiUtil.AP_2] + "\n"
                            +   WifiUtil.accessPoints[WifiUtil.AP_3] + ": " + wifiData[WifiUtil.AP_3] + "\n"
                            +   WifiUtil.accessPoints[WifiUtil.AP_4] + ": " + wifiData[WifiUtil.AP_4] + "\n"
                            +   WifiUtil.accessPoints[WifiUtil.AP_5] + ": " + wifiData[WifiUtil.AP_5] + "\n"
                            +   WifiUtil.accessPoints[WifiUtil.AP_6] + ": " + wifiData[WifiUtil.AP_6] + "\n"
                            +   WifiUtil.accessPoints[WifiUtil.AP_7] + ": " + wifiData[WifiUtil.AP_7] + "\n"
                            +   WifiUtil.accessPoints[WifiUtil.AP_8] + ": " + wifiData[WifiUtil.AP_8]);
    }
}
