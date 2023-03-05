package com.js.phonicdiary.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bilibili.magicasakura.widgets.TintTextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.View;

import com.js.phonicdiary.R;
import com.js.phonicdiary.activity.EditActivity;
import com.js.phonicdiary.activity.MainActivity;
import com.js.phonicdiary.activity.SearchActivity;
import com.js.phonicdiary.adapter.DiaryAdapter;
import com.js.phonicdiary.bean.DiaryInfo;
import com.js.phonicdiary.manager.DiaryDbManager;
import com.shizhefei.fragment.LazyFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 日记fragment，懒加载
 * Created by 王兵兵 on 2019/1/22.
 */
public class DiaryFragment extends LazyFragment {

    private MainActivity activity;

    private RecyclerView mDiaryRlv;
    private FloatingActionButton mDiaryFb;
    private SwipeRefreshLayout mDiaryRefresh;
    private CardView mDiarySearch;
    private TintTextView mDiaryNull;
    private DiaryAdapter diaryAdapter;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_diary);
        initView();
        initListener();
        initData();
    }

    /**
     * 初始化view
     */
    private void initView() {
        mDiaryRlv = (RecyclerView) findViewById(R.id.diary_rlv);
        mDiaryFb = (FloatingActionButton) findViewById(R.id.diary_fb);
        mDiaryRefresh = (SwipeRefreshLayout) findViewById(R.id.diary_refresh);
        mDiarySearch = (CardView) findViewById(R.id.diary_search);
        mDiaryNull = (TintTextView) findViewById(R.id.diary_null);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        mDiaryRlv.setLayoutManager(linearLayoutManager);
        mDiaryRlv.setItemAnimator(new DefaultItemAnimator());

        diaryAdapter = new DiaryAdapter(activity, mDiaryRlv);
        mDiaryRlv.setAdapter(diaryAdapter);

    }

    /**
     * 设置监听
     */
    private void initListener() {
        mDiaryFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 添加及编辑日记
                Intent intent = new Intent(activity, EditActivity.class);
                startActivity(intent);
            }
        });
        mDiaryRlv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0) {
                    // 上滑
                    mDiaryFb.show();
                } else if (dy > 0) {
                    // 下滑
                    mDiaryFb.hide();
                }
            }
        });

        mDiaryRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        mDiarySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, SearchActivity.class));
            }
        });
    }

    /**
     * 初始话数据
     */
    private void initData() {
        DiaryDbManager.query(new Observer<List<DiaryInfo>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<DiaryInfo> diaryInfos) {
                if (diaryInfos != null && diaryInfos.size() > 0) {
                    diaryAdapter.addDataList((ArrayList<DiaryInfo>) diaryInfos, true);
                    diaryAdapter.notifyDataSetChanged();
                    mDiaryNull.setVisibility(View.GONE);
                }else {
                    diaryAdapter.addDataList(new ArrayList<>(), true);
                    diaryAdapter.notifyDataSetChanged();
                    mDiaryNull.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                mDiaryRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }
}
