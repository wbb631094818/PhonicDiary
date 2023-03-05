package com.js.phonicdiary.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.js.phonicdiary.R;
import com.js.phonicdiary.adapter.DiaryAdapter;
import com.js.phonicdiary.bean.DiaryInfo;
import com.js.phonicdiary.manager.CalendarManager;
import com.js.phonicdiary.utils.DateUtil;
import com.js.phonicdiary.utils.LogE;
import com.js.phonicdiary.utils.Utils;
import com.shizhefei.fragment.LazyFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 日历界面
 *
 * @author 兵兵
 */
public class CalendarFragment extends LazyFragment implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener,
        View.OnClickListener {

    private RelativeLayout mRlTool;
    private TextView mTvMonthDay;
    private TextView mTvYear;
    private TextView mTvLunar;
    private FrameLayout mFlCurrent;
    private ImageView mIbCalendar;
    private TextView mTvCurrentDay;
    private CalendarLayout mCalendarCalendarLayout;
    public CalendarView mCalendarCalendarView;
    private RecyclerView mCalendarRecyclerView;

    private int mYear;
    private DiaryAdapter diaryAdapter;

    private Activity activity;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_calendar);
        initView();
        initData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    private void initView() {

        mRlTool = (RelativeLayout) findViewById(R.id.rl_tool);
        mTvMonthDay = (TextView) findViewById(R.id.tv_month_day);
        mTvYear = (TextView) findViewById(R.id.tv_year);
        mTvLunar = (TextView) findViewById(R.id.tv_lunar);
        mFlCurrent = (FrameLayout) findViewById(R.id.fl_current);
        mIbCalendar = (ImageView) findViewById(R.id.ib_calendar);
        mTvCurrentDay = (TextView) findViewById(R.id.tv_current_day);
        mCalendarCalendarLayout = (CalendarLayout) findViewById(R.id.calendar_calendar_layout);
        mCalendarCalendarView = (CalendarView) findViewById(R.id.calendar_calendarView);
        mCalendarRecyclerView = (RecyclerView) findViewById(R.id.calendar_recyclerView);

        mTvMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarCalendarLayout.isExpand()) {
                    mCalendarCalendarLayout.expand();
                    return;
                }
                mCalendarCalendarView.showYearSelectLayout(mYear);
                mTvLunar.setVisibility(View.GONE);
                mTvYear.setVisibility(View.GONE);
                mTvMonthDay.setText(String.valueOf(mYear));
            }
        });
        findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarCalendarView.scrollToCurrent();
            }
        });

        mCalendarCalendarView.setOnYearChangeListener(this);
        mCalendarCalendarView.setOnCalendarSelectListener(this);

        mCalendarRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCalendarRecyclerView.setItemAnimator(new DefaultItemAnimator());

        diaryAdapter = new DiaryAdapter(getActivity(), mCalendarRecyclerView);
        mCalendarRecyclerView.setAdapter(diaryAdapter);

        mFlCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarCalendarView.scrollToCurrent();
            }
        });


    }

    protected void initData() {
        mTvYear.setText(String.valueOf(mCalendarCalendarView.getCurYear()));
        mYear = mCalendarCalendarView.getCurYear();
        mTvMonthDay.setText(DateUtil.getFormatData(System.currentTimeMillis()));
        if (Utils.isZh(activity)) {
            mTvLunar.setText("今日");
        } else {
            mTvLunar.setText(activity.getString(R.string.today));
        }
        mTvCurrentDay.setText(String.valueOf(mCalendarCalendarView.getCurDay()));


        CalendarManager.query(new Observer<Map<String, Calendar>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Map<String, Calendar> stringCalendarMap) {
                //此方法在巨大的数据量上不影响遍历性能，推荐使用
                mCalendarCalendarView.setSchemeDate(stringCalendarMap);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        CalendarManager.query(DateUtil.getLongData(mCalendarCalendarView.getCurYear(), mCalendarCalendarView.getCurMonth(), mCalendarCalendarView.getCurDay()), new Observer<List<DiaryInfo>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<DiaryInfo> diaryInfos) {
                if (diaryInfos != null && diaryInfos.size() > 0) {
                    DiaryInfo diaryInfo = new DiaryInfo();
                    diaryInfo.setLayoutType(DiaryInfo.TOP_LAYOUT);
                    diaryInfos.add(0,diaryInfo);
                    diaryAdapter.addDataList((ArrayList<DiaryInfo>) diaryInfos, true);
                    diaryAdapter.notifyDataSetChanged();
                } else {
                    diaryAdapter.addDataList(new ArrayList<>(), true);
                    diaryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable e) {
                diaryAdapter.addDataList(new ArrayList<>(), true);
                diaryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onComplete() {

            }
        });


    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTvLunar.setVisibility(View.VISIBLE);
        mTvYear.setVisibility(View.VISIBLE);
        if (LogE.isLog) {
            LogE.e("wbb", "dd: " + DateUtil.getFormatData(calendar.getTimeInMillis()));
        }
        mTvMonthDay.setText(DateUtil.getFormatData(calendar.getTimeInMillis()));
        mTvYear.setText(String.valueOf(calendar.getYear()));
        if (Utils.isZh(activity)) {
            mTvLunar.setText(calendar.getLunar());
        } else {
            mTvLunar.setText(DateUtil.getDataForWeek(calendar.getTimeInMillis()));
        }
        mYear = calendar.getYear();
        CalendarManager.query(calendar.getTimeInMillis(), new Observer<List<DiaryInfo>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<DiaryInfo> diaryInfos) {
                if (diaryInfos != null && diaryInfos.size() > 0) {
                    DiaryInfo diaryInfo = new DiaryInfo();
                    diaryInfo.setLayoutType(DiaryInfo.TOP_LAYOUT);
                    diaryInfos.add(0,diaryInfo);
                    diaryAdapter.addDataList((ArrayList<DiaryInfo>) diaryInfos, true);
                    diaryAdapter.notifyDataSetChanged();
                } else {
                    diaryAdapter.addDataList(new ArrayList<>(), true);
                    diaryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable e) {
                diaryAdapter.addDataList(new ArrayList<>(), true);
                diaryAdapter.notifyDataSetChanged();
            }


            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void onYearChange(int year) {
        mTvMonthDay.setText(String.valueOf(year));
    }

}
