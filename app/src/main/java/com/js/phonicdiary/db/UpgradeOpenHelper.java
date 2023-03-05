package com.js.phonicdiary.db;

import android.content.Context;

import com.js.phonicdiary.dbhelper.MigrationHelper;

import org.greenrobot.greendao.database.Database;

/**
 *  数据库升级操作
 * @author 兵兵
 */
public class UpgradeOpenHelper extends DaoMaster.OpenHelper{
    public UpgradeOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        //把需要管理的数据库表DAO作为最后一个参数传入到方法中
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {

            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        },  DiaryInfoDao.class);


    }
}
