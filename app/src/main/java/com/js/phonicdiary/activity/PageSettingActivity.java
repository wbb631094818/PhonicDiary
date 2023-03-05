package com.js.phonicdiary.activity;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bilibili.magicasakura.widgets.TintSwitchCompat;
import com.bilibili.magicasakura.widgets.TintTextView;
import com.js.phonicdiary.R;
import com.js.phonicdiary.activity.base.BaseActivity;
import com.js.phonicdiary.utils.AppPreferences;

/**
 * 页面设置
 *
 * @author 兵兵
 */
public class PageSettingActivity extends BaseActivity {

    private ImageView mPageBlack;
    private TintTextView mPageTitle;
    private FrameLayout mPageVoiceClick;
    private TintSwitchCompat mPageVoiceSwitch;
    private FrameLayout mPageTitleClick;
    private TintSwitchCompat mPageTitleSwitch;
    private FrameLayout mPageWeatherClick;
    private TintSwitchCompat mPageWeatherSwitch;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_page_setting);

        mPageBlack = (ImageView) findViewById(R.id.page_black);
        mPageTitle = (TintTextView) findViewById(R.id.page_title);
        mPageVoiceClick = (FrameLayout) findViewById(R.id.page_voice_click);
        mPageVoiceSwitch = (TintSwitchCompat) findViewById(R.id.page_voice_switch);
        mPageTitleClick = (FrameLayout) findViewById(R.id.page_title_click);
        mPageTitleSwitch = (TintSwitchCompat) findViewById(R.id.page_title_switch);
        mPageWeatherClick = (FrameLayout) findViewById(R.id.page_weather_click);
        mPageWeatherSwitch = (TintSwitchCompat) findViewById(R.id.page_weather_switch);

    }

    @Override
    protected void initData() {
        super.initData();
        mPageVoiceSwitch.setChecked(AppPreferences.isOpenAKeyRecording());
        mPageTitleSwitch.setChecked(AppPreferences.isShowDiaryTitle());
        mPageWeatherSwitch.setChecked(AppPreferences.isShowDiaryWeather());
    }

    @Override
    protected void initListener() {
        super.initListener();
        mPageBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mPageVoiceClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPageVoiceSwitch.isChecked()) {
                    mPageVoiceSwitch.setChecked(false);
                    AppPreferences.setOpenAKeyRecording(false);
                } else {
                    mPageVoiceSwitch.setChecked(true);
                    AppPreferences.setOpenAKeyRecording(true);
                }
            }
        });
        mPageTitleClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPageTitleSwitch.isChecked()) {
                    mPageTitleSwitch.setChecked(false);
                    AppPreferences.setShowDiaryTitle(false);
                } else {
                    mPageTitleSwitch.setChecked(true);
                    AppPreferences.setShowDiaryTitle(true);
                }
            }
        });
        mPageWeatherClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPageWeatherSwitch.isChecked()) {
                    mPageWeatherSwitch.setChecked(false);
                    AppPreferences.setShowDiaryWeather(false);
                } else {
                    mPageWeatherSwitch.setChecked(true);
                    AppPreferences.setShowDiaryWeather(true);
                }
            }
        });
    }
}
