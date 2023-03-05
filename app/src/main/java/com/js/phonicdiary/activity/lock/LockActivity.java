package com.js.phonicdiary.activity.lock;

import android.content.Intent;
import android.widget.Toast;

import com.js.phonicdiary.DiaryApplication;
import com.js.phonicdiary.R;
import com.js.phonicdiary.activity.MainActivity;
import com.js.phonicdiary.activity.base.BaseActivity;
import com.js.phonicdiary.utils.AppPreferences;
import com.js.phonicdiary.utils.LogE;
import com.js.phonicdiary.view.passcodeview.PasscodeView;

/**
 * 密码设置或验证页面
 *
 * @author 兵兵
 */
public class LockActivity extends BaseActivity {

    private PasscodeView mLockPasscode;
    private int passcodeType;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_lock);

        if (passcodeType == -1) {
            finish();
            return;
        }
        mLockPasscode = (PasscodeView) findViewById(R.id.lock_passcode);
        mLockPasscode
                .setPasscodeType(passcodeType)
                .setListener(new PasscodeView.PasscodeViewListener() {
                    @Override
                    public void onFail(String wrongNumber) {
//                        Toast.makeText(getApplication(), "密码错误!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String number) {
//                        Toast.makeText(getApplication(), "finish", Toast.LENGTH_SHORT).show();
                        if (passcodeType == PasscodeView.TYPE_SET_PASSCODE) {
                            // 设置密码成功
                            AppPreferences.setLocalPasscode(number);
                            Toast.makeText(DiaryApplication.getInstance(), DiaryApplication.getInstance().getString(R.string.Password_set_successfully), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // 验证密码成功
                            // 去首页
                            Intent intent = new Intent(LockActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
        String passcode = AppPreferences.getLocalPasscode();
        if (passcodeType == PasscodeView.TYPE_CHECK_PASSCODE && passcode != null && !"".equals(passcode)) {
            mLockPasscode.setLocalPasscode(passcode);
            mLockPasscode.useFingerprint();
        }

    }

    @Override
    protected void initIntent() {
        super.initIntent();
        if (getIntent() != null) {
            passcodeType = getIntent().getIntExtra("PasscodeType", -1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLockPasscode != null) {
            mLockPasscode.cancel();
        }
    }
}
