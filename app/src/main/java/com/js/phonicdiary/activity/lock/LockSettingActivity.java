package com.js.phonicdiary.activity.lock;

import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bilibili.magicasakura.widgets.TintSwitchCompat;
import com.bilibili.magicasakura.widgets.TintTextView;
import com.js.phonicdiary.R;
import com.js.phonicdiary.activity.base.BaseActivity;
import com.js.phonicdiary.utils.AppPreferences;
import com.js.phonicdiary.view.passcodeview.PasscodeView;

public class LockSettingActivity extends BaseActivity {

    private ImageView mLockBlack;
    private TintTextView mLockTitle;
    private FrameLayout mLockOpenClick;
    private TintSwitchCompat mLockOpenSwitch;
    private FrameLayout mLockPasswordClick;
    private FrameLayout mLockFingerprintClick;
    private TintSwitchCompat mLockFingerprintSwitch;
    private FrameLayout mLockReset;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_lock_setting);

        mLockBlack = (ImageView) findViewById(R.id.lock_black);
        mLockTitle = (TintTextView) findViewById(R.id.lock_title);
        mLockOpenClick = (FrameLayout) findViewById(R.id.lock_open_click);
        mLockOpenSwitch = (TintSwitchCompat) findViewById(R.id.lock_open_switch);
        mLockPasswordClick = (FrameLayout) findViewById(R.id.lock_password_click);
        mLockFingerprintClick = (FrameLayout) findViewById(R.id.lock_fingerprint_click);
        mLockFingerprintSwitch = (TintSwitchCompat) findViewById(R.id.lock_fingerprint_switch);
        mLockReset = (FrameLayout) findViewById(R.id.lock_reset);

    }

    @Override
    protected void initData() {
        super.initData();
        mLockOpenSwitch.setChecked(AppPreferences.isOpenCodedLock());
        mLockFingerprintSwitch.setChecked(AppPreferences.isOpenFingerprintCodedLock());
    }

    @Override
    protected void initListener() {
        super.initListener();
        mLockBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mLockPasswordClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LockSettingActivity.this, LockActivity.class);
                intent.putExtra("PasscodeType", PasscodeView.TYPE_SET_PASSCODE);
                startActivity(intent);

            }
        });

        mLockOpenClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 是否打开密码锁
                if (mLockOpenSwitch.isChecked()){
                    mLockOpenSwitch.setChecked(false);
                    AppPreferences.setOpenCodedLock(false);
                }else {
                    mLockOpenSwitch.setChecked(true);
                    AppPreferences.setOpenCodedLock(true);
                }
            }
        });
        mLockFingerprintClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 是否使用指纹密码
                if (mLockFingerprintSwitch.isChecked()){
                    mLockFingerprintSwitch.setChecked(false);
                    AppPreferences.setOpenFingerprintCodedLock(false);
                }else {
                    mLockFingerprintSwitch.setChecked(true);
                    AppPreferences.setOpenFingerprintCodedLock(true);
                }
            }
        });

//        mLockReset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // 如何重置密码
//
//            }
//        });
    }
}
