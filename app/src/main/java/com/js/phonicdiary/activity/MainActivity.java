package com.js.phonicdiary.activity;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bilibili.magicasakura.widgets.TintImageView;
import com.js.phonicdiary.R;
import com.js.phonicdiary.activity.base.BaseActivity;
import com.js.phonicdiary.fragment.CalendarFragment;
import com.js.phonicdiary.fragment.DiaryFragment;
import com.js.phonicdiary.fragment.SettingFragment;
import com.js.phonicdiary.view.listener.OnTransitionLayoutListener;
import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.viewpager.SViewPager;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private SViewPager mMainViewpage;
    private FixedIndicatorView mMainIndicator;
    private CalendarFragment calendarFragment;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);

        mMainViewpage = (SViewPager) findViewById(R.id.main_viewpage);
        mMainIndicator = (FixedIndicatorView) findViewById(R.id.main_indicator);

        mMainIndicator.setOnTransitionListener(new OnTransitionLayoutListener()
                .setColor(ContextCompat.getColor(this, R.color.theme_color), ContextCompat.getColor(this, R.color.theme_color_tab_title)));

        IndicatorViewPager indicatorViewPager = new IndicatorViewPager(mMainIndicator, mMainViewpage);
        calendarFragment = new CalendarFragment();
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new DiaryFragment());
        fragmentList.add(calendarFragment);
        fragmentList.add(new SettingFragment());
        indicatorViewPager.setAdapter(new MyAdapter(getSupportFragmentManager(), fragmentList));
        // 禁止viewpager的滑动事件
        mMainViewpage.setCanScroll(true);
        // 设置viewpager保留界面不重新加载的页面数量
        mMainViewpage.setOffscreenPageLimit(3);

        indicatorViewPager.setOnIndicatorPageChangeListener(new IndicatorViewPager.OnIndicatorPageChangeListener() {
            @Override
            public void onIndicatorPageChange(int preItem, int currentItem) {
                if (preItem != -1) {
                    View tabItem = mMainIndicator.getItemView(preItem);
                    if (tabItem != null) {
                        TintImageView mIcon = (TintImageView) tabItem.findViewById(R.id.icon);
                        if (mIcon != null) {
                            mIcon.setImageTintList(R.color.theme_color_tab_title);
                        }
                    }
                }
                if (currentItem != -1) {
                    View currentItemView = mMainIndicator.getItemView(currentItem);
                    if (currentItemView != null) {
                        TintImageView mIcon = (TintImageView) currentItemView.findViewById(R.id.icon);
                        if (mIcon != null) {
                            mIcon.setImageTintList(R.color.theme_color);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (calendarFragment != null && calendarFragment.mCalendarCalendarView != null) {
            if (calendarFragment.mCalendarCalendarView.isYearSelectLayoutVisible()) {
                calendarFragment.mCalendarCalendarView.closeYearSelectLayout();
                return;
            }
        }
        super.onBackPressed();

    }

    private class MyAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
        private String[] tabNames = {"日记", "日历", "更多"};
        private int[] tabIcons = {R.drawable.tab_home_diary_white, R.drawable.ic_date_range_white_24dp, R.drawable.ic_settings_white_24dp};
        //        private int[] tabIcons = {R.drawable.maintab_diary_selector, R.drawable.maintab_date_range_selector, R.drawable.maintab_3_selector};
        private LayoutInflater inflater;
        private ArrayList<Fragment> fragmentList;

        public MyAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragmentList) {
            super(fragmentManager);
            inflater = LayoutInflater.from(getApplicationContext());
            this.fragmentList = fragmentList;
        }

        @Override
        public int getCount() {
            return tabNames.length;
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.tab_main, container, false);
            }
            TintImageView mIcon = (TintImageView) convertView.findViewById(R.id.icon);
            TextView mText = (TextView) convertView.findViewById(R.id.text);
            mText.setText(tabNames[position]);
            mIcon.setImageResource(tabIcons[position]);
            mIcon.setImageTintList(R.color.theme_color_tab_title);
            if (position == 0) {
                mIcon.setImageTintList(R.color.theme_color);
            }

            return convertView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {
            return fragmentList.get(position);
        }
    }

}
