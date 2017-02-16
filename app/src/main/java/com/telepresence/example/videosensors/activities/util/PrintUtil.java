package com.telepresence.example.videosensors.activities.util;

import android.util.Log;
import android.util.Range;


/**
 * Created by Filip on 2/8/2017.
 */

public class PrintUtil {

    public static String getRangeAsString(Range r){
       return new String("[ " + r.getLower() + " - " + r.getUpper() + " ]\n");
    }

    public static void printRanges(Range<Integer>[] r, String tag, String descr){
        String val = "";

        if(descr != "") {
            val += descr;
            val += ": ";
        }

        int len = val.length();

        val += getRangeAsString(r[0]);

        for(int i = 1; i < r.length; i++) {
            for(int j = 0; j < len; j++) val += " ";
                val += getRangeAsString(r[i]);
        }

        Log.i(tag, val);
    }
}
