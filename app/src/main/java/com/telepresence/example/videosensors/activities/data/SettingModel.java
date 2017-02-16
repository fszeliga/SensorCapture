package com.telepresence.example.videosensors.activities.data;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.CamcorderProfile;

import com.telepresence.example.videosensors.R;
import com.telepresence.example.videosensors.activities.activities.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by telepresence on 13.05.2016.
 */
public class SettingModel {

    // display data
    private boolean displayData = false;

    private List<Integer> profileList = new ArrayList<>();
    private List<String> profileStringList = new ArrayList<>();
    private int videoProfile;

    // video settings
    private int resolution;
    private int framerate;

    // sensor settings
    private int sensorRate = SensorManager.SENSOR_DELAY_FASTEST;
    private List<Integer> sensorRateList = new ArrayList<>();
    private List<String> sensorRateStringList = new ArrayList<>();
    private HashMap<Integer,Boolean> sensorOn = new HashMap<>();

    public SettingModel() {
        prepareVideoProfiles();
        prepareSensorRateList();
        prepareSensorOnArray();
    }

    private void prepareSensorRateList() {
        sensorRateStringList.add("Fastest");
        sensorRateStringList.add("Fast");
        sensorRateStringList.add("Medium");
        sensorRateStringList.add("Slow");

        sensorRateList.add(SensorManager.SENSOR_DELAY_FASTEST);
        sensorRateList.add(SensorManager.SENSOR_DELAY_GAME);
        sensorRateList.add(SensorManager.SENSOR_DELAY_NORMAL);
        sensorRateList.add(SensorManager.SENSOR_DELAY_UI);
    }

    private void prepareVideoProfiles() {
        profileList.add(CamcorderProfile.QUALITY_2160P);
        profileList.add(CamcorderProfile.QUALITY_1080P);
        profileList.add(CamcorderProfile.QUALITY_720P);
        profileList.add(CamcorderProfile.QUALITY_480P);

        profileStringList.add("2160P");
        profileStringList.add("1080P");
        profileStringList.add("720P");
        profileStringList.add("480P");

        for(int i = 0; i < profileList.size(); i++)
            if(!checkProfile(profileList.get(i))) {
                deleteProfile(i);
            }

        videoProfile = profileList.get(0);
    }

    private void prepareSensorOnArray() {
        sensorOn.put(Sensor.TYPE_ACCELEROMETER, true);
        sensorOn.put(Sensor.TYPE_GRAVITY, true);
        sensorOn.put(Sensor.TYPE_GYROSCOPE, true);
        sensorOn.put(Sensor.TYPE_GYROSCOPE_UNCALIBRATED, true);
        sensorOn.put(Sensor.TYPE_LINEAR_ACCELERATION, true);
        sensorOn.put(Sensor.TYPE_ROTATION_VECTOR, true);
        sensorOn.put(Sensor.TYPE_GAME_ROTATION_VECTOR, true);
        sensorOn.put(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR, true);
        sensorOn.put(Sensor.TYPE_MAGNETIC_FIELD, true);
        sensorOn.put(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED, true);
        sensorOn.put(Sensor.TYPE_ORIENTATION, true);
        sensorOn.put(Sensor.TYPE_PROXIMITY, true);
    }

    public int getPosition(int sensorRate) {
        return sensorRateList.indexOf(sensorRate);
    }

    private boolean checkProfile(int quality) {
        if (CamcorderProfile.hasProfile(quality))
            return true;
        else
            return false;
    }

    public void deleteProfile(int position) {
        profileStringList.remove(position);
        profileList.remove(position);
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public int getFramerate() {
        return framerate;
    }

    public void setFramerate(int framerate) {
        this.framerate = framerate;
    }

    public int getSensorRate() {
        return sensorRate;
    }

    public void setSensorRate(int sensorRate) {
        this.sensorRate = sensorRate;
    }

    public List<Integer> getSensorRateList() {
        return sensorRateList;
    }

    public void setSensorRateList(List<Integer> sensorRateList) {
        this.sensorRateList = sensorRateList;
    }

    public List<String> getSensorRateStringList() {
        return sensorRateStringList;
    }

    public void setSensorRateStringList(List<String> sensorRateStringList) {
        this.sensorRateStringList = sensorRateStringList;
    }

    public List<Integer> getProfileList() {
        return profileList;
    }

    public void setProfileList(List<Integer> profileList) {
        this.profileList = profileList;
    }

    public List<String> getProfileStringList() {
        return profileStringList;
    }

    public void setProfileStringList(List<String> profileStringList) {
        this.profileStringList = profileStringList;
    }

    public int getVideoProfile() {
        return videoProfile;
    }

    public void setVideoProfile(int videoProfile) {
        this.videoProfile = videoProfile;
    }

    public boolean isDisplayData() {
        return displayData;
    }

    public void setDisplayData(boolean displayData) {
        this.displayData = displayData;
    }

    public void setSensorOn(int type, boolean state) {
        sensorOn.put(type, state);
    }

    public boolean getSensorOn(int type) {
        return sensorOn.get(type);
    }

    public HashMap<Integer,Boolean> getSensorOn() {
        return sensorOn;
    }
}
