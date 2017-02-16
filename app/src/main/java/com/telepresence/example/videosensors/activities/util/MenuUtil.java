package com.telepresence.example.videosensors.activities.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by telepresence on 07.04.2016.
 */
public class MenuUtil {

    private Activity activity;

    // saveRateMenu
    public static final int SAVERATE_10HZ = 0;
    public static final int SAVERATE_15HZ = 1;
    public static final int[] saveRateValues = {100, 67};
    public static final String[] saveRateOptions = {"10 Hz", "15 Hz"};
    private int saveRate = saveRateValues[SAVERATE_10HZ];

    // quality menu
    public static final int QUALITY_1080P = 0;
    public static final int QUALITY_720P = 1;
    public static final int QUALITY_480P = 2;
    public static final String[] qualityOptions = {"1080p", "720p", "480p"};
    private int quality = QUALITY_720P;

    // frameRate menu
    public static final int FRAMERATE_10FPS = 0;
    public static final int FRAMERATE_20FPS = 1;
    public static final int FRAMERATE_30FPS = 2;
    public static final int[] frameRateValues = {10, 20, 30};
    public static final String[] frameRateOptions = {"10 fps", "20 fps", "30 fps"};
    private int frameRate = frameRateValues[FRAMERATE_30FPS];

    // saveModeMenu
    public static final int SAVEMODE_IMAGE = 0;
    public static final int SAVEMODE_VIDEO = 1;
    public static final String[] saveModeOptions = {"Image", "Video"};
    private int saveMode = SAVEMODE_VIDEO;

    public MenuUtil(Activity activity) {
        this.activity = activity;
    }

    public int openSaveRateMenu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        String setting = new String();
        if (saveRate == saveRateValues[SAVERATE_10HZ]) {
            setting = saveRateOptions[SAVERATE_10HZ];
        } else if (saveRate == saveRateValues[SAVERATE_15HZ]) {
            setting = saveRateOptions[SAVERATE_15HZ];
        }
        builder.setTitle("Pick Data Save Rate, Current setting: " + setting)
                .setItems(saveRateOptions, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        if (which == SAVERATE_10HZ) {
                            saveRate = saveRateValues[SAVERATE_10HZ];
                        } else if (which == SAVERATE_15HZ) {
                            saveRate = saveRateValues[SAVERATE_15HZ];
                        }
                    }
                });
        builder.show();

        return getSaveRate();
    }

    public int getSaveRate() {
        return saveRate;
    }

    public int openQualityMenu(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        String setting = new String();

        if(quality == QUALITY_1080P) {
            setting = qualityOptions[QUALITY_1080P];
        }
        else if(quality == QUALITY_720P){
            setting = qualityOptions[QUALITY_720P];
        }
        else if(quality == QUALITY_480P){
            setting = qualityOptions[QUALITY_480P];
        }
        builder.setTitle("Pick Quality, Current setting: " + setting)
                .setItems(qualityOptions, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        quality = which;
                    }
                });
        builder.show();

        return getQuality();
    }

    public int getQuality() {
        return quality;
    }

    public int openFrameRateMenu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        String setting = new String();
        if(frameRate == frameRateValues[FRAMERATE_10FPS]) {
            setting = frameRateOptions[FRAMERATE_10FPS];
        }
        else if(frameRate == frameRateValues[FRAMERATE_20FPS]) {
            setting = frameRateOptions[FRAMERATE_20FPS];
        }
        else if(frameRate == frameRateValues[FRAMERATE_30FPS]) {
            setting = frameRateOptions[FRAMERATE_30FPS];
        }
        builder.setTitle("Pick Video fps, Current setting: " + setting)
                .setItems(frameRateOptions, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        if(which == FRAMERATE_10FPS){
                            frameRate = frameRateValues[FRAMERATE_10FPS];
                        }
                        else if (which == FRAMERATE_20FPS){
                            frameRate = frameRateValues[FRAMERATE_20FPS];
                        }
                        else if (which == FRAMERATE_30FPS){
                            frameRate = frameRateValues[FRAMERATE_30FPS];
                        }
                    }
                });
        builder.show();

        return getFrameRate();
    }

    public int getFrameRate() {
        return frameRate;
    }

    public int openSaveModeMenu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        String setting = new String();
        if (saveMode == SAVEMODE_VIDEO) {
            setting = saveModeOptions[SAVEMODE_VIDEO];
        } else if (saveMode == SAVEMODE_IMAGE) {
            setting = saveModeOptions[SAVEMODE_IMAGE];
        }
        builder.setTitle("Pick Save Mode, Current setting: " + setting)
                .setItems(saveModeOptions, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        saveMode = which;
                    }
                });
        builder.show();

        return getSaveMode();
    }

    public int getSaveMode() {
        return saveMode;
    }
}
