package com.js.phonicdiary.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.js.phonicdiary.BuildConfig;

import static com.js.phonicdiary.BuildConfig.DEBUG;

/**
 *
 * Created by admin on 2016-04-26.
 */
public class LogE {
    public static boolean isLog = true;// 是否是debug模式

    public static void init(Context context){
        isLog = isApkDebugable(context);
    }

    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info= context.getApplicationInfo();
            return (info.flags&ApplicationInfo.FLAG_DEBUGGABLE)!=0;
        } catch (Exception e) {

        }
        return false;
    }


    public static void logE(String tag, String content) {
        int p = 2048;
        long length = content.length();
        if (length < p || length == p) {
            Log.e(tag, content);
        } else {
            while (content.length() > p) {
                String logContent = content.substring(0, p);

                content = content.replace(logContent, "");
                Log.e(tag, logContent);
            }
            Log.e(tag, content);
        }
    }

    //正式版置为false
    public static void e(String tag, String content) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, content);
        }
    }

}
