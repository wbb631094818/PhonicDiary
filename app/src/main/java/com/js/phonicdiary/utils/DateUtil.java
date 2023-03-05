package com.js.phonicdiary.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.js.phonicdiary.DiaryApplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by admin on --.
 */

public class DateUtil {

    /**
     * 日期格式：yyyy-MM-dd HH:mm:ss
     **/
    public static final String DF_YYYY_MM_DD_HH_MM_SS = "yyyy_MM_dd_EEEE_hh_mm_ss_SSS";


    /**
     * 判断是否是今年
     *
     * @return
     */
    public static boolean isToYear(Date date) {
        Calendar time = Calendar.getInstance();
        time.setTime(date);
        int thenYear = time.get(Calendar.YEAR);
        time.setTime(time.getTime());
        return (thenYear == time.get(Calendar.YEAR));
    }


    /**
     * 将日期以yyyy-MM-dd HH:mm:ss格式化
     *
     * @param dateLong 日期
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatDateTime(long dateLong) {
        SimpleDateFormat sdf = new SimpleDateFormat(DF_YYYY_MM_DD_HH_MM_SS, Locale.ENGLISH);
        Date date = new Date(dateLong);
        return sdf.format(date);
    }

    /**
     *  获取日记月份
     * @param dateLong
     * @return
     */
    public static String getDataForMonth(long dateLong) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM", Locale.getDefault());
        Date date = new Date(dateLong);
        return sdf.format(date);
    }
    /**
     *  获取日记日期
     * @param time
     * @return
     */
    public static String getDataForDay(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd", Locale.getDefault());
        return sdf.format(time);
    }

    /**
     *  获取日记日期
     * @param dateLong
     * @return
     */
    public static String getDataDay(long dateLong) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.getDefault());
        Date date = new Date(dateLong);
        return sdf.format(date);
    }
    /**
     *  获取日记周几
     * @param dateLong
     * @return
     */
    public static String getDataForWeek(long dateLong) {
        SimpleDateFormat sdf = new SimpleDateFormat("EE", Locale.getDefault());
        Date date = new Date(dateLong);
        return sdf.format(date);
    }


    /**
     * 格式化时间，（系统时间格式）
     *
     * @param date
     * @return
     */
    public static String getDataTime(long date) {
        return SimpleDateFormat.getDateInstance(DateFormat.MEDIUM).format(date)+" "+ new SimpleDateFormat("EEEE", Locale.getDefault()).format(date);
    }

    public static String getFormatTime(Date date) {
        return SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format(date);
    }
    public static String getFormatDataShort(long date) {
        return SimpleDateFormat.getDateInstance(DateFormat.SHORT).format(date);
    }
    public static String getFormatData(long date) {
        if (Utils.isZh(DiaryApplication.getInstance())){
            return new SimpleDateFormat("M月dd日",Locale.CHINA).format(date);
        }
        return new SimpleDateFormat("MMM,dd",Locale.getDefault()).format(date);
    }

    /**
     * 判断是否是24小时制
     *
     * @param context 上下文
     * @return 返回真假
     */
    public static boolean is24HourMode(Context context) {
        return android.text.format.DateFormat.is24HourFormat(context);
    }

    /**
     * 将时分格式化
     *
     * @param hour
     * @param minute
     * @return
     */
    public static String getFormatTime(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);//时
        calendar.set(Calendar.MINUTE, minute);//分
        return SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
    }

    /**
     * 将年月日换算成long时间
     *
     * @param year 年
     * @param month 月
     * @param dayOfMonth 日
     * @return
     */
    public static long getLongData(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        //年
        calendar.set(Calendar.YEAR, year);
        //月
        calendar.set(Calendar.MONTH, month);
        // 日
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return calendar.getTimeInMillis();
    }

}

