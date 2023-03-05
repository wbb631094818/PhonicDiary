package com.js.phonicdiary.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bilibili.magicasakura.widgets.TintSwitchCompat;
import com.bilibili.magicasakura.widgets.TintTextView;
import com.js.phonicdiary.DiaryApplication;
import com.js.phonicdiary.R;
import com.js.phonicdiary.activity.base.BaseActivity;
import com.js.phonicdiary.utils.AppMarketUtils;
import com.js.phonicdiary.utils.AppPreferences;
import com.js.phonicdiary.utils.Utils;

/**
 * 关于设置
 *
 * @author 兵兵
 */
public class AboutSettingActivity extends BaseActivity {

    private ImageView mAboutBlack;
    private TintTextView mAboutTitle;
    private LinearLayout mAboutVersionClick;
    private TintTextView mAboutVersion;
    private FrameLayout mAboutShareClick;
    private TintTextView mAboutRateUsClick;
    private TintTextView mAboutHelpClick;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_about_setting);

        mAboutBlack = (ImageView) findViewById(R.id.about_black);
        mAboutTitle = (TintTextView) findViewById(R.id.about_title);
        mAboutVersionClick = (LinearLayout) findViewById(R.id.about_version_click);
        mAboutVersion = (TintTextView) findViewById(R.id.about_version);
        mAboutShareClick = (FrameLayout) findViewById(R.id.about_share_click);
        mAboutRateUsClick = (TintTextView) findViewById(R.id.about_rate_us_click);
        mAboutHelpClick = (TintTextView) findViewById(R.id.about_help_click);


    }

    @Override
    protected void initData() {
        super.initData();

        mAboutVersion.setText("V" + Utils.getAppVersionName(this));
    }

    @Override
    protected void initListener() {
        super.initListener();
        mAboutBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mAboutShareClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 分享
                Utils.share(AboutSettingActivity.this,"分享有声日记...");
            }
        });
        mAboutRateUsClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 评分
                AppMarketUtils.gotoMarket(AboutSettingActivity.this);
            }
        });
        mAboutHelpClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 帮助及反馈  binggo233@qq.com
                try {
                    Intent data = new Intent(Intent.ACTION_SENDTO);
                    data.setData(Uri.parse("mailto:binggo233@qq.com"));
                    data.putExtra(Intent.EXTRA_SUBJECT, DiaryApplication.getInstance().getString(R.string.help_feedback));
                    startActivity(data);
                } catch (Exception e) {
                    try {
                        String[] email = {"mailto:binggo233@qq.com"};
                        Intent intentEmail = new
                                Intent(Intent.ACTION_SEND);
                        intentEmail.setType("message/rfc822");
                        intentEmail.putExtra(Intent.EXTRA_EMAIL, email);
                        startActivity(Intent.createChooser(intentEmail, "E-mail"));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }
}
