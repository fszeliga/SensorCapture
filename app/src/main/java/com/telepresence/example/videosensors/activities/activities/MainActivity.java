package com.telepresence.example.videosensors.activities.activities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.telepresence.example.videosensors.R;
import com.telepresence.example.videosensors.activities.data.DataModel;
import com.telepresence.example.videosensors.activities.data.PreferencesModel;
import com.telepresence.example.videosensors.activities.fragments.CameraFragment;
import com.telepresence.example.videosensors.activities.fragments.PreferencesFragment;
import com.telepresence.example.videosensors.activities.util.FileWriterUtil;
import com.telepresence.example.videosensors.activities.util.SensorUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    // constants
    private static final int MY_PERMISSIONS_REQUEST = 0;
    private static final String TAG = "MainActivity";

    // file writer
    private FileWriterUtil fileWriter;
    private String timeStampFile;

    // fragments
    private CameraFragment cameraFragment;
    //private Camera2Fragment cameraFragment;
    private PreferencesFragment preferencesFragment;

    // GUI components
    private Chronometer chrono;
    private ImageButton captureButton;
    private ImageButton settingsButton;
    private TextView statusView;

    // manager
    private SensorUtil sensorManager;

    private boolean recording = false;

    // data model
    private DataModel dataModel = new DataModel();
    private PreferencesModel preferences;
    //private SettingModel settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
        {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.CAMERA,
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST);
        }

        // get fragments
        cameraFragment = CameraFragment.newInstance();
        //cameraFragment = Camera2Fragment.newInstance();
        preferences = new PreferencesModel(this);
        preferencesFragment = new PreferencesFragment();
        preferencesFragment.setPreferencesModel(preferences);

        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction().replace(R.id.container_camera, cameraFragment).commit();
        }

        // initialize components
        chrono = (Chronometer) findViewById(R.id.chronometer);
        captureButton = (ImageButton) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(this);
        settingsButton = (ImageButton) findViewById(R.id.button_settings);
        settingsButton.setOnClickListener(this);
        statusView = (TextView) findViewById(R.id.tv_status);
        statusView.setTextColor(Color.RED);

        fileWriter = new FileWriterUtil();

        // initialize managers
        sensorManager = new SensorUtil((SensorManager) getSystemService(SENSOR_SERVICE), fileWriter);
    }

    // onClickListener
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button_capture: {
                captureButtonClick();
                break;
            }

            case R.id.button_settings: {
                getFragmentManager().beginTransaction().replace(R.id.container_camera, preferencesFragment).addToBackStack(null).commit();

                captureButton.setVisibility(View.INVISIBLE);
                settingsButton.setVisibility(View.INVISIBLE);
                statusView.setVisibility(View.INVISIBLE);
                chrono.setVisibility(View.INVISIBLE);

                break;
            }
            default:
                break;
        }
    }

    private void captureButtonClick() {

        //for(String key : preferences.getSensorsActive().keySet()){
         //   Log.e(TAG, key + ": " + preferences.getValueForKey(key));
        //}

        if (recording) {
            cameraFragment.stopRecording();
            settingsButton.setVisibility(View.VISIBLE);
            chrono.stop();
            chrono.setBase(SystemClock.elapsedRealtime());

            //Set<Integer> keys = settings.getSensorOn().keySet();
             for (int key : preferences.getSensorsActive()) {
                 sensorManager.unregisterListener(key);
            }

            // media recorder
            //cameraFragment.stopRecordingVideo();

            fileWriter.closeWriters();

            recording = false;
            statusView.setTextColor(Color.RED);
        } else {
            settingsButton.setVisibility(View.INVISIBLE);

            openFiles();

            //Set<Integer> keys = settings.getSensorOn().keySet();
            for (int key : preferences.getSensorsActive()) {
                sensorManager.registerListener(key, preferences.getSensorSampleFrequency());
            }

            Toast.makeText(this,
                    "SensorListeners: "+preferences.getSensorsActive().size() +
                            "\nSensor speed: " + PreferencesModel.sensorFrequencyToString(preferences.getSensorSampleFrequency()),
                    Toast.LENGTH_LONG).show();

            chrono.setBase(SystemClock.elapsedRealtime());
            chrono.start();
            recording = true;
            statusView.setTextColor(Color.GREEN);

            cameraFragment.startRecording(fileWriter.getVideoDirectory());
        }
    }

    private void openFiles() {
        timeStampFile = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        //TODO change to real sensors map
        fileWriter.openMeasDirectory(timeStampFile, preferences.getSensorsActive());//settings.getSensorOn());
    }

    // permissions
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Du bist ein Spast!", Toast.LENGTH_LONG);
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            captureButton.setVisibility(View.VISIBLE);
            settingsButton.setVisibility(View.VISIBLE);
            statusView.setVisibility(View.VISIBLE);
            chrono.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.e(TAG, "onResume");
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).registerOnSharedPreferenceChangeListener(preferences);
    }
    @Override
    public void onPause(){
        super.onPause();        Log.e(TAG, "onPause");

        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).unregisterOnSharedPreferenceChangeListener(preferences);

    }

    public FileWriterUtil getFileWriter() {
        return fileWriter;
    }
}
