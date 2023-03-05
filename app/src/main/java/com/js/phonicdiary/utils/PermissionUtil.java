package com.js.phonicdiary.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.List;

/**
 * 动态获取权限工具类
 * Created by 王兵兵 on 2018/5/22.
 */

public class PermissionUtil {

    /**
     * 请求获取录音及sd卡相关权限
     *
     * @param context 上下文
     */
    public static void requestMicrophoneAndStoragePermission(Context context, Action<List<String>> granted) {
        AndPermission
                .with(context)
                .runtime()
                //请求码
//                .requestCode(PHONE_REQUEST_CODE)
                //单个权限
//                .permission(Manifest.permission.READ_CONTACTS)
                //多权限，用数组形式
                .permission(Permission.Group.STORAGE, Permission.Group.MICROPHONE)
                .onGranted(granted)
                .start();
    }
    /**
     * 请求获取录音及sd卡相关权限
     *
     * @param context 上下文
     */
    public static void requestStoragePermission(Context context, Action<List<String>> granted) {
        AndPermission
                .with(context)
                .runtime()
                //请求码
//                .requestCode(PHONE_REQUEST_CODE)
                //单个权限
//                .permission(Manifest.permission.READ_CONTACTS)
                //多权限，用数组形式
                .permission(Permission.Group.STORAGE)
                .onGranted(granted)
                .start();
    }


    /**
     * 判断录音及sd卡相关权限是否已获取
     *
     * @param mContext
     * @return
     */
    public static boolean checkSelfPermissionMicrophoneAndStorage(Context mContext) {
        return checkSelfPermissionMicrophone(mContext)
                && checkSelfPermissionStorage(mContext);
    }


    /**
     * 判断是否获取录音权限
     *
     * @param mContext
     * @return
     */
    public static boolean checkSelfPermissionMicrophone(Context mContext) {
        return checkSelfPermission(mContext,Permission.RECORD_AUDIO);
    }


    /**
     * 判断是否获取通话记录读取及写入权限
     *
     * @param mContext
     * @return
     */
    public static boolean checkSelfPermissionStorage(Context mContext) {
        return checkSelfPermission(mContext,Permission.READ_EXTERNAL_STORAGE)
                 && checkSelfPermission(mContext, Permission.WRITE_EXTERNAL_STORAGE);
    }




    /**
     * 判断该权限是否以获取
     *
     * @param mContext   上下文
     * @param permission 权限
     */
    private static boolean checkSelfPermission(Context mContext, String permission) {
        try {
            int permissionCheck = ContextCompat.checkSelfPermission(mContext, permission);
            return permissionCheck == PackageManager.PERMISSION_GRANTED;
        } catch (Exception e) {
            if (Build.VERSION.SDK_INT >= 23) {
                return false;
            } else {
                return true;
            }
        }
    }



}
