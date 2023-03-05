package com.js.phonicdiary.manager;

import com.js.phonicdiary.DiaryApplication;
import com.js.phonicdiary.R;
import com.js.phonicdiary.bean.DiaryInfo;
import com.js.phonicdiary.db.DiaryInfoDao;
import com.js.phonicdiary.utils.DateUtil;
import com.js.phonicdiary.utils.LogE;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchManager {


    /**
     * 查询所有日记数据
     */
    public static void query(final String searchText, Observer<? super List<DiaryInfo>> observer) {
        Observable.create(new ObservableOnSubscribe<List<DiaryInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<DiaryInfo>> emitter) throws Exception {
                List<DiaryInfo> diaryInfos = DiaryApplication.getInstance().getDaoSession().getDiaryInfoDao()
                        .queryBuilder()
                        .whereOr(DiaryInfoDao.Properties.Title.like("%"+searchText+"%"),DiaryInfoDao.Properties.Text.like("%"+searchText+"%"),DiaryInfoDao.Properties.Weather.like("%"+searchText+"%"))
                        .orderDesc(DiaryInfoDao.Properties.Time).orderDesc(DiaryInfoDao.Properties.CreateTime).list();
                if (LogE.isLog) {
                    LogE.e("wbb", "查询成功： ");
                }
                emitter.onNext(diaryInfos);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

}
