package com.js.phonicdiary;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.js.phonicdiary.db.DaoMaster;
import com.js.phonicdiary.db.DaoSession;
import com.js.phonicdiary.db.UpgradeOpenHelper;

import org.greenrobot.greendao.database.DatabaseOpenHelper;

import java.util.ArrayList;

/**
 * 全局Application
 *
 * @author 王兵兵
 */
public class DiaryApplication extends Application implements ThemeUtils.switchColor {

    private static DiaryApplication singleton = null;

    public static DiaryApplication getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        // 数据库初始化
        initGreenDao();
    }


    /**
     * 初始化GreenDao,直接在Application中进行初始化操作
     */
    private void initGreenDao() {
        UpgradeOpenHelper helper = new UpgradeOpenHelper(this, "diarydata.db");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    private DaoSession daoSession;

    public DaoSession getDaoSession() {
        return daoSession;
    }

    // activity 集合
    private ArrayList<Activity> listActivity = new ArrayList<>();


    public void setListActivity(Activity activity) {
        if (listActivity != null) {
            listActivity.add(activity);
        }
    }

    @Override
    public int replaceColorById(Context context, int colorId) {
        return 0;
    }

    @Override
    public int replaceColor(Context context, int color) {
        return 0;
    }
}
