package com.js.phonicdiary.fingerprint;

import android.content.Context;

/**
 * Created by ZuoHailong on 2019/7/9.
 */
public interface IFingerprint {

    /**
     * 检测指纹硬件是否可用，及是否添加指纹
     *
     * @param context
     * @param callback
     * @return
     */
    boolean canAuthenticate(Context context, FingerprintCallback callback);

    /**
     * 初始化并调起指纹验证
     *
     * @param context
     * @param callback
     */
    void authenticate(Context context, FingerprintCallback callback);

}
