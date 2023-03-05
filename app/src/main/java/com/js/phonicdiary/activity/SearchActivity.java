package com.js.phonicdiary.activity;

import android.content.Context;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bilibili.magicasakura.widgets.TintProgressBar;
import com.bilibili.magicasakura.widgets.TintTextView;
import com.js.phonicdiary.R;
import com.js.phonicdiary.activity.base.BaseActivity;
import com.js.phonicdiary.adapter.DiaryAdapter;
import com.js.phonicdiary.bean.DiaryInfo;
import com.js.phonicdiary.manager.SearchManager;
import com.js.phonicdiary.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 搜索页面
 *
 * @author 兵兵
 */
public class SearchActivity extends BaseActivity {

    private ImageView mSearchBack;
    private EditText mSearchEt;
    private RecyclerView mSearchRlv;
    private TintProgressBar mSearchProgress;
    private TintTextView mSearchNull;

    private DiaryAdapter diaryAdapter;
    private Disposable disposable;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (StatusBarUtil.isLightColor(Th))
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        mSearchBack = (ImageView) findViewById(R.id.search_back);
        mSearchEt = (EditText) findViewById(R.id.search_et);
        mSearchRlv = (RecyclerView) findViewById(R.id.search_rlv);
        mSearchProgress = (TintProgressBar) findViewById(R.id.search_progress);
        mSearchNull = (TintTextView) findViewById(R.id.search_null);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        mSearchRlv.setLayoutManager(linearLayoutManager);
        mSearchRlv.setItemAnimator(new DefaultItemAnimator());

        diaryAdapter = new DiaryAdapter(this, mSearchRlv);
        mSearchRlv.setAdapter(diaryAdapter);

        mSearchEt.requestFocus();

    }

    @Override
    protected boolean isStatusBarDark() {
        return false;
    }

    @Override
    protected void initListener() {
        super.initListener();
        mSearchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    if (!"".equals(mSearchEt.getText().toString())) {
                        // 展示搜索结果
                        // 去搜索
                        searchData();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 搜索
     */
    private void searchData() {
        mSearchProgress.setVisibility(View.VISIBLE);
        mSearchNull.setVisibility(View.GONE);
        SearchManager.query(mSearchEt.getText().toString(), new Observer<List<DiaryInfo>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(List<DiaryInfo> diaryInfos) {
                if (diaryInfos != null && diaryInfos.size() > 0) {
                    diaryAdapter.addDataList((ArrayList<DiaryInfo>) diaryInfos, true);
                    diaryAdapter.notifyDataSetChanged();
                } else {
                    diaryAdapter.addDataList(new ArrayList<>(), true);
                    diaryAdapter.notifyDataSetChanged();
                    mSearchNull.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(Throwable e) {
                mSearchProgress.setVisibility(View.GONE);
                diaryAdapter.addDataList(new ArrayList<>(), true);
                diaryAdapter.notifyDataSetChanged();
                mSearchNull.setVisibility(View.VISIBLE);
            }

            @Override
            public void onComplete() {
                mSearchProgress.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
