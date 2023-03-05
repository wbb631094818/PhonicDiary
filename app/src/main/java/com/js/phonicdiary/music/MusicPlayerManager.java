package com.js.phonicdiary.music;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 音乐播放工具类
 * Created by 王兵兵 on 2018/1/16.
 */

public class MusicPlayerManager implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener, MediaPlayer.OnErrorListener {

    // 音乐回调
    private ArrayList<MusicStatusUpdater> updaterList = new ArrayList<>();

    private MediaPlayer mediaPlayer; // 媒体播放器

    private boolean isStart; // 是否开始播放

    private String path; // 当前播放地址

    private String playId; // 当前播放ID

    private boolean isShowProgress; // 是否显示进度条

    private static MusicPlayerManager musicPlayerManager = null;

    public static MusicPlayerManager get() {
        if (musicPlayerManager == null) {
            musicPlayerManager = new MusicPlayerManager();
            //  x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能
        }
        return musicPlayerManager;
    }

    // 初始化播放器
    private MusicPlayerManager() {
        super();
    }

    public void play() {
        mediaPlayer.start();
    }

    /**
     * @param url url地址
     */
    public void playUrl(final String url, final String id) {
        try {
            isShowProgress = true;
//            if (LogE.isLog) {
//                LogE.e("wbb", "开始。。。");
//            }
            if (playId != null && !playId.equals(id)) {
                // 切歌了
                onSwitchMusic(url, id, playId);
            }
            playId = id;
            path = url;
            initPlay();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url); // 设置数据源
            mediaPlayer.prepareAsync(); // prepare自动播放
            isStart = true;
            if (listener != null) {
                listener.onStart(mediaPlayer);
            }
            // 监听
            musicStart(mediaPlayer, playId);
//            if (LogE.isLog) {
//                LogE.e("wbb", "结束。。。");
//            }
        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 初始化播放器
    private void initPlay() {
        try {
            if (handler == null) {
                handler = new Handler();
            }
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
//                mediaPlayer.setOnBufferingUpdateListener(this);  // 二级缓存，暂时不用
                mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.setOnErrorListener(this);
//                mediaPlayer.setOnInfoListener(this);
                mediaPlayer.setOnCompletionListener(this);
            }
            if (runnable != null) {
                handler.removeCallbacks(runnable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public boolean isStart() {
        return isStart;
    }

    public boolean isShowProgress() {
        return isShowProgress;
    }

    public String getPath() {
        return path;
    }

    public void timeViewUpdata(TextView timeView, ProgressBar progressBar) {
        if (timeView != null && mediaPlayer != null) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.ENGLISH);
                String runingtime = simpleDateFormat.format(mediaPlayer.getCurrentPosition());
                String alltime = simpleDateFormat.format(mediaPlayer.getDuration());
                timeView.setText(runingtime + "/" + alltime);
                progressBar.setMax(mediaPlayer.getDuration());
                progressBar.setProgress(mediaPlayer.getCurrentPosition());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断当前播放的是不是该资源
     *
     * @param id 本地地址
     * @return
     */
    public boolean isPlayingPath(String id) {
        if (playId != null) {
            if (playId.equals(id)) {
                return true;
            }
        }
        return false;
    }


    // 暂停
    public void pause() {
        mediaPlayer.pause();
        isStart = false;
    }

    // 停止
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            try {
                mediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mediaPlayer = null;
            path = null;
            playId = null;
        }
        isStart = false;
    }

    /**
     * 播放停止
     */
    public void mediaPlayerStop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void destroyPlay() {
        if (handler != null) {
            if (runnable != null) {
                handler.removeCallbacks(runnable);
            }
            handler = null;
        }
        stop(); // 停止及销毁媒体
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isStart = true;
        isShowProgress = false;
        mp.start();
        if (listener != null) {
            listener.onPrepared(mp);
        }
        musicPrepared(mp, playId);

        // 每一秒触发一次
        if (handler != null) {
            handler.postDelayed(runnable, 1000); //每隔1s执行
        }

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // 播放完成
        isStart = false;
        isShowProgress = false;
        if (listener != null) {
            listener.onCompletion(mp);
        }

        musicCompletion(mp, playId);

        destroyPlay();

    }

    /**
     * 缓冲更新
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
//        if (listener != null) {
//            listener.onBufferingUpdate(mp, percent);
//        }

    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        // 缓冲监听
        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
            if (listener != null) {
                listener.onBuffering(mp, extra);
            }
//            onBuffering(mp,extra);
        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
            //此接口每次回调完START就回调END,若不加上判断就会出现缓冲图标一闪一闪的卡顿现象,缓存结束
            if (listener != null) {
                listener.onBuffered(mp, extra);
            }
//            onBuffered(mp,extra);
        }
        return false;
    }

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                // 定时回调
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    int duration = mediaPlayer.getDuration();
                    if (listener != null) {
                        listener.onProgressUpdate(mediaPlayer, currentPosition, duration);
                    }
                    onProgressUpdate(mediaPlayer, currentPosition, duration, playId);
                    // 更新UI
//                    if (timeView != null) {
//                        if (simpleDateFormat != null) {
//                            try {
//                                String runingtime = simpleDateFormat.format(currentPosition);
//                                String alltime = simpleDateFormat.format(duration);
//                                timeView.setText(runingtime + "/" + alltime);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }
                    handler.postDelayed(this, 1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    private MusicListener listener;

    public void setOnCompletionListener(MusicListener listener) {
        this.listener = listener;
    }

    /**
     * 添加监听
     *
     * @param updater 监听器
     */
    public void addUpdater(final MusicStatusUpdater updater) {
        if (!updaterList.contains(updater)) {
            updaterList.add(updater);
        }
    }

    /**
     * 移除监听
     *
     * @param updater 监听器
     * @return 返回是否成功删除（true 为成功，false 为失败）
     */
    public boolean removeUpdater(final MusicStatusUpdater updater) {
        return updaterList.remove(updater);
    }


    // -----------------  音乐监听 ----------------------

    // 点击开始播放
    private void musicStart(MediaPlayer mp, String playId) {
        final List<MusicStatusUpdater> updaterListCopy = (List<MusicStatusUpdater>) updaterList.clone();
        if (updaterListCopy != null) {
            for (MusicStatusUpdater musicStatusUpdater : updaterListCopy) {
                musicStatusUpdater.onStart(mp, playId);
            }
        }
    }

    // 播放成功
    private void musicCompletion(MediaPlayer mp, String playId) {
        final List<MusicStatusUpdater> updaterListCopy = (List<MusicStatusUpdater>) updaterList.clone();
        if (updaterListCopy != null) {
            for (MusicStatusUpdater musicStatusUpdater : updaterListCopy) {
                musicStatusUpdater.onCompletion(mp, playId);
            }
        }
    }

    // 开始播放
    private void musicPrepared(MediaPlayer mp, String playId) {
        final List<MusicStatusUpdater> updaterListCopy = (List<MusicStatusUpdater>) updaterList.clone();
        if (updaterListCopy != null) {
            for (MusicStatusUpdater musicStatusUpdater : updaterListCopy) {
                musicStatusUpdater.onPrepared(mp, playId);
            }
        }
    }

    // 缓存中
    private void onBuffering(MediaPlayer mp, int percent, String playId) {
        final List<MusicStatusUpdater> updaterListCopy = (List<MusicStatusUpdater>) updaterList.clone();
        if (updaterListCopy != null) {
            for (MusicStatusUpdater musicStatusUpdater : updaterListCopy) {
                musicStatusUpdater.onBuffering(mp, percent, playId);
            }
        }
    }

    // 缓存结束

    private void onBuffered(MediaPlayer mp, int percent, String playId) {
        final List<MusicStatusUpdater> updaterListCopy = (List<MusicStatusUpdater>) updaterList.clone();
        if (updaterListCopy != null) {
            for (MusicStatusUpdater musicStatusUpdater : updaterListCopy) {
                musicStatusUpdater.onBuffered(mp, percent, playId);
            }
        }
    }

    private void onProgressUpdate(MediaPlayer mp, int currentPosition, int duration, String playId) {
        final List<MusicStatusUpdater> updaterListCopy = (List<MusicStatusUpdater>) updaterList.clone();
        if (updaterListCopy != null) {
            for (MusicStatusUpdater musicStatusUpdater : updaterListCopy) {
                musicStatusUpdater.onProgressUpdate(mp, currentPosition, duration, playId);
            }
        }
    }

    // 切换音乐时回调
    private void onSwitchMusic(String path, String playId, String oldPalyId) {
        final List<MusicStatusUpdater> updaterListCopy = (List<MusicStatusUpdater>) updaterList.clone();
        if (updaterListCopy != null) {
            for (MusicStatusUpdater musicStatusUpdater : updaterListCopy) {
                musicStatusUpdater.onSwitchMusic(path, playId, oldPalyId);
            }
        }
    }

    // 播放失败回调
    private void onMusicError(MediaPlayer mp, int what, int extra) {
        final List<MusicStatusUpdater> updaterListCopy = (List<MusicStatusUpdater>) updaterList.clone();
        if (updaterListCopy != null) {
            for (MusicStatusUpdater musicStatusUpdater : updaterListCopy) {
                musicStatusUpdater.onError(mp, what, extra);
            }
        }
    }


    /**
     * 获取当前界面资源的播放ID
     *
     * @param id   资源ID
     * @param page 页面名称
     * @return
     */
    public static String getPlayId(String id, String page) {
        return page + id;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        onMusicError(mp, what, extra);
        return false;
    }
}
