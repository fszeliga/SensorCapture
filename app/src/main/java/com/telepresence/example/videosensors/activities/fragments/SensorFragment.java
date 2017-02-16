package com.telepresence.example.videosensors.activities.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.telepresence.example.videosensors.R;
import com.telepresence.example.videosensors.activities.util.SensorUtil;

public class SensorFragment extends Fragment {

    private TextView mTextView;

    public static SensorFragment newInstance() {
        return new SensorFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sensor, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        mTextView = (TextView) view.findViewById(R.id.textViewHeading);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void updateViews(int orientation) {
        mTextView.setText("Orientation: " + orientation);
    }

}
