package com.js.phonicdiary.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.text.format.DateUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bilibili.magicasakura.widgets.TintProgressBar;
import com.google.android.material.card.MaterialCardView;
import com.js.phonicdiary.R;
import com.js.phonicdiary.adapter.baseadapter.BaseRecyleAdapter;
import com.js.phonicdiary.bean.VoiceInfo;
import com.js.phonicdiary.manager.VoiceDbManager;
import com.js.phonicdiary.music.MusicPlayerManager;
import com.js.phonicdiary.music.MusicStatusUpdater;
import com.js.phonicdiary.utils.Utils;
import com.js.phonicdiary.voice.VoiceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import io.reactivex.observers.DefaultObserver;

/**
 * 录音文件适配器
 */
public class VoiceAdapter extends BaseRecyleAdapter<VoiceInfo> {

    private Activity activity;

    private RecyclerView recyclerView;

    public VoiceAdapter(Context context, RecyclerView recyclerView) {
        super(context);
        this.activity = (Activity) context;
        this.recyclerView = recyclerView;
        MusicPlayerManager.get().addUpdater(musicStatusUpdater);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VoiceHolder(inflater.inflate(R.layout.item_voice, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final VoiceHolder voiceHolder = (VoiceHolder) holder;
        if (voiceHolder != null) {
            final VoiceInfo voiceInfo = list.get(position);
            if (voiceInfo != null) {
                voiceHolder.mItemVoiceName.setText(voiceInfo.getFileName());
//                voiceHolder.mItemVoiceSize.setText(Formatter.formatShortFileSize(activity,voiceInfo.getFileSize()));

                final String playId = MusicPlayerManager.getPlayId(voiceInfo.getFileName(), "VoiceAdapter");

                if (MusicPlayerManager.get().isPlayingPath(playId)) {
                    // 该资源正在播放
                    MusicPlayerManager.get().timeViewUpdata(voiceHolder.mItemVoiceSize, voiceHolder.mItemVoiceProgress);
                    voiceHolder.mItemVoiceProgress.setVisibility(View.VISIBLE);
                } else {
                    voiceHolder.mItemVoiceSize.setText(Formatter.formatShortFileSize(activity, voiceInfo.getFileSize()));
                    voiceHolder.mItemVoiceProgress.setVisibility(View.GONE);
                }

                voiceHolder.mItemVoiceClick.setTag(playId);

                voiceHolder.mItemVoiceClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 播放与暂停
                        if (MusicPlayerManager.get().isPlayingPath(playId)) {
                            // 播放
                            if (MusicPlayerManager.get().isStart()) {
                                voiceHolder.mItemVoiceSize.setText(Formatter.formatShortFileSize(activity, voiceInfo.getFileSize()));
                                voiceHolder.mItemVoiceProgress.setVisibility(View.GONE);
                                MusicPlayerManager.get().pause();
                                MusicPlayerManager.get().destroyPlay();
                            } else {
                                MusicPlayerManager.get().timeViewUpdata(voiceHolder.mItemVoiceSize, voiceHolder.mItemVoiceProgress);
                                voiceHolder.mItemVoiceProgress.setProgress(0);
                                voiceHolder.mItemVoiceProgress.setVisibility(View.VISIBLE);
                                if (voiceInfo != null) {
                                    if (voiceInfo.getFilePath() != null && !"".equals(voiceInfo.getFilePath())) {
                                        MusicPlayerManager.get().playUrl(voiceInfo.getFilePath(),
                                                playId);
                                    }
                                }
                            }
                        } else {
                            MusicPlayerManager.get().timeViewUpdata(voiceHolder.mItemVoiceSize, voiceHolder.mItemVoiceProgress);
                            voiceHolder.mItemVoiceProgress.setProgress(0);
                            voiceHolder.mItemVoiceProgress.setVisibility(View.VISIBLE);
                            if (voiceInfo != null) {
                                if (voiceInfo.getFilePath() != null && !"".equals(voiceInfo.getFilePath())) {
                                    MusicPlayerManager.get().playUrl(voiceInfo.getFilePath(),
                                            playId);
                                }
                            }
                        }
                    }
                });

                voiceHolder.mItemVoiceClick.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        final ArrayList<String> list1 = new ArrayList<>();
                        list1.add(activity.getString(R.string.Delete));
                        list1.add(activity.getString(R.string.Open_with));
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity,
                                android.R.layout.simple_list_item_1, list1);
                        new AlertDialog.Builder(activity)
                                .setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which < list.size()) {
                                            String method = list1.get(which);
                                            if (activity.getString(R.string.Delete).equals(method)) {
                                                // 删除
                                                VoiceDbManager.delete(voiceInfo, new DefaultObserver<String>() {
                                                    @Override
                                                    public void onNext(String o) {

                                                    }

                                                    @Override
                                                    public void onError(Throwable e) {

                                                    }

                                                    @Override
                                                    public void onComplete() {
                                                        list.remove(position);
                                                        notifyItemRemoved(position);
                                                        notifyItemChanged(0, list.size());
                                                    }
                                                });
                                            } else {
                                                // 打开方式
                                                Utils.openWithAudio(activity,voiceInfo.getFilePath());
                                            }
                                        }
                                    }
                                })
                                .show();
                        return true;
                    }
                });
            }
        }


    }

    private class VoiceHolder extends RecyclerView.ViewHolder {
        private CardView mItemVoiceClick;
        private TintProgressBar mItemVoiceProgress;
        private TextView mItemVoiceName;
        private TextView mItemVoiceSize;

        public VoiceHolder(View itemView) {
            super(itemView);

            mItemVoiceClick = (CardView) itemView.findViewById(R.id.item_voice_click);
            mItemVoiceProgress = (TintProgressBar) itemView.findViewById(R.id.item_voice_progress);
            mItemVoiceName = (TextView) itemView.findViewById(R.id.item_voice_name);
            mItemVoiceSize = (TextView) itemView.findViewById(R.id.item_voice_size);

        }
    }


    private MusicStatusUpdater musicStatusUpdater = new MusicStatusUpdater() {

        @Override
        public void onStart(MediaPlayer mp, String playId) {
//            if (recyclerView != null && mp != null && playId != null) {
//                CardView cardView = (CardView) recyclerView.findViewWithTag(playId);
//                if (cardView != null) {
//                    TextView mRingtoneDuration = (TextView) cardView.findViewById(R.id.ringtone_duration);
//                    mRingtoneDuration.setText(simpleDateFormat.format(mp.getDuration()));
//                }
//            }

        }

        @Override
        public void onCompletion(MediaPlayer mp, String playId) {
            try {
                if (recyclerView != null && playId != null && mp != null) {
                    CardView cardView = (CardView) recyclerView.findViewWithTag(playId);
                    if (cardView != null) {
                        TextView mItemVoiceSize = (TextView) cardView.findViewById(R.id.item_voice_size);
                        ProgressBar mItemVoiceProgress = (ProgressBar) cardView.findViewById(R.id.item_voice_progress);
                        if (mItemVoiceSize != null) {
                            mItemVoiceProgress.setVisibility(View.GONE);
                            int i = recyclerView.getChildAdapterPosition(cardView);
                            if (i != -1) {
                                if (list != null && list.size() > i) {
                                    VoiceInfo voiceInfo = list.get(i);
                                    if (voiceInfo != null) {
                                        mItemVoiceSize.setText(Formatter.formatShortFileSize(activity, voiceInfo.getFileSize()));
                                    }
                                }
                            }


                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPrepared(MediaPlayer mp, String playId) {
            if (recyclerView != null && playId != null && mp != null) {
                CardView cardView = (CardView) recyclerView.findViewWithTag(playId);
                if (cardView != null) {
                    TextView mItemVoiceSize = (TextView) cardView.findViewById(R.id.item_voice_size);
                    ProgressBar mItemVoiceProgress = (ProgressBar) cardView.findViewById(R.id.item_voice_progress);
                    if (mItemVoiceSize != null) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.ENGLISH);
                        String alltime = simpleDateFormat.format(mp.getDuration());
                        mItemVoiceSize.setText("00:00/" + alltime);
                    }
                    if (mItemVoiceProgress != null) {
                        mItemVoiceProgress.setMax(mp.getDuration());
                        mItemVoiceProgress.setProgress(0);
                    }
                }
            }

        }

        @Override
        public void onBuffering(MediaPlayer mp, int percent, String playId) {

        }

        @Override
        public void onBuffered(MediaPlayer mp, int percent, String playId) {

        }

        @Override
        public void onProgressUpdate(MediaPlayer mp, int currentPosition, int duration, String playId) {
            if (recyclerView != null && playId != null) {
                CardView cardView = (CardView) recyclerView.findViewWithTag(playId);
                if (cardView != null) {
                    TextView mItemVoiceSize = (TextView) cardView.findViewById(R.id.item_voice_size);
                    ProgressBar mItemVoiceProgress = (ProgressBar) cardView.findViewById(R.id.item_voice_progress);
                    if (mItemVoiceSize != null) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.ENGLISH);
                        String runingtime = simpleDateFormat.format(currentPosition);
                        String alltime = simpleDateFormat.format(duration);
                        mItemVoiceSize.setText(runingtime + "/" + alltime);
                        mItemVoiceProgress.setProgress(currentPosition);
                    }
                }
            }

        }

        @Override
        public void onSwitchMusic(String path, String playId, String oldPalyId) {
            try {
                if (recyclerView != null && playId != null) {
                    CardView cardView = (CardView) recyclerView.findViewWithTag(playId);

                    if (cardView != null) {
                        TextView mItemVoiceSize = (TextView) cardView.findViewById(R.id.item_voice_size);
                        ProgressBar mItemVoiceProgress = (ProgressBar) cardView.findViewById(R.id.item_voice_progress);
                        if (mItemVoiceSize != null) {
                            mItemVoiceSize.setText("00:00");
                        }
                        if (mItemVoiceProgress != null) {
                            mItemVoiceProgress.setVisibility(View.VISIBLE);
                        }
                    }
                    if (oldPalyId != null) {
                        CardView oldcardView = (CardView) recyclerView.findViewWithTag(oldPalyId);
                        if (oldcardView != null) {
                            TextView mItemVoiceSize = (TextView) oldcardView.findViewById(R.id.item_voice_size);
                            ProgressBar mItemVoiceProgress = (ProgressBar) oldcardView.findViewById(R.id.item_voice_progress);
                            if (mItemVoiceSize != null) {
                                int i = recyclerView.getChildAdapterPosition(oldcardView);
                                if (i != -1) {
                                    if (list != null && list.size() > i) {
                                        VoiceInfo voiceInfo = list.get(i);
                                        if (voiceInfo != null) {
                                            mItemVoiceSize.setText(Formatter.formatShortFileSize(activity, voiceInfo.getFileSize()));
                                        }
                                    }
                                }
                            }
                            if (mItemVoiceProgress != null) {
                                mItemVoiceProgress.setVisibility(View.GONE);
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(MediaPlayer mp, int what, int extra) {
            // 播放失败
        }

    };


}
