package com.js.phonicdiary.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bilibili.magicasakura.widgets.TintProgressDialog;
import com.bilibili.magicasakura.widgets.TintTextView;
import com.js.phonicdiary.DiaryApplication;
import com.js.phonicdiary.R;
import com.js.phonicdiary.adapter.baseadapter.BaseRecyleAdapter;
import com.js.phonicdiary.bean.FileInfo;
import com.js.phonicdiary.manager.BackupManager;
import com.js.phonicdiary.utils.Utils;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 备份文件适配器
 *
 * @author 兵兵
 */
public class BackupFileAdapter extends BaseRecyleAdapter<FileInfo> {

    private Activity activity;
    private TintProgressDialog progressDialog;
    private Disposable disposable;

    public BackupFileAdapter(Context context) {
        super(context);
        this.activity = (Activity) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BackupFileHolder(inflater.inflate(R.layout.item_backup_file, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final BackupFileHolder backupFileHolder = (BackupFileHolder) holder;
        if (backupFileHolder != null) {
            final FileInfo fileInfo = list.get(position);
            if (fileInfo != null) {
                backupFileHolder.mBackupFileName.setText(fileInfo.getFileName());
                backupFileHolder.mBackupFileInfo.setText(fileInfo.getFileDate() + " - " + fileInfo.getFileSize());

                backupFileHolder.mBackupFileMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 更多
                        showPopupMenu(view, position);
                    }
                });

            }
        }


    }

    private class BackupFileHolder extends RecyclerView.ViewHolder {

        private FrameLayout mBackupFileClick;
        private TintTextView mBackupFileName;
        private TintTextView mBackupFileInfo;
        private ImageView mBackupFileMore;

        public BackupFileHolder(View itemView) {
            super(itemView);

            mBackupFileClick = (FrameLayout) itemView.findViewById(R.id.backup_file_click);
            mBackupFileName = (TintTextView) itemView.findViewById(R.id.backup_file_name);
            mBackupFileInfo = (TintTextView) itemView.findViewById(R.id.backup_file_info);
            mBackupFileMore = (ImageView) itemView.findViewById(R.id.backup_file_more);


        }
    }

    /**
     * 更多
     *
     * @param view
     * @param position
     */
    private void showPopupMenu(View view, int position) {
        // 这里的view代表popupMenu需要依附的view
        PopupMenu popupMenu = new PopupMenu(activity, view);
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.popup_backup_file, popupMenu.getMenu());
        popupMenu.show();
        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 控件每一个item的点击事件
                switch (item.getItemId()) {
                    case R.id.recover:
                        // 恢复
                        if (position != -1) {
                            FileInfo fileInfo = list.get(position);
                            if (fileInfo != null) {
                                if (progressDialog == null) {
                                    progressDialog = new TintProgressDialog(activity);
                                    progressDialog.setMessage(DiaryApplication.getInstance().getString(R.string.Loading));
                                }
                                progressDialog.show();
                                BackupManager.recoverBackupData(fileInfo.getFilePath(), new Observer<String>() {
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

                        break;
                    case R.id.delete:
                        if (position != -1) {
                            FileInfo fileInfo = list.get(position);
                            if (fileInfo != null) {
                                new File(fileInfo.getFilePath()).delete();
                                list.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(0, list.size());
                            }
                        }
                        break;
                    case R.id.share:
                        if (position != -1) {
                            FileInfo fileInfo = list.get(position);
                            if (fileInfo != null) {
                                Utils.shareFile(activity, fileInfo.getFilePath());
                            }
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

    }


    public void dispose() {
        if (disposable != null) {
            disposable.dispose();

        }
    }
}
