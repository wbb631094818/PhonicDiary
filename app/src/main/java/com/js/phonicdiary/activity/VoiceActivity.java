package com.js.phonicdiary.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.js.phonicdiary.R;
import com.js.phonicdiary.activity.base.BaseActivity;
import com.js.phonicdiary.adapter.VoiceAdapter;
import com.js.phonicdiary.bean.VoiceInfo;
import com.js.phonicdiary.manager.VoiceDbManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 录音管理页面
 *
 * @author 兵兵
 */
public class VoiceActivity extends BaseActivity {
    private ImageView mVoiceBlack;
    private TextView mVoiceTitle;
    private RecyclerView mVoiceRlv;
    private TextView mVoiceNull;

    // 日记创建时间
    private long createTime;
    // Rx线程
    private Disposable disposable;
    private VoiceAdapter voiceAdapter;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_voice);

        mVoiceBlack = (ImageView) findViewById(R.id.voice_black);
        mVoiceTitle = (TextView) findViewById(R.id.voice_title);
        mVoiceRlv = (RecyclerView) findViewById(R.id.voice_rlv);
        mVoiceNull = (TextView) findViewById(R.id.voice_null);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mVoiceRlv.setLayoutManager(linearLayoutManager);

        voiceAdapter = new VoiceAdapter(this,mVoiceRlv);
        mVoiceRlv.setAdapter(voiceAdapter);

    }

    @Override
    protected void initListener() {
        super.initListener();
        mVoiceBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 返回
                finish();
            }
        });
    }

    @Override
    protected void initIntent() {
        super.initIntent();
        Intent intent = getIntent();
        if (intent != null) {
            createTime = intent.getLongExtra("createTime", 0);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        if (createTime != 0) {
            VoiceDbManager.query(createTime, new Observer<List<VoiceInfo>>() {
                @Override
                public void onSubscribe(Disposable d) {
                    disposable = d;
                }

                @Override
                public void onNext(List<VoiceInfo> voiceInfos) {
                    if (voiceInfos != null && voiceInfos.size() > 0) {
                        voiceAdapter.addDataList((ArrayList<VoiceInfo>) voiceInfos,true);
                        voiceAdapter.notifyDataSetChanged();
                        mVoiceNull.setVisibility(View.GONE);
                    }else {
                        // 空数据页面
                        voiceAdapter.addDataList(new ArrayList<VoiceInfo>(),true);
                        voiceAdapter.notifyDataSetChanged();
                        mVoiceNull.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
