package com.js.phonicdiary.fingerprint;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;

import com.js.phonicdiary.fingerprint.uitls.CipherHelper;

/**
 * Android M == 6.0
 * Created by ZuoHailong on 2019/7/9.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintImplForAndrM implements IFingerprint {

    private final String TAG = FingerprintImplForAndrM.class.getName();

    private static FingerprintImplForAndrM fingerprintImplForAndrM;
    //指向调用者的指纹回调
    private FingerprintCallback fingerprintCallback;

    //用于取消扫描器的扫描动作
    private CancellationSignal cancellationSignal;
    //指纹加密
    private static FingerprintManagerCompat.CryptoObject cryptoObject;

    @Override
    public void authenticate(Context context, FingerprintCallback callback) {
        this.fingerprintCallback = callback;
        //Android 6.0 指纹管理 实例化
        /**
         * Android 6.0 指纹管理
         */
        FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(context);

        //取消扫描，每次取消后需要重新创建新示例
        cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
                // 取消扫描监听
            }
        });

        //调起指纹验证
        fingerprintManagerCompat.authenticate(cryptoObject, 0, cancellationSignal, authenticationCallback, null);
    }

    public static FingerprintImplForAndrM newInstance() {
        if (fingerprintImplForAndrM == null) {
            synchronized (FingerprintImplForAndrM.class) {
                if (fingerprintImplForAndrM == null) {
                    fingerprintImplForAndrM = new FingerprintImplForAndrM();
                }
            }
        }
        //指纹加密，提前进行Cipher初始化，防止指纹认证时还没有初始化完成
        try {
            cryptoObject = new FingerprintManagerCompat.CryptoObject(new CipherHelper().createCipher());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fingerprintImplForAndrM;
    }


    /**
     * 取消扫描
     */
    public void cancel() {
        if (cancellationSignal != null && !cancellationSignal.isCanceled()) {
            cancellationSignal.cancel();
        }
    }

    /**
     * 指纹验证结果回调
     */
    private FingerprintManagerCompat.AuthenticationCallback authenticationCallback = new FingerprintManagerCompat.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            super.onAuthenticationError(errMsgId, errString);
            if (errMsgId != 5) {
                //用户取消指纹验证
                if (fingerprintCallback!=null) {
                    fingerprintCallback.onCancel();
                }
            }
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            super.onAuthenticationHelp(helpMsgId, helpString);
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            if (fingerprintCallback!=null) {
                fingerprintCallback.onSucceeded();
            }
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            if (fingerprintCallback!=null) {
                fingerprintCallback.onFailed();
            }
        }
    };

    /*
     * 在 Android Q，Google 提供了 Api BiometricManager.canAuthenticate() 用来检测指纹识别硬件是否可用及是否添加指纹
     * 不过尚未开放，标记为"Stub"(存根)
     * 所以暂时还是需要使用 Andorid 6.0 的 Api 进行判断
     * */
    @Override
    public boolean canAuthenticate(Context context, FingerprintCallback fingerprintCallback) {
        /*
         * 硬件是否支持指纹识别
         * */
        if (!FingerprintManagerCompat.from(context).isHardwareDetected()) {
            if (fingerprintCallback!=null) {
                fingerprintCallback.onHwUnavailable();
            }
            return false;
        }
        //是否已添加指纹
        if (!FingerprintManagerCompat.from(context).hasEnrolledFingerprints()) {
            if (fingerprintCallback!=null) {
                fingerprintCallback.onNoneEnrolled();
            }
            return false;
        }
        return true;
    }

}
