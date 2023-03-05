package com.js.phonicdiary.activity;

import android.content.Intent;

import com.js.phonicdiary.activity.base.BaseActivity;
import com.js.phonicdiary.activity.lock.LockActivity;
import com.js.phonicdiary.utils.AppPreferences;
import com.js.phonicdiary.view.passcodeview.PasscodeView;

/**
 * 启动页
 * @author 兵兵
 */
public class StartActivity extends BaseActivity {
    @Override
    protected void initView() {
        if (AppPreferences.isOpenCodedLock()){
            // 去密码验证
            Intent intent = new Intent(this, LockActivity.class);
            intent.putExtra("PasscodeType", PasscodeView.TYPE_CHECK_PASSCODE);
            startActivity(intent);
        }else {
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }
}
