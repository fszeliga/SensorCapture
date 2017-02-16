package com.telepresence.example.videosensors.activities.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Size;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.telepresence.example.videosensors.R;
import com.telepresence.example.videosensors.activities.activities.MainActivity;
import com.telepresence.example.videosensors.activities.data.SettingModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by telepresence on 13.05.2016.
 */
public class SettingFragment extends PreferenceFragment {//implements View.OnClickListener {

    private Dialog dialog;
    //private SettingModel settings;

    private MainActivity activity;

    // gui
    private Spinner resolutionSpinner;
    private Spinner sensorRateSpinner;

    private CheckBox accCheckBox;
    private CheckBox gravityCheckBox;
    private CheckBox gyroCheckBox;
    private CheckBox gyroUncalibCheckBox;
    private CheckBox linAccCheckBox;
    private CheckBox rotVecCheckBox;
    private CheckBox gameRotVecCheckBox;
    private CheckBox geoRotVecCheckBox;
    private CheckBox magCheckBox;
    private CheckBox magUncalibCheckBox;
    private CheckBox orientationCheckBox;
    private CheckBox proxCheckBox;

    private List<Size> sizeList = new ArrayList<>();
    private List<String> spinnerList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        /*AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        activity = (MainActivity) getActivity();

        LinearLayout layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.fragment_settings, null);
        resolutionSpinner = (Spinner) layout.findViewById(R.id.spinner_resolution);
        sensorRateSpinner = (Spinner) layout.findViewById(R.id.spinner_sensorRate);

        accCheckBox = (CheckBox) layout.findViewById(R.id.switch_accleration);
        accCheckBox.setOnClickListener(this);
        gravityCheckBox = (CheckBox) layout.findViewById(R.id.switch_gravity);
        gravityCheckBox.setOnClickListener(this);
        gyroCheckBox = (CheckBox) layout.findViewById(R.id.switch_gyro);
        gyroCheckBox.setOnClickListener(this);
        gyroUncalibCheckBox = (CheckBox) layout.findViewById(R.id.switch_gyro_uncalib);
        gyroUncalibCheckBox.setOnClickListener(this);
        linAccCheckBox = (CheckBox) layout.findViewById(R.id.switch_linear_acc);
        linAccCheckBox.setOnClickListener(this);
        rotVecCheckBox = (CheckBox) layout.findViewById(R.id.switch_rot_vec);
        rotVecCheckBox.setOnClickListener(this);
        gameRotVecCheckBox = (CheckBox) layout.findViewById(R.id.switch_game_rot_vec);
        gameRotVecCheckBox.setOnClickListener(this);
        geoRotVecCheckBox = (CheckBox) layout.findViewById(R.id.switch_geo_rot_vec);
        geoRotVecCheckBox.setOnClickListener(this);
        magCheckBox = (CheckBox) layout.findViewById(R.id.switch_mag_field);
        magCheckBox.setOnClickListener(this);
        magUncalibCheckBox = (CheckBox) layout.findViewById(R.id.switch_mag_field_uncalib);
        magUncalibCheckBox.setOnClickListener(this);
        orientationCheckBox = (CheckBox) layout.findViewById(R.id.switch_orientation);
        orientationCheckBox.setOnClickListener(this);
        proxCheckBox = (CheckBox) layout.findViewById(R.id.switch_proximity);
        proxCheckBox.setOnClickListener(this);

        loadSettings();

        builder.setView(layout);
        builder.setTitle("Settings");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                settings.setSensorRate(settings.getSensorRateList().get(sensorRateSpinner.getSelectedItemPosition()));
                settings.setVideoProfile(settings.getProfileList().get(resolutionSpinner.getSelectedItemPosition()));
                activity.setSettings(settings);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog = builder.create();
        return dialog;*/
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.prefs);
    }

    private void loadSettings() {
        /*settings = activity.getSettings();

        // spinner
        ArrayAdapter<String> videoSizeAdapter;
        videoSizeAdapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_spinner_item, settings.getProfileStringList());
        resolutionSpinner.setAdapter(videoSizeAdapter);
        resolutionSpinner.setSelection(settings.getProfileList().indexOf(settings.getVideoProfile()));

        ArrayAdapter<String> sensorRateAdapter;
        sensorRateAdapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_spinner_item, settings.getSensorRateStringList());
        sensorRateSpinner.setAdapter(sensorRateAdapter);
        sensorRateSpinner.setSelection(settings.getPosition(settings.getSensorRate()));

        accCheckBox.setChecked(settings.getSensorOn(Sensor.TYPE_ACCELEROMETER));
        gravityCheckBox.setChecked(settings.getSensorOn(Sensor.TYPE_GRAVITY));
        gyroCheckBox.setChecked(settings.getSensorOn(Sensor.TYPE_GYROSCOPE));
        gyroUncalibCheckBox.setChecked(settings.getSensorOn(Sensor.TYPE_GYROSCOPE_UNCALIBRATED));
        linAccCheckBox.setChecked(settings.getSensorOn(Sensor.TYPE_LINEAR_ACCELERATION));
        rotVecCheckBox.setChecked(settings.getSensorOn(Sensor.TYPE_ROTATION_VECTOR));
        gameRotVecCheckBox.setChecked(settings.getSensorOn(Sensor.TYPE_GAME_ROTATION_VECTOR));
        geoRotVecCheckBox.setChecked(settings.getSensorOn(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR));
        magCheckBox.setChecked(settings.getSensorOn(Sensor.TYPE_MAGNETIC_FIELD));
        magUncalibCheckBox.setChecked(settings.getSensorOn(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED));
        orientationCheckBox.setChecked(settings.getSensorOn(Sensor.TYPE_ORIENTATION));
        proxCheckBox.setChecked(settings.getSensorOn(Sensor.TYPE_PROXIMITY));*/
    }

    /*// check box onclicklistener
    @Override
    public void onClick(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.switch_accleration: {
                settings.setSensorOn(Sensor.TYPE_ACCELEROMETER, checked);
                break;
            }
            case R.id.switch_gravity: {
                settings.setSensorOn(Sensor.TYPE_GRAVITY, checked);
                break;
            }
            case R.id.switch_gyro: {
                settings.setSensorOn(Sensor.TYPE_GYROSCOPE, checked);
                break;
            }
            case R.id.switch_gyro_uncalib: {
                settings.setSensorOn(Sensor.TYPE_GYROSCOPE_UNCALIBRATED, checked);
                break;
            }
            case R.id.switch_linear_acc: {
                settings.setSensorOn(Sensor.TYPE_LINEAR_ACCELERATION, checked);
                break;
            }
            case R.id.switch_rot_vec: {
                settings.setSensorOn(Sensor.TYPE_ROTATION_VECTOR, checked);
                break;
            }
            case R.id.switch_game_rot_vec: {
                settings.setSensorOn(Sensor.TYPE_GAME_ROTATION_VECTOR, checked);
                break;
            }case R.id.switch_geo_rot_vec: {
                settings.setSensorOn(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR, checked);
                break;
            }
            case R.id.switch_mag_field: {
                settings.setSensorOn(Sensor.TYPE_MAGNETIC_FIELD, checked);
                break;
            }
            case R.id.switch_mag_field_uncalib: {
                settings.setSensorOn(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED, checked);
                break;
            }
            case R.id.switch_orientation: {
                settings.setSensorOn(Sensor.TYPE_ORIENTATION, checked);
                break;
            }
            case R.id.switch_proximity: {
                settings.setSensorOn(Sensor.TYPE_PROXIMITY, checked);
                break;
            }
        }
    }*/
}
