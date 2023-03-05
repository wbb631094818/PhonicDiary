package com.js.phonicdiary.manager;

import com.js.phonicdiary.DiaryApplication;
import com.js.phonicdiary.bean.DiaryInfo;
import com.js.phonicdiary.db.DiaryInfoDao;
import com.js.phonicdiary.utils.DateUtil;
import com.js.phonicdiary.utils.LogE;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CalendarManager {


    /**
     * 查询所有日记数据
     */
    public static void query(Observer<? super Map<String, com.haibin.calendarview.Calendar>> observer) {
        Observable.create(new ObservableOnSubscribe<Map<String, com.haibin.calendarview.Calendar>>() {
            @Override
            public void subscribe(ObservableEmitter<Map<String, com.haibin.calendarview.Calendar>> emitter) throws Exception {
                List<DiaryInfo> diaryInfos = DiaryApplication.getInstance().getDaoSession().getDiaryInfoDao()
                        .queryBuilder().orderDesc(DiaryInfoDao.Properties.CreateTime).list();
                Map<String, com.haibin.calendarview.Calendar> map = new HashMap<>();
                // 号码时间的日历
                Calendar dateCal = Calendar.getInstance();
                for (DiaryInfo info : diaryInfos) {
                    dateCal.setTimeInMillis(info.getCreateTime());
                    com.haibin.calendarview.Calendar schemeCalendar = getSchemeCalendar(dateCal.get(Calendar.YEAR), dateCal.get(Calendar.MONTH) + 1, dateCal.get(Calendar.DAY_OF_MONTH), 0xFF40db25, "假");
                    map.put(schemeCalendar.toString(),
                            schemeCalendar);
                }
                emitter.onNext(map);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    private static com.haibin.calendarview.Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        com.haibin.calendarview.Calendar calendar = new com.haibin.calendarview.Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }


    /**
     * 查询所有日记数据
     */
    public static void query(final long time, Observer<? super List<DiaryInfo>> observe) {
        if (LogE.isLog) {
            LogE.e("wbb", "time: " + DateUtil.getDataForDay(time));
        }
        Observable.create(new ObservableOnSubscribe<List<DiaryInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<DiaryInfo>> emitter) throws Exception {
                List<DiaryInfo> diaryInfos = DiaryApplication.getInstance().getDaoSession().getDiaryInfoDao()
                        .queryBuilder().where(DiaryInfoDao.Properties.Data.eq(DateUtil.getDataForDay(time)))
                        .orderDesc(DiaryInfoDao.Properties.Time).orderDesc(DiaryInfoDao.Properties.CreateTime).list();
                emitter.onNext(diaryInfos);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observe);
    }
}
