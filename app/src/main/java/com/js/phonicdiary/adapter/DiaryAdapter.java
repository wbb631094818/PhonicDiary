package com.js.phonicdiary.adapter;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bilibili.magicasakura.widgets.TintTextView;
import com.bumptech.glide.Glide;
import com.js.phonicdiary.R;
import com.js.phonicdiary.activity.EditActivity;
import com.js.phonicdiary.activity.VoiceActivity;
import com.js.phonicdiary.adapter.baseadapter.BaseRecyleAdapter;
import com.js.phonicdiary.bean.DiaryInfo;
import com.js.phonicdiary.bean.PhotoInfo;
import com.js.phonicdiary.manager.DiaryDbManager;
import com.js.phonicdiary.manager.PhotoDbManager;
import com.js.phonicdiary.utils.DateUtil;
import com.js.phonicdiary.utils.LogE;
import com.wanglu.photoviewerlibrary.OnLongClickListener;
import com.wanglu.photoviewerlibrary.PhotoViewer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 首页日记适配器
 */
public class DiaryAdapter extends BaseRecyleAdapter<DiaryInfo> {

    private AppCompatActivity activity;
    private RecyclerView recyclerView;

    public DiaryAdapter(Context context, RecyclerView recyclerView) {
        super(context);
        this.activity = (AppCompatActivity) context;
        this.recyclerView = recyclerView;
    }

    @Override
    public int getItemViewType(int position) {
        DiaryInfo diaryInfo = list.get(position);
        return diaryInfo.getLayoutType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == DiaryInfo.TITLE_LAYOUT) {
            return new TitleHolder(inflater.inflate(R.layout.item_diary_title, parent, false));
        } else if (viewType == DiaryInfo.TOP_LAYOUT) {
            return new TopHolder(inflater.inflate(R.layout.item_diary_top, parent, false));
        }
        return new DiaryHolder(inflater.inflate(R.layout.item_diary, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == DiaryInfo.TITLE_LAYOUT) {
            // 标题界面
            TitleHolder titleHolder = (TitleHolder) holder;
            if (titleHolder != null) {
                DiaryInfo diaryInfo = list.get(position);
                titleHolder.mDiaryTitleText.setText(diaryInfo.getTitle());
            }
        } else if (itemViewType == DiaryInfo.TOP_LAYOUT) {
            // 头部界面
        } else {
            // 日记正文界面
            DiaryHolder diaryHolder = (DiaryHolder) holder;
            if (diaryHolder != null) {
                final DiaryInfo diaryInfo = list.get(position);
                diaryHolder.mItemDiaryWeek.setText(DateUtil.getDataForWeek(diaryInfo.getCreateTime()));
                diaryHolder.mItemDiaryDay.setText(DateUtil.getDataDay(diaryInfo.getCreateTime()));
                diaryHolder.mItemDiaryTime.setText(DateUtil.getFormatTime(new Date(diaryInfo.getCreateTime())));
                if (diaryInfo.getTitle() != null && !"".equals(diaryInfo.getTitle())) {
                    diaryHolder.mItemDiaryTitle.setText(diaryInfo.getTitle());
                    diaryHolder.mItemDiaryTitle.setVisibility(View.VISIBLE);
                } else {
                    diaryHolder.mItemDiaryTitle.setVisibility(View.GONE);
                }
                if (diaryInfo.getText() != null && !"".equals(diaryInfo.getText())) {
                    diaryHolder.mItemDiaryMessage.setText(diaryInfo.getText().replace("\t", ""));
                }
                if (diaryInfo.getPhotoInfos() != null && diaryInfo.getPhotoInfos().size() > 0) {
                    diaryHolder.mItemDiaryImageLabel.setText(diaryInfo.getPhotoInfos().size() + "");
                    diaryHolder.mItemDiaryImageLabel.setVisibility(View.VISIBLE);
                    Glide.with(activity).load(diaryInfo.getPhotoInfos().get(0).getPath()).centerCrop().into(diaryHolder.mItemDiaryImage);
                    diaryHolder.mItemDiaryImage.setVisibility(View.VISIBLE);
                } else if (diaryInfo.getVoiceInfos() != null && diaryInfo.getVoiceInfos().size() > 0) {
                    diaryHolder.mItemDiaryImage.setImageResource(R.drawable.ic_voice_red_24dp);
                    diaryHolder.mItemDiaryImage.setVisibility(View.VISIBLE);
                    diaryHolder.mItemDiaryImageLabel.setVisibility(View.GONE);
                } else {
                    diaryHolder.mItemDiaryImageLabel.setVisibility(View.GONE);
                    diaryHolder.mItemDiaryImage.setVisibility(View.GONE);
                }
                if (diaryInfo.getVoiceInfos() != null && diaryInfo.getVoiceInfos().size() > 0) {
                    diaryHolder.mItemDiaryVoiceLabel.setText(diaryInfo.getVoiceInfos().size() + "");
                    diaryHolder.mItemDiaryVoiceLabel.setVisibility(View.VISIBLE);
                } else {
                    diaryHolder.mItemDiaryVoiceLabel.setVisibility(View.GONE);
                }

                diaryHolder.mItemDiaryClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 去详情页
                        Intent intent = new Intent(activity, EditActivity.class);
                        intent.putExtra("DiaryInfo", diaryInfo);
                        activity.startActivity(intent);
                    }
                });
                diaryHolder.mItemDiaryClick.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        new AlertDialog.Builder(activity)
                                .setTitle(R.string.delete)
                                .setMessage("是否删除该日记？")
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // 取消
                                    }
                                }).setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 数据库中删除
                                DiaryDbManager.delete(diaryInfo, new Observer<String>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(String s) {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if (activity != null) {
                                            Toast.makeText(activity, activity.getString(R.string.successfully), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onComplete() {
                                        if (position != -1 && list.size() > position) {
                                            list.remove(position);
                                            notifyItemRemoved(position);
                                            notifyItemChanged(0, list.size());
                                        }
                                    }
                                });
                            }
                        }).show();
                        return true;
                    }
                });

                diaryHolder.mItemDiaryImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (diaryInfo.getPhotoInfos() != null && diaryInfo.getPhotoInfos().size() > 0) {
                            // 展示图片
                            PhotoViewer.INSTANCE
                                    .setClickSingleImg(diaryInfo.getPhotoInfos().get(0).getPath(), diaryHolder.mItemDiaryImage)
                                    .setShowImageViewInterface(new PhotoViewer.ShowImageViewInterface() {
                                        @Override
                                        public void show(ImageView iv, String url) {
                                            // 设置自己加载图片的框架来加载图片
                                            Glide.with(iv.getContext()).load(url).into(iv);
                                        }
                                    }).setOnLongClickListener(new OnLongClickListener() {
                                @Override
                                public void onLongClick(View view) {
                                    // 长按图片。编辑
                                }
                            }).start(activity);
                        } else if (diaryInfo.getVoiceInfos() != null && diaryInfo.getVoiceInfos().size() > 0) {
                            // 展示音频
                            Intent intent = new Intent(activity, VoiceActivity.class);
                            intent.putExtra("createTime", diaryInfo.getCreateTime());
                            activity.startActivity(intent);
                        }
                    }
                });
            }

        }
    }

    /**
     * 日记界面
     */
    private class DiaryHolder extends RecyclerView.ViewHolder {

        private CardView mItemDiaryClick;
        private TintTextView mItemDiaryWeek;
        private TintTextView mItemDiaryDay;
        private TintTextView mItemDiaryTime;
        private TintTextView mItemDiaryTitle;
        private TintTextView mItemDiaryMessage;
        private ImageView mItemDiaryImage;
        private TintTextView mItemDiaryImageLabel;
        private TintTextView mItemDiaryVoiceLabel;

        public DiaryHolder(View itemView) {
            super(itemView);

            mItemDiaryClick = (CardView) itemView.findViewById(R.id.item_diary_click);
            mItemDiaryWeek = (TintTextView) itemView.findViewById(R.id.item_diary_week);
            mItemDiaryDay = (TintTextView) itemView.findViewById(R.id.item_diary_day);
            mItemDiaryTime = (TintTextView) itemView.findViewById(R.id.item_diary_time);
            mItemDiaryTitle = (TintTextView) itemView.findViewById(R.id.item_diary_title);
            mItemDiaryMessage = (TintTextView) itemView.findViewById(R.id.item_diary_message);
            mItemDiaryImage = (ImageView) itemView.findViewById(R.id.item_diary_image);

            mItemDiaryImageLabel = (TintTextView) itemView.findViewById(R.id.item_diary_image_label);
            mItemDiaryVoiceLabel = (TintTextView) itemView.findViewById(R.id.item_diary_voice_label);


        }
    }

    /**
     * 标题界面
     */
    private class TitleHolder extends RecyclerView.ViewHolder {
        private TextView mDiaryTitleText;

        public TitleHolder(View itemView) {
            super(itemView);

            mDiaryTitleText = (TextView) itemView.findViewById(R.id.diary_title_text);

        }
    }

    /**
     * 头部界面
     */
    private class TopHolder extends RecyclerView.ViewHolder {

        public TopHolder(View itemView) {
            super(itemView);

        }
    }
}
