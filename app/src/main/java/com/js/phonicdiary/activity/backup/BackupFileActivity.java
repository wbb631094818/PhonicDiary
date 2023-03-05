package com.js.phonicdiary.activity.backup;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bilibili.magicasakura.widgets.TintTextView;
import com.google.gson.Gson;
import com.js.phonicdiary.DiaryApplication;
import com.js.phonicdiary.R;
import com.js.phonicdiary.activity.base.BaseActivity;
import com.js.phonicdiary.adapter.BackupFileAdapter;
import com.js.phonicdiary.bean.DiaryInfo;
import com.js.phonicdiary.bean.FileInfo;
import com.js.phonicdiary.bean.PhotoInfo;
import com.js.phonicdiary.bean.VoiceInfo;
import com.js.phonicdiary.utils.DateUtil;
import com.js.phonicdiary.utils.FileUtils;
import com.js.phonicdiary.utils.LogE;
import com.js.phonicdiary.utils.PermissionUtil;
import com.yanzhenjie.permission.Action;

import net.lingala.zip4j.ZipFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 本地备份文件展示界面
 *
 * @author 兵兵
 */
public class BackupFileActivity extends BaseActivity {

    private ImageView mBackupFileBlack;
    private TintTextView mBackupFileTitle;
    private RecyclerView mBackupFileRlv;
    private BackupFileAdapter backupFileAdapter;
    private Disposable disposable;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_backup_file);

        mBackupFileBlack = (ImageView) findViewById(R.id.backup_file_black);
        mBackupFileTitle = (TintTextView) findViewById(R.id.backup_file_title);
        mBackupFileRlv = (RecyclerView) findViewById(R.id.backup_file_rlv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mBackupFileRlv.setLayoutManager(linearLayoutManager);

        backupFileAdapter = new BackupFileAdapter(this);
        mBackupFileRlv.setAdapter(backupFileAdapter);

    }

    @Override
    protected void initData() {
        super.initData();
        if (PermissionUtil.checkSelfPermissionStorage(this)){
            loadData();
        }else {
            PermissionUtil.requestStoragePermission(this, new Action<List<String>>() {
                @Override
                public void onAction(List<String> data) {
                    loadData();
                }
            });
        }
    }

    private void loadData(){
        Observable.create(new ObservableOnSubscribe<ArrayList<FileInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<FileInfo>> emitter) throws Exception {
                emitter.onNext((ArrayList<FileInfo>) FileUtils.getAllFiles(FileUtils.getBackupFilePath(), "zip"));
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<FileInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(ArrayList<FileInfo> fileInfos) {
                        if (fileInfos != null && fileInfos.size() > 0) {
                            backupFileAdapter.addDataList(fileInfos, true);
                            backupFileAdapter.notifyDataSetChanged();

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

    @Override
    protected void initListener() {
        super.initListener();
        mBackupFileBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
        if (backupFileAdapter != null) {
            backupFileAdapter.dispose();
        }
    }
}
