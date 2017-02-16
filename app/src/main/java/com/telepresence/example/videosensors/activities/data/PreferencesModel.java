package com.telepresence.example.videosensors.activities.data;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Filip on 2/6/2017.
 */

public class PreferencesModel implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = "PreferencesModel";
    Map<String, Boolean> sensorsActive = new HashMap<>();

    public PreferencesModel(){
        //CheckBoxPreference imu_pref = (CheckBoxPreference)prefFragment.findPreference("switch_imu_control");
       // imu_pref.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.e(TAG, "onSharedPreferenceChanged: " + s + " " + sharedPreferences.getBoolean(s, true));
    }
}
