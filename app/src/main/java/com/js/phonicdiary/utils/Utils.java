package com.js.phonicdiary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.js.phonicdiary.R;

import java.io.File;
import java.util.Locale;

public class Utils {

    /**
     *  打开zip文件
     * @param activity 上下文
     */
    public static void getZipFile(Activity activity){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/zip");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        activity.startActivityForResult(intent,1001);
    }

    /**
     * 分享文本
     *
     * @param mContext
     * @param content
     */
    public static void share(Context mContext, String content) {
        try {
            Intent intentSend = new Intent(Intent.ACTION_SEND);
            intentSend.setType("text/plain");
            intentSend.putExtra(Intent.EXTRA_SUBJECT, mContext.getResources().getString(R.string.Get_Phonic_Diary));
            intentSend.putExtra(Intent.EXTRA_TEXT, content);
            intentSend.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Intent intentShare = Intent.createChooser(intentSend, mContext.getResources().getString(R.string.Share_Phonic_Diary));
            intentShare.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intentShare);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 分享zip文件
     *
     * @param mContext
     * @param path 文件路径
     */
    public static void shareFile(Context mContext, String path) {
        try {
            Intent intentSend = new Intent(Intent.ACTION_SEND);
            intentSend.putExtra(Intent.EXTRA_STREAM, FileUtils.getUriWithPath(mContext, path));
            intentSend.setType("application/zip");
            mContext.startActivity(intentSend);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 将字符串转化为int
     *
     * @param str
     * @return
     */
    public static int intValueOf(String str) {
        if (str != null && !"".equals(str)) {
            try {
                return Integer.valueOf(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 打开其他播放音频的软件
     *
     * @param context 上下文
     * @param url     地址
     */
    public static void openWithAudio(Context context, String url) {
        if (LogE.isLog) {
            LogE.e("wbb", "开始方式");
        }
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            String type = "audio/*";
            Uri uri = FileUtils.getUriWithPath(context, url);
            intent.setDataAndType(uri, type);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断语言是否是中文
     *
     * @param context
     * @return
     */
    public static boolean isZh(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        return language.endsWith("zh");
    }
}
