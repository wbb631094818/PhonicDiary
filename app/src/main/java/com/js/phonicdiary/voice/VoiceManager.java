package com.js.phonicdiary.voice;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.js.phonicdiary.DiaryApplication;
import com.js.phonicdiary.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 录音管理类（包括录音和播放）
 * 作者：xsf
 * 创建时间：2017/3/16
 * 最后修改时间：2017/3/16
 */

/**
 * 录音管理（包括播放和监听）
 */
public class VoiceManager {

    private static volatile VoiceManager voiceManager;
    public final int MSG_TIME_INTERVAL = 100;
    // 多媒体例如声音的状态
    public final int MEDIA_STATE_UNDEFINE = 200;
    public final int MEDIA_STATE_RECORD_STOP = 210;
    public final int MEDIA_STATE_RECORD_DOING = 220;
    public final int MEDIA_STATE_RECORD_PAUSE = 230;

    private ArrayList<File> mRecList = new ArrayList<File>();
    private int mSavedState, mDeviceState = MEDIA_STATE_UNDEFINE;
    private MediaRecorder mMediaRecorder = null;
    private String mRecTimePrev;
    private long mRecTimeSum = 0;

    private Handler handler;
    /**
     * 录音文件存放的位置(文件夹)
     */
    private String recordFilePath = "";
    /**
     * 播放音频文件位置
     */
    private String playFilePath;
    /**
     * 录音监听
     */
    private VoiceRecordCallBack voiceRecordCallBack;
    private ObtainDecibelThread mThread;

    private VoiceManager() {
        handler = new Handler(Looper.getMainLooper());
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static VoiceManager getInstance() {
        if (voiceManager == null) {
            synchronized (VoiceManager.class) {
                if (voiceManager == null) {
                    voiceManager = new VoiceManager();
                }
            }
        }
        return voiceManager;
    }

    /**
     * 播放或录音handler
     */
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            VoiceTimeUtils ts;
            int current;
            try {
                switch (msg.what) {
                    //录音
                    case MSG_TIME_INTERVAL:
                        if (mDeviceState == MEDIA_STATE_RECORD_DOING) {
                            ts = VoiceTimeUtils.timeSpanToNow(mRecTimePrev);
                            mRecTimeSum += ts.mDiffSecond;
                            mRecTimePrev = VoiceTimeUtils.getTimeStrFromMillis(ts.mNowTime);
                            ts = VoiceTimeUtils.timeSpanSecond(mRecTimeSum);
                            //回调录音时间
                            if (voiceRecordCallBack != null) {
                                voiceRecordCallBack.recDoing(mRecTimeSum, String.format("%02d:%02d:%02d",
                                        ts.mSpanHour, ts.mSpanMinute, ts.mSpanSecond));
                            }
                            mHandler.sendEmptyMessageDelayed(MSG_TIME_INTERVAL, 1000);
                        }
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
            }
        }
    };
    /*********************************录音操作begin***************************/
    /**
     * 录音监听
     *
     * @param callBack
     */
    public void setVoiceRecordListener(VoiceRecordCallBack callBack) {
        voiceRecordCallBack = callBack;
    }

    /**
     * 开始录音（外部调）
     *
     * @param filePath 音频存放文件夹
     */
    public void startVoiceRecord(String filePath) {
        if (!isSDCardAvailable()) {
            return;
        }
        this.recordFilePath = filePath;
        startVoiceRecord(true);
    }

    /**
     * 继续或暂停录音
     */
    public void pauseOrStartVoiceRecord() {
        if (mDeviceState == MEDIA_STATE_RECORD_DOING) {
            mDeviceState = MEDIA_STATE_RECORD_PAUSE;
            stopRecorder(mMediaRecorder, true);
            mMediaRecorder = null;
            voiceRecordCallBack.recPause("已暂停");
        } else {
            startVoiceRecord(false);
        }
    }

    /**
     * 完成录音
     */
    public void stopVoiceRecord() {
        try {
            mHandler.removeMessages(MSG_TIME_INTERVAL);
            mDeviceState = MEDIA_STATE_RECORD_STOP;
            stopRecorder(mMediaRecorder, true);
            mMediaRecorder = null;
            if (VoiceTimeUtils.timeSpanSecond(mRecTimeSum).mSpanSecond == 0) {
                Toast.makeText(DiaryApplication.getInstance(), R.string.time_is_too_short, Toast.LENGTH_SHORT).show();
            } else {
                File file = getOutputVoiceFile(mRecList);
                if (file != null && file.length() > 0) {
                    cleanFieArrayList(mRecList);
                    //TODO 这里可以返回数据 setResult
                    final VoiceTimeUtils ts = VoiceTimeUtils.timeSpanSecond(mRecTimeSum);
                    //完成录音
                    if (voiceRecordCallBack != null) {
                        voiceRecordCallBack.recFinish(mRecTimeSum, String.format("%02d:%02d:%02d",
                                ts.mSpanHour, ts.mSpanMinute, ts.mSpanSecond), file.getAbsolutePath());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始录音(内部调)
     *
     * @param init 是否是初始化录音还是暂停后再录音
     */
    private void startVoiceRecord(boolean init) {
        if (!isSDCardAvailable()) {
            return;
        }
        if (init) {
            mRecTimeSum = 0;
            cleanFieArrayList(mRecList);
        }
        stopRecorder(mMediaRecorder, true);
        mMediaRecorder = null;

        mMediaRecorder = new MediaRecorder();
        File file = prepareRecorder(mMediaRecorder, true);
        if (file != null) {
            //开始录音回调
            if (voiceRecordCallBack != null) {
                voiceRecordCallBack.recStart(init);
            }
            mDeviceState = MEDIA_STATE_RECORD_DOING;
            mRecTimePrev = VoiceTimeUtils.getTimeStrFromMillis(System.currentTimeMillis());
            mRecList.add(file);

            mHandler.removeMessages(MSG_TIME_INTERVAL);
            mHandler.sendEmptyMessage(MSG_TIME_INTERVAL);
        }
    }

    /**
     * 监听录音声音频率大小
     */
    private class ObtainDecibelThread extends Thread {

        private volatile boolean running = true;

        public void exit() {
            running = false;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mMediaRecorder == null || !running) {
                    break;
                }
                try {
                    final double ratio = mMediaRecorder.getMaxAmplitude() / 150;
                    if (ratio != 0 && voiceRecordCallBack != null) {
                        double db = 0;// 分贝
                        if (ratio > 1) {
                            db = (int) (20 * Math.log10(ratio));
                        }
                        if (handler == null) {
                            handler = new Handler(Looper.getMainLooper());
                        }
                        final double finalDb = db;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                voiceRecordCallBack.recVoiceGrade((int) finalDb);
                            }
                        });
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    /**
     * 合并录音
     *
     * @param list
     * @return
     */
    private File getOutputVoiceFile(ArrayList<File> list) {
        String mMinute1 = VoiceTimeUtils.getTime();
        File recDirFile = recAudioDir(recordFilePath);

        // 创建音频文件,合并的文件放这里
        File resFile = new File(recDirFile, mMinute1 + ".amr");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(resFile);
        } catch (IOException e) {
        }
        // list里面为暂停录音 所产生的 几段录音文件的名字，中间几段文件的减去前面的6个字节头文件
        for (int i = 0; i < list.size(); i++) {
            File file = list.get(i);
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] myByte = new byte[fileInputStream.available()];
                // 文件长度
                int length = myByte.length;
                // 头文件
                if (i == 0) {
                    while (fileInputStream.read(myByte) != -1) {
                        fileOutputStream.write(myByte, 0, length);
                    }
                }
                // 之后的文件，去掉头文件就可以了
                else {
                    while (fileInputStream.read(myByte) != -1) {
                        fileOutputStream.write(myByte, 6, length - 6);
                    }
                }
                fileOutputStream.flush();
                fileInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 结束后关闭流
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resFile;
    }

    /**
     * 清空暂停录音所产生的几段录音文件
     *
     * @param list
     */
    private void cleanFieArrayList(ArrayList<File> list) {
        for (File file : list) {
            file.delete();
        }
        list.clear();
    }

    /*********************************录音操作end***************************/


    /**
     * 播放录音准备工作
     *
     * @param mp
     * @param file
     * @return
     */
    private boolean prepareMedia(MediaPlayer mp, String file) {
        boolean result = false;
        try {
            mp.setDataSource(file);
            mp.prepare();
            result = true;
        } catch (Exception e) {
        }

        return result;
    }

    /**
     * 播放录音开始
     *
     * @param mp
     * @return
     */
    private boolean playMedia(MediaPlayer mp) {
        boolean result = false;
        try {
            if (mp != null) {
                mp.start();
                result = true;
            }
        } catch (Exception e) {
        }

        return result;
    }

    /**
     * 拖动播放进度条
     *
     * @param mp
     * @param pos
     * @return
     */
    private boolean seektoMedia(MediaPlayer mp, int pos) {
        boolean result = false;
        try {
            if (mp != null && pos >= 0) {
                mp.seekTo(pos);
                result = true;
            }
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 停止播放
     *
     * @param mp
     * @param release
     * @return
     */
    private boolean stopMedia(MediaPlayer mp, boolean release) {
        boolean result = false;
        try {
            if (mp != null) {
                mp.stop();
                if (release) {
                    mp.release();
                }
                result = true;
            }
        } catch (Exception e) {
        }

        return result;
    }

    /**
     * 暂停播放
     *
     * @param mp
     * @return
     */
    private boolean pauseMedia(MediaPlayer mp) {
        boolean result = false;

        try {
            if (mp != null) {
                mp.pause();
                result = true;
            }
        } catch (Exception e) {
        }

        return result;
    }

    /**
     * 最后停止录音
     *
     * @param mr
     * @param release
     * @return
     */
    private boolean stopRecorder(MediaRecorder mr, boolean release) {
        boolean result = false;
        try {
            if (mr != null) {
                mr.stop();
                if (release) {
                    mr.release();
                }
                result = true;
            }
            if (mThread != null) {
                mThread.exit();
                mThread = null;
            }
        } catch (Exception e) {
            if (mThread != null) {
                mThread.exit();
                mThread = null;
            }
        }
        return result;
    }

    /**
     * 录音准备工作 ，开始录音
     *
     * @param mr
     * @param start
     * @return
     */
    @SuppressWarnings("deprecation")
    private File prepareRecorder(MediaRecorder mr, boolean start) {
        File recFile = null;
        if (mr == null) {
            return null;
        }
        try {
            String path = recAudioDir(recordFilePath).getAbsolutePath();
            recFile = new File(path, VoiceTimeUtils.getTime() + ".amr");
            mr.setAudioSource(MediaRecorder.AudioSource.MIC);
            mr.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
            mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mr.setOutputFile(recFile.getAbsolutePath());
            mr.prepare();
            if (start) {
                mr.start();
                if (mThread == null) {
                    mThread = new ObtainDecibelThread();
                    mThread.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recFile;
    }

    /**
     * 停止录音
     */
    public void stopRecordAndPlay() {
        stopRecorder(mMediaRecorder, true);
        mMediaRecorder = null;
    }

    /**
     * 录音回调监听
     */
    public interface VoiceRecordCallBack {
        //录音中
        void recDoing(long time, String strTime);

        //录音中的声音频率等级
        void recVoiceGrade(int grade);

        //录音开始
        void recStart(boolean init);

        //录音暂停
        void recPause(String str);

        //录音结束
        void recFinish(long length, String strLength, String path);
    }

    /**
     * SD卡是否可用
     */
    public static boolean isSDCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static File recAudioDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
}
