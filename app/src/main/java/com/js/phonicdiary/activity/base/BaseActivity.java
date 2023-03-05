package com.js.phonicdiary.activity.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.js.phonicdiary.utils.StatusBarUtil;

/**
 * activity公共父类
 *
 * @author 王兵兵
 * @date 2018/3/5
 */

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isStatusBarDark()) {
            StatusBarUtil.setImmersiveStatusBar(this);
        }
        initIntent(); // 初始化Intent数据
        initView(); // 初始化布局
        requestPermission(); // 请求权限
        initListener();
        initData(); // 初始化数据
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    protected boolean isStatusBarDark() {
        return true;
    }

    protected abstract void initView();  // 布局

    /**
     * 请求权限
     */
    protected void requestPermission() {

    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 初始化监听事件
     */
    protected void initListener() {

    }

    /**
     * 初始化Intent数据
     */
    protected void initIntent() {

    }


}
