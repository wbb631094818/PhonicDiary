package com.js.phonicdiary.manager;

import com.js.phonicdiary.DiaryApplication;
import com.js.phonicdiary.bean.DiaryInfo;
import com.js.phonicdiary.bean.PhotoInfo;
import com.js.phonicdiary.bean.VoiceInfo;
import com.js.phonicdiary.db.DiaryInfoDao;
import com.js.phonicdiary.db.PhotoInfoDao;
import com.js.phonicdiary.db.VoiceInfoDao;
import com.js.phonicdiary.utils.FileUtils;
import com.js.phonicdiary.utils.LogE;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 图片数据库管理类
 *
 * @author 兵兵
 */
public class PhotoDbManager {

    /**
     * 插入日记数据
     *
     * @param photoInfos
     */
    public static void insert(final List<PhotoInfo> photoInfos) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                for (PhotoInfo info : photoInfos) {
                    long id = DiaryApplication.getInstance().getDaoSession().insert(info);
                    DiaryApplication.getInstance().getDaoSession().clear();
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

    /**
     * 查询所有图片数据
     */
    public static void query(Observer<? super List<PhotoInfo>> observer) {
        Observable.create(new ObservableOnSubscribe<List<PhotoInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<PhotoInfo>> emitter) throws Exception {
                List<PhotoInfo> photoInfos = DiaryApplication.getInstance().getDaoSession().getPhotoInfoDao()
                        .queryBuilder().list();
                emitter.onNext(photoInfos);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    /**
     * 根据id删除录音数据
     *
     * @param photoInfo
     */
    public static void delete(final PhotoInfo photoInfo, Observer<? super String> observer) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                try {
                    DiaryApplication.getInstance().getDaoSession().getPhotoInfoDao()
                            .delete(photoInfo);
                    DiaryApplication.getInstance().getDaoSession().clear();
//                    File file = new File(photoInfo.getPath());
//                    file.delete();
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
            DiaryApplication.getInstance().getDaoSession().getPhotoInfoDao()
                    .queryBuilder().where(PhotoInfoDao.Properties.CreateTime.eq(createTime)).buildDelete().executeDeleteWithoutDetachingEntities();
//            FileUtils.deleteFile(FileUtils.getDiaryFilePath(createTime));
            if (LogE.isLog) {
                LogE.e("wbb", "删除成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
