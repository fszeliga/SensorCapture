package com.telepresence.example.videosensors.activities.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.util.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Filip on 2/6/2017.
 */

public class PreferencesModel implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = "PreferencesModel";
    private final Context context;

    private Map<String, Boolean> sensorsActive = new HashMap<>();

    //will only contain available sensors for the device!!! rest will be in
    /**
     * @{sensorMap} will only contain available sensors for the device.
     * All sensors that are not available are in @link{unavailableSensors}
     */
    private Map<String, Integer> sensorMap = new HashMap<>();
    //private Map<String, Pair<>> sensorData = new HashMap<>();
    private Map<String, Integer> unavailableSensors = new HashMap<>();

    private boolean logAll;
    private int sensorSampleFrequency = SensorManager.SENSOR_DELAY_FASTEST;

    public PreferencesModel(Context context){
        this.context = context;

        //initializes @{sensorMap} with device available sensors and other map with sensors not available
        initSensorMap();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        logAll = prefs.getBoolean("switch_imu_control", false);
        Log.e(TAG, prefs.getString("pref_sample_rate", "0"));
        sensorSampleFrequency = Integer.parseInt(prefs.getString("pref_sample_rate", "0"));

        for(String k : sensorMap.keySet()){

            sensorsActive.put(k, prefs.getBoolean(k, false));
            Log.e(TAG, k + " exists: " +  prefs.getBoolean(k, false));
        }

    }

    void initSensorMap(){
        sensorMap.put("switch_acceleration", Sensor.TYPE_ACCELEROMETER);
        sensorMap.put("switch_gravity", Sensor.TYPE_GRAVITY);
        sensorMap.put("switch_orientation", Sensor.TYPE_ORIENTATION);
        sensorMap.put("switch_mag_field_uncalib", Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED);
        sensorMap.put("switch_game_rot_vec", Sensor.TYPE_GAME_ROTATION_VECTOR);
        sensorMap.put("switch_rot_vec", Sensor.TYPE_ROTATION_VECTOR);
        sensorMap.put("switch_geo_rot_vec", Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR);
        sensorMap.put("switch_mag_field", Sensor.TYPE_MAGNETIC_FIELD);
        sensorMap.put("switch_linear_acc", Sensor.TYPE_LINEAR_ACCELERATION);
        sensorMap.put("switch_gyro_uncalib", Sensor.TYPE_GYROSCOPE_UNCALIBRATED);
        sensorMap.put("switch_proximity", Sensor.TYPE_PROXIMITY);
        sensorMap.put("switch_gyro", Sensor.TYPE_GYROSCOPE);
        sensorMap.put("switch_pose_6dof", Sensor.TYPE_POSE_6DOF);

        //get sensor manager to check for available sensors
        SensorManager m = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> msensorList = m.getSensorList(Sensor.TYPE_ALL);

        for(String key : sensorMap.keySet()){
            boolean available = false;
            for(Sensor s : msensorList)
                if(s.getType() == sensorMap.get(key)) available = true;
            if(!available) unavailableSensors.put(key, sensorMap.get(key));
        }

        for(String unavailableKey : unavailableSensors.keySet())
            if(sensorMap.containsKey(unavailableKey))
                sensorMap.remove(unavailableKey);

    }

    public Set<String> getAvailableSensors(){
        return sensorMap.keySet();
    }

    public Set<String> getUnvailableSensors(){
        return unavailableSensors.keySet();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.e(TAG, "onSharedPreferenceChanged: " + key);//+" " + sharedPreferences.getBoolean(key, true));

        if(key == "switch_imi_control") logAll = sharedPreferences.getBoolean(key, true);

        if(sensorMap.containsKey(key)) sensorsActive.put(key, sharedPreferences.getBoolean(key, false));

        if(key == "pref_sample_rate") {
            sensorSampleFrequency = sharedPreferences.getInt("pref_sample_rate", 0);
        }
    }

    int getPositionInArray(String[] array, String key){
        for(int i = 0; i < array.length ; i++)
            if(array[i]==key) return i;
        return -1;
    }

    public boolean getValueForKey(String key){
        return sensorsActive.get(key);
    }

    public Set<Integer> getSensorsActive(){
        Set<Integer> res = new HashSet<>();
        for(String key : sensorsActive.keySet())
            if(sensorsActive.get(key))
                res.add(sensorMap.get(key));
        return res;
    }


    public int getSensorSampleFrequency() {
        /*
        SENSOR_DELAY_FASTEST = 0
        SENSOR_DELAY_GAME = 1
        SENSOR_DELAY_UI = 2
        SENSOR_DELAY_NORMAL = 3
         */
        return sensorSampleFrequency;
    }

    public static String sensorFrequencyToString(int freq){
        switch(freq){
            case SensorManager.SENSOR_DELAY_FASTEST:
                return "SENSOR_DELAY_FASTEST";
            case SensorManager.SENSOR_DELAY_GAME:
                return "SENSOR_DELAY_GAME";
            case SensorManager.SENSOR_DELAY_UI:
                return "SENSOR_DELAY_UI";
            case SensorManager.SENSOR_DELAY_NORMAL:
                return "SENSOR_DELAY_NORMAL";
        }
        return "NA";
    }
}
