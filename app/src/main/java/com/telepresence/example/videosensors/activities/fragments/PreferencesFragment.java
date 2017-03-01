package com.telepresence.example.videosensors.activities.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.telepresence.example.videosensors.R;
import com.telepresence.example.videosensors.activities.activities.MainActivity;
import com.telepresence.example.videosensors.activities.data.PreferencesModel;
import com.telepresence.example.videosensors.activities.data.SettingModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by telepresence on 13.05.2016.
 */
public class PreferencesFragment extends PreferenceFragment {

    private PreferencesModel preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.prefs);

        for(String key : this.preferences.getUnvailableSensors()){
            findPreference(key).setEnabled(false);
        }

    }

    public void setPreferencesModel(PreferencesModel preferences){
        Log.e("pref fragment", "setPreferencesModel");

        this.preferences = preferences;
    }
}
