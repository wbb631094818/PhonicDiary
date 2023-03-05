package com.js.phonicdiary.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.js.phonicdiary.DiaryApplication;

/**
 *
 */
public class AppPreferences {


    /**
     * 设置密码
     */
    public static void setLocalPasscode(String passcode) {
        SharedPreferences preferences = DiaryApplication.getInstance().getSharedPreferences("LocalPasscode",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("LocalPasscode", passcode);
        editor.apply();
    }

    /**
     * 获取密码
     */
    public static String getLocalPasscode() {
        SharedPreferences preferences = DiaryApplication.getInstance().getSharedPreferences("LocalPasscode",
                Context.MODE_PRIVATE);
        return preferences.getString("LocalPasscode", "0000");
    }

    /**
     * 是否打开密码锁
     */
    public static void setOpenCodedLock(boolean isopen) {
        SharedPreferences preferences = DiaryApplication.getInstance().getSharedPreferences("OpenCodedLock",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("OpenCodedLock", isopen);
        editor.apply();
    }

    /**
     * 获取是否打开密码锁
     */
    public static boolean isOpenCodedLock() {
        SharedPreferences preferences = DiaryApplication.getInstance().getSharedPreferences("OpenCodedLock",
                Context.MODE_PRIVATE);
        return preferences.getBoolean("OpenCodedLock", false);
    }

    /**
     * 是否使用指纹密码锁
     */
    public static void setOpenFingerprintCodedLock(boolean isopen) {
        SharedPreferences preferences = DiaryApplication.getInstance().getSharedPreferences("OpenFingerprintCodedLock",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("OpenFingerprintCodedLock", isopen);
        editor.apply();
    }

    /**
     * 获取使用指纹密码锁
     */
    public static boolean isOpenFingerprintCodedLock() {
        SharedPreferences preferences = DiaryApplication.getInstance().getSharedPreferences("OpenFingerprintCodedLock",
                Context.MODE_PRIVATE);
        return preferences.getBoolean("OpenFingerprintCodedLock", false);
    }


    /**
     * 是否打开一键录音按钮
     */
    public static void setOpenAKeyRecording(boolean isopen) {
        SharedPreferences preferences = DiaryApplication.getInstance().getSharedPreferences("OpenAKeyRecording",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("OpenAKeyRecording", isopen);
        editor.apply();
    }

    /**
     * 获取打开一键录音按钮
     */
    public static boolean isOpenAKeyRecording() {
        SharedPreferences preferences = DiaryApplication.getInstance().getSharedPreferences("OpenAKeyRecording",
                Context.MODE_PRIVATE);
        return preferences.getBoolean("OpenAKeyRecording", false);
    }

    /**
     * 是否展示日记天气
     */
    public static void setShowDiaryWeather(boolean isShow) {
        SharedPreferences preferences = DiaryApplication.getInstance().getSharedPreferences("ShowDiaryWeather",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("ShowDiaryWeather", isShow);
        editor.apply();
    }

    /**
     * 获取是否展示日记天气
     */
    public static boolean isShowDiaryWeather() {
        SharedPreferences preferences = DiaryApplication.getInstance().getSharedPreferences("ShowDiaryWeather",
                Context.MODE_PRIVATE);
        return preferences.getBoolean("ShowDiaryWeather", true);
    }

    /**
     * 是否展示日记标题
     */
    public static void setShowDiaryTitle(boolean isShow) {
        SharedPreferences preferences = DiaryApplication.getInstance().getSharedPreferences("ShowDiaryTitle",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("ShowDiaryTitle", isShow);
        editor.apply();
    }

    /**
     * 获取是否展示日记标题
     */
    public static boolean isShowDiaryTitle() {
        SharedPreferences preferences = DiaryApplication.getInstance().getSharedPreferences("ShowDiaryTitle",
                Context.MODE_PRIVATE);
        return preferences.getBoolean("ShowDiaryTitle", true);
    }
    /**
     * 是否开启自动备份
     */
    public static void setOpenAutoBackup(boolean isShow) {
        SharedPreferences preferences = DiaryApplication.getInstance().getSharedPreferences("OpenAutoBackup",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("OpenAutoBackup", isShow);
        editor.apply();
    }

    /**
     * 获取是否开启自动备份
     */
    public static boolean isOpenAutoBackup() {
        SharedPreferences preferences = DiaryApplication.getInstance().getSharedPreferences("OpenAutoBackup",
                Context.MODE_PRIVATE);
        return preferences.getBoolean("OpenAutoBackup", false);
    }


}
