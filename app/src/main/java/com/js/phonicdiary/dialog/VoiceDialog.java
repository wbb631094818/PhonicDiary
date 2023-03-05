package com.js.phonicdiary.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.js.phonicdiary.DiaryApplication;
import com.js.phonicdiary.R;
import com.js.phonicdiary.bean.VoiceInfo;
import com.js.phonicdiary.callback.VoiceCallBack;
import com.js.phonicdiary.manager.VoiceDbManager;
import com.js.phonicdiary.utils.DisplayUtil;
import com.js.phonicdiary.utils.FileUtils;
import com.js.phonicdiary.view.VoiceLineView;
import com.js.phonicdiary.voice.VoiceManager;

import java.io.File;

public class VoiceDialog {

    /**
     * 展示录音中弹窗
     *
     * @param activity   上下文
     * @param createTime 日记创建时间（用于绑定录音文件）
     * @param callBack   录音回调
     * @return 弹窗类
     */
    public static AlertDialog showVoiceDialog(Activity activity, final long createTime, boolean isShowKeyboard, final VoiceCallBack callBack) {
        final View view = LayoutInflater.from(activity).inflate(R.layout.dialog_record_voice, null);
        final VoiceLineView mDialogRecordVoicLine = (VoiceLineView) view.findViewById(R.id.dialog_record_voicLine);
        ImageView mDialogRecordVoice = (ImageView) view.findViewById(R.id.dialog_record__voice);
        final TextView mDialogRecordLength = (TextView) view.findViewById(R.id.dialog_record__length);
        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setView(view)
                .setCancelable(false)
                .show();
        //此处设置位置窗体大小
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setLayout(DisplayUtil.dip2px(activity, 200), LinearLayout.LayoutParams.WRAP_CONTENT);
            Window window = alertDialog.getWindow();
            window.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);

            WindowManager.LayoutParams params = window.getAttributes();
            if (isShowKeyboard) {
                params.y = DisplayUtil.dip2px(activity, 120);
            } else {
                params.y = DisplayUtil.dip2px(activity, 220);
            }

            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            window.setAttributes(params);
        }
        VoiceManager.getInstance().startVoiceRecord(FileUtils.getDiaryFilePath("voice"));
        VoiceManager.getInstance().setVoiceRecordListener(new VoiceManager.VoiceRecordCallBack() {
            @Override
            public void recDoing(long time, String strTime) {
                mDialogRecordLength.setText(strTime);
            }

            @Override
            public void recVoiceGrade(int grade) {
                mDialogRecordVoicLine.setVolume(grade);
            }

            @Override
            public void recStart(boolean init) {
                mDialogRecordVoicLine.run();
            }

            @Override
            public void recPause(String str) {
                mDialogRecordVoicLine.setPause();
            }

            @Override
            public void recFinish(long length, String strLength, String path) {
                if (mDialogRecordVoicLine != null) {
                    mDialogRecordVoicLine.setPause();
                }
                VoiceInfo voiceInfo = new VoiceInfo();
                voiceInfo.setId(System.currentTimeMillis());
                voiceInfo.setFileCreateDate(System.currentTimeMillis());
                voiceInfo.setFilePath(path);
                voiceInfo.setCreateTime(createTime);
                File file = new File(path);
                if (file.exists()) {
                    voiceInfo.setFileName(file.getName());
                    voiceInfo.setFileSize(file.length());
                }
                VoiceDbManager.insert(voiceInfo);
                if (callBack != null) {
                    callBack.recFinish(length, strLength, path);
                }
            }
        });

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                VoiceManager.getInstance().stopVoiceRecord();
            }
        });

        return alertDialog;
    }


}
