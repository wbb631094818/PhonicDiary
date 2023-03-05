package com.js.phonicdiary.music;

import android.media.MediaPlayer;

/**
 * Created by 王兵兵 on 2019/1/25.
 */
public interface MusicStatusUpdater {

    // 点击开始播放
    void onStart(MediaPlayer mp, String playId);

    // 播放成功
    void onCompletion(MediaPlayer mp, String playId);

    // 开始播放
    void onPrepared(MediaPlayer mp, String playId);

    // 缓存中
    void onBuffering(MediaPlayer mp, int percent, String playId);

    // 缓存结束
    void onBuffered(MediaPlayer mp, int percent, String playId);

    void onProgressUpdate(MediaPlayer mp, int currentPosition, int duration, String playId);

    // 切换音乐时回调
    void onSwitchMusic(String path, String playId, String oldPalyId);

    // 播放失败
    void onError(MediaPlayer mp, int what, int extra);

}
