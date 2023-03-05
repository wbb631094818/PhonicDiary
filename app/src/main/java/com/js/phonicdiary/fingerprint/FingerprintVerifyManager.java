package com.js.phonicdiary.fingerprint;

import android.os.Build;

import com.js.phonicdiary.DiaryApplication;
import com.js.phonicdiary.fingerprint.uitls.AndrVersionUtil;


/**
 * Created by ZuoHailong on 2019/7/9.
 */
public class FingerprintVerifyManager {

    private FingerprintImplForAndrM fingerprint;

    /**
     *  开始验证
     * @param callback
     */
    public void startVerify(FingerprintCallback callback) {
        if (AndrVersionUtil.isAboveAndrP()) {
            fingerprint = FingerprintImplForAndrM.newInstance();
        } else if (AndrVersionUtil.isAboveAndrM()) {
            fingerprint = FingerprintImplForAndrM.newInstance();
        } else {//Android 6.0 以下官方未开放指纹识别，某些机型自行支持的情况暂不做处理
            callback.onHwUnavailable();
            return;
        }
        //检测指纹硬件是否存在或者是否可用，若false，不再弹出指纹验证框
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!fingerprint.canAuthenticate(DiaryApplication.getInstance(), callback)) {
                return;
            }
        }
        /**
         * 设定指纹验证框的样式
         */
        // >= Android 6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprint.authenticate(DiaryApplication.getInstance(), callback);
        }
    }



    /**
     * 取消扫描
     */
    public void cancel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprint.cancel();
        }
    }
}
