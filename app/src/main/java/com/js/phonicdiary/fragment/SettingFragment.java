package com.js.phonicdiary.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bilibili.magicasakura.widgets.TintTextView;
import com.js.phonicdiary.R;
import com.js.phonicdiary.activity.AboutSettingActivity;
import com.js.phonicdiary.activity.backup.BackupActivity;
import com.js.phonicdiary.activity.PageSettingActivity;
import com.js.phonicdiary.activity.lock.LockSettingActivity;
import com.js.phonicdiary.utils.FileUtils;
import com.shizhefei.fragment.LazyFragment;

public class SettingFragment extends LazyFragment {

    private TintTextView mSettingPassword;
    private TintTextView mSettingTheme;
    private TintTextView mSettingBackup;
    private TintTextView mSettingEdit;
    private TintTextView mSettingInfo;
    private TintTextView mSettingVoicePath;

    private Activity activity;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        initView();
        initListener();
        initData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    private void initView(){
        setContentView(R.layout.fragment_setting);

        mSettingPassword = (TintTextView) findViewById(R.id.setting_password);
        mSettingTheme = (TintTextView) findViewById(R.id.setting_theme);
        mSettingBackup = (TintTextView) findViewById(R.id.setting_backup);
        mSettingEdit = (TintTextView) findViewById(R.id.setting_edit);
        mSettingInfo = (TintTextView) findViewById(R.id.setting_info);
        mSettingVoicePath = (TintTextView) findViewById(R.id.setting_voice_path);

    }

    private void initListener(){
        mSettingPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 密码管理
                Intent intent = new Intent(activity, LockSettingActivity.class);
                startActivity(intent);

            }
        });
        mSettingBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 备份与恢复
                Intent intent = new Intent(activity, BackupActivity.class);
                startActivity(intent);
            }
        });
        mSettingEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 页面设置
                Intent intent = new Intent(activity, PageSettingActivity.class);
                startActivity(intent);
            }
        });
        mSettingInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 关于设置
                Intent intent = new Intent(activity, AboutSettingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData(){
        mSettingVoicePath.setText(FileUtils.getDiaryFilePath());
    }
}
