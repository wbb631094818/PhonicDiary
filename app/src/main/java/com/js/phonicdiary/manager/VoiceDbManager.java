package com.js.phonicdiary.manager;

import com.js.phonicdiary.DiaryApplication;
import com.js.phonicdiary.bean.VoiceInfo;
import com.js.phonicdiary.db.VoiceInfoDao;
import com.js.phonicdiary.utils.FileUtils;
import com.js.phonicdiary.utils.LogE;

import org.reactivestreams.Subscriber;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VoiceDbManager {

    /**
     * 插入录音数据
     *
     * @param voiceInfo
     */
    public static void insert(final VoiceInfo voiceInfo) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                long id = DiaryApplication.getInstance().getDaoSession().insert(voiceInfo);
                DiaryApplication.getInstance().getDaoSession().clear();
                if (LogE.isLog) {
                    LogE.e("wbb", "插入成功： " + id);
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

    /**
     * 根据日记创建时间查询录音数据
     *
     * @param createTime
     */
    public static void query(final long createTime, Observer<? super List<VoiceInfo>> observer) {
        Observable.create(new ObservableOnSubscribe<List<VoiceInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<VoiceInfo>> emitter) throws Exception {
                List<VoiceInfo> voiceInfoList = DiaryApplication.getInstance().getDaoSession().getVoiceInfoDao()
                        .queryBuilder().where(VoiceInfoDao.Properties.CreateTime.eq(createTime)).list();
                if (LogE.isLog) {
                    LogE.e("wbb", "查询成功： ");
                }
                emitter.onNext(voiceInfoList);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    /**
     * 根据id删除录音数据
     *
     * @param voiceInfo
     */
    public static void delete(final VoiceInfo voiceInfo, Observer<? super String> observer) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                try {
                    DiaryApplication.getInstance().getDaoSession().getVoiceInfoDao()
                            .delete(voiceInfo);
                    File file = new File(voiceInfo.getFilePath());
                    file.delete();
                    DiaryApplication.getInstance().getDaoSession().clear();
                    if (LogE.isLog) {
                        LogE.e("wbb", "删除成功");
                    }
                    emitter.onComplete();
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    /**
     * 根据日记创建时间删除录音数据
     *
     * @param createTime 日记创建时间
     */
    public static void deleteWithCreateTime(final long createTime) {
        try {
            VoiceInfo voiceInfo = DiaryApplication.getInstance().getDaoSession().getVoiceInfoDao().queryBuilder().where(VoiceInfoDao.Properties.CreateTime.eq(createTime)).unique();
            if (voiceInfo != null) {
                FileUtils.deleteFile(FileUtils.getDiaryFilePath(voiceInfo.getFilePath()));
                DiaryApplication.getInstance().getDaoSession().getVoiceInfoDao().delete(voiceInfo);
                if (LogE.isLog) {
                    LogE.e("wbb", "删除成功");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
