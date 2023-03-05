package com.js.phonicdiary.music;

import android.media.MediaPlayer;

/**
 * Created by 王兵兵 on 2019/1/23.
 */
public interface MusicListener{

    // 点击开始播放
    void onStart(MediaPlayer mp);

    // 播放成功
    void onCompletion(MediaPlayer mp);

    // 开始播放
    void onPrepared(MediaPlayer mp);

    // 缓存中
    void onBuffering(MediaPlayer mp, int percent);

    // 缓存结束
    void onBuffered(MediaPlayer mp, int percent);

    void onProgressUpdate(MediaPlayer mp, int currentPosition, int duration);

}
