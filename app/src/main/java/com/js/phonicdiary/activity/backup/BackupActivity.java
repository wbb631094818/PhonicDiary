package com.js.phonicdiary.activity.backup;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bilibili.magicasakura.widgets.TintProgressDialog;
import com.bilibili.magicasakura.widgets.TintSwitchCompat;
import com.bilibili.magicasakura.widgets.TintTextView;
import com.js.phonicdiary.DiaryApplication;
import com.js.phonicdiary.R;
import com.js.phonicdiary.activity.base.BaseActivity;
import com.js.phonicdiary.manager.BackupManager;
import com.js.phonicdiary.utils.AppPreferences;
import com.js.phonicdiary.utils.FileUtils;
import com.js.phonicdiary.utils.LogE;
import com.js.phonicdiary.utils.PermissionUtil;
import com.js.phonicdiary.utils.Utils;
import com.yanzhenjie.permission.Action;

import java.util.List;

import cn.qqtheme.framework.picker.FilePicker;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static cn.qqtheme.framework.picker.FilePicker.FILE;

/**
 * 备份页面
 *
 * @author 兵兵
 */
public class BackupActivity extends BaseActivity {

    private ImageView mBackupBlack;
    private TintTextView mBackupTitle;
    private FrameLayout mBackupAutoClick;
    private TintSwitchCompat mBackupAutoSwitch;
    private FrameLayout mBackupDataClick;
    private FrameLayout mBackupRestoringClick;
    private TintTextView mBackupPath;
    private TintTextView mBackupFile;

    private Disposable disposable;
    private TintProgressDialog progressDialog;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_backup);

        mBackupBlack = (ImageView) findViewById(R.id.backup_black);
        mBackupTitle = (TintTextView) findViewById(R.id.backup_title);
        mBackupAutoClick = (FrameLayout) findViewById(R.id.backup_auto_click);
        mBackupAutoSwitch = (TintSwitchCompat) findViewById(R.id.backup_auto_switch);
        mBackupDataClick = (FrameLayout) findViewById(R.id.backup_data_click);
        mBackupRestoringClick = (FrameLayout) findViewById(R.id.backup_restoring_click);
        mBackupPath = (TintTextView) findViewById(R.id.backup_path);
        mBackupFile = (TintTextView) findViewById(R.id.backup_file);


    }


    @Override
    protected void initData() {
        super.initData();
        // 自动备份
        mBackupAutoSwitch.setChecked(AppPreferences.isOpenAutoBackup());
        mBackupPath.setText(FileUtils.getBackupFilePath());
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBackupBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mBackupAutoClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionUtil.checkSelfPermissionStorage(BackupActivity.this)) {
                    // 自动备份
                    if (mBackupAutoSwitch.isChecked()) {
                        mBackupAutoSwitch.setChecked(false);
                        AppPreferences.setOpenAutoBackup(false);
                    } else {
                        mBackupAutoSwitch.setChecked(true);
                        AppPreferences.setOpenAutoBackup(true);
                    }
                } else {
                    PermissionUtil.requestStoragePermission(BackupActivity.this, new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            // 自动备份
                            if (mBackupAutoSwitch.isChecked()) {
                                mBackupAutoSwitch.setChecked(false);
                                AppPreferences.setOpenAutoBackup(false);
                            } else {
                                mBackupAutoSwitch.setChecked(true);
                                AppPreferences.setOpenAutoBackup(true);
                            }
                        }
                    });
                }

            }
        });
        mBackupDataClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionUtil.checkSelfPermissionStorage(BackupActivity.this)) {
                    backupClick();
                } else {
                    PermissionUtil.requestStoragePermission(BackupActivity.this, new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            backupClick();
                        }
                    });
                }

            }
        });
        mBackupRestoringClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionUtil.checkSelfPermissionStorage(BackupActivity.this)) {
//                    Utils.getZipFile(BackupActivity.this);
                    FilePicker filePicker = new FilePicker(BackupActivity.this, FILE);
                    filePicker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
                        @Override
                        public void onFilePicked(String currentPath) {
                            restoreBackup(currentPath);
                        }
                    });
                    filePicker.show();
                } else {
                    PermissionUtil.requestStoragePermission(BackupActivity.this, new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            FilePicker filePicker = new FilePicker(BackupActivity.this, FILE);
                            filePicker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
                                @Override
                                public void onFilePicked(String currentPath) {
                                    restoreBackup(currentPath);
                                }
                            });
                            filePicker.show();
                        }
                    });
                }

            }
        });

        mBackupFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 去所有备份的文件
                startActivity(new Intent(BackupActivity.this, BackupFileActivity.class));
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


    /**
     * 备份文件
     */
    private void backupClick() {
        if (progressDialog == null) {
            progressDialog = new TintProgressDialog(BackupActivity.this);
            progressDialog.setMessage(DiaryApplication.getInstance().getString(R.string.backuping));
        }
        progressDialog.show();
        // 点击备份
        BackupManager.backupData(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(String s) {

            }

            @Override
            public void onError(Throwable e) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Toast.makeText(DiaryApplication.getInstance(), DiaryApplication.getInstance().getText(R.string.Failure), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Toast.makeText(DiaryApplication.getInstance(), DiaryApplication.getInstance().getText(R.string.Data_backup_completed), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     *  恢复备份
     * @param path 文件路径
     */
    private void restoreBackup (String path){
        if (path == null) {
            Toast.makeText(DiaryApplication.getInstance(), DiaryApplication.getInstance().getString(R.string.no_zip_file), Toast.LENGTH_SHORT).show();
            return;
        }
        if (progressDialog == null) {
            progressDialog = new TintProgressDialog(BackupActivity.this);
            progressDialog.setMessage(DiaryApplication.getInstance().getString(R.string.Loading));
        }
        progressDialog.show();
        BackupManager.recoverBackupData(path, new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(String s) {

            }

            @Override
            public void onError(Throwable e) {
                if (e != null) {
                    Toast.makeText(DiaryApplication.getInstance(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onComplete() {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Toast.makeText(DiaryApplication.getInstance(), DiaryApplication.getInstance().getString(R.string.successfully), Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BackupManager.fileMove();
                    }
                }, 800);
            }
        });
    }
}
