package com.js.phonicdiary.callback;

public interface VoiceCallBack {
    //录音结束
    void recFinish(long length, String strLength, String path);
}
