package com.js.phonicdiary.manager;

import com.js.phonicdiary.DiaryApplication;
import com.js.phonicdiary.R;
import com.js.phonicdiary.bean.DiaryInfo;
import com.js.phonicdiary.bean.VoiceInfo;
import com.js.phonicdiary.db.DiaryInfoDao;
import com.js.phonicdiary.db.VoiceInfoDao;
import com.js.phonicdiary.utils.DateUtil;
import com.js.phonicdiary.utils.LogE;

import java.io.File;
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

public class DiaryDbManager {

    /**
     * 插入日记数据
     *
     * @param diaryInfo
     */
    public static void insertOrReplace(final DiaryInfo diaryInfo) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                long id = DiaryApplication.getInstance().getDaoSession().insertOrReplace(diaryInfo);
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
     * 查询所有日记数据
     */
    public static void query(Observer<? super List<DiaryInfo>> observer) {
        Observable.create(new ObservableOnSubscribe<List<DiaryInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<DiaryInfo>> emitter) throws Exception {
                List<DiaryInfo> diaryInfos = DiaryApplication.getInstance().getDaoSession().getDiaryInfoDao()
                        .queryBuilder().orderDesc(DiaryInfoDao.Properties.Time).orderDesc(DiaryInfoDao.Properties.CreateTime).list();
                List<DiaryInfo> diaryInfoList = new ArrayList<>();
                HashMap<String, String> titles = new HashMap<>();
                // 今天的日历
                Calendar todayCal = Calendar.getInstance();
                todayCal.setTime(new Date());
                todayCal.setFirstDayOfWeek(Calendar.MONDAY);
                // 号码时间的日历
                Calendar dateCal = Calendar.getInstance();
                dateCal.setFirstDayOfWeek(Calendar.MONDAY);

                for (DiaryInfo info : diaryInfos) {
                    dateCal.setTimeInMillis(info.getCreateTime());
                    if (todayCal.getTimeInMillis() > info.getCreateTime()) {
                        if (todayCal.get(Calendar.YEAR) == dateCal.get(Calendar.YEAR)
                                && todayCal.get(Calendar.MONTH) == dateCal.get(Calendar.MONTH)
                                && todayCal.get(Calendar.DAY_OF_YEAR) == dateCal.get(Calendar.DAY_OF_YEAR)) {
                            // 今天
                            if (titles.get("today") == null) {
                                DiaryInfo diaryInfo = new DiaryInfo();
                                diaryInfo.setLayoutType(DiaryInfo.TITLE_LAYOUT);
                                diaryInfo.setTitle(DiaryApplication.getInstance().getString(R.string.Today));
                                diaryInfoList.add(diaryInfo);
                                titles.put("today", "today");
                            }
                        } else if (todayCal.get(Calendar.YEAR) == dateCal.get(Calendar.YEAR)
                                && todayCal.get(Calendar.DAY_OF_YEAR) - 1 == dateCal.get(Calendar.DAY_OF_YEAR)) {
                            // 昨天
                            if (titles.get("YESTERDAY") == null) {
                                DiaryInfo diaryInfo = new DiaryInfo();
                                diaryInfo.setLayoutType(DiaryInfo.TITLE_LAYOUT);
                                diaryInfo.setTitle(DiaryApplication.getInstance().getString(R.string.Yesterday));
                                diaryInfoList.add(diaryInfo);
                                titles.put("YESTERDAY", "YESTERDAY");
                            }
                        } else if (todayCal.get(Calendar.WEEK_OF_YEAR) == dateCal.get(Calendar.WEEK_OF_YEAR) && todayCal.get(Calendar.YEAR) == dateCal.get(Calendar.YEAR)) {
                            // 同一周内
                            if (titles.get("This_week") == null) {
                                DiaryInfo diaryInfo = new DiaryInfo();
                                diaryInfo.setLayoutType(DiaryInfo.TITLE_LAYOUT);
                                diaryInfo.setTitle(DiaryApplication.getInstance().getString(R.string.This_week));
                                diaryInfoList.add(diaryInfo);
                                titles.put("This_week", "This_week");
                            }
                        } else if (todayCal.get(Calendar.MONTH) == dateCal.get(Calendar.MONTH) && todayCal.get(Calendar.YEAR) == dateCal.get(Calendar.YEAR)) {
                            // 同一月内
                            if (titles.get("This_month") == null) {
                                DiaryInfo diaryInfo = new DiaryInfo();
                                diaryInfo.setLayoutType(DiaryInfo.TITLE_LAYOUT);
                                diaryInfo.setTitle(DiaryApplication.getInstance().getString(R.string.This_month));
                                diaryInfoList.add(diaryInfo);
                                titles.put("This_month", "This_month");
                            }
                        }/* else if (todayCal.get(Calendar.MONTH)  == dateCal.get(Calendar.MONTH) && todayCal.get(Calendar.YEAR) == dateCal.get(Calendar.YEAR)) {
                            // 同一月内
                            if (titles.get("This_month") == null) {
                                DiaryInfo diaryInfo = new DiaryInfo();
                                diaryInfo.setLayoutType(DiaryInfo.TITLE_LAYOUT);
                                diaryInfo.setTitle(DiaryApplication.getInstance().getString(R.string.This_month));
                                diaryInfoList.add(diaryInfo);
                                titles.put("This_month", "This_month");
                            }
                        }*/ else {
                            String month = DateUtil.getDataForMonth(info.getCreateTime());
                            if (titles.get(month) == null) {
                                DiaryInfo diaryInfo = new DiaryInfo();
                                diaryInfo.setLayoutType(DiaryInfo.TITLE_LAYOUT);
                                diaryInfo.setTitle(DiaryApplication.getInstance().getString(R.string.This_month));
                                diaryInfoList.add(diaryInfo);
                                titles.put(month, month);
                            }
                        }
                    } else {
                        // 未来
                        if (titles.get("future") == null) {
                            DiaryInfo diaryInfo = new DiaryInfo();
                            diaryInfo.setLayoutType(DiaryInfo.TITLE_LAYOUT);
                            diaryInfo.setTitle("future");
                            diaryInfoList.add(diaryInfo);
                            titles.put("future", "future");
                        }
                    }
                    diaryInfoList.add(info);
                }
                if (LogE.isLog) {
                    LogE.e("wbb", "查询成功： ");
                }
                emitter.onNext(diaryInfoList);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    /**
     * 根据id删除日记数据
     *
     * @param diaryInfo
     */
    public static void delete(final DiaryInfo diaryInfo, Observer<? super String> observer) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                try {
                    // 删除日记数据
                    DiaryApplication.getInstance().getDaoSession().getDiaryInfoDao()
                            .delete(diaryInfo);
                    // 删除录音数据
                    VoiceDbManager.deleteWithCreateTime(diaryInfo.getCreateTime());
                    // 删除图片数据
                    PhotoDbManager.deleteWithCreateTime(diaryInfo.getCreateTime());
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

}
