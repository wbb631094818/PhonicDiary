package com.js.phonicdiary.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.js.phonicdiary.bean.DiaryInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DIARY_INFO".
*/
public class DiaryInfoDao extends AbstractDao<DiaryInfo, Long> {

    public static final String TABLENAME = "DIARY_INFO";

    /**
     * Properties of entity DiaryInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property LayoutType = new Property(1, int.class, "layoutType", false, "LAYOUT_TYPE");
        public final static Property Title = new Property(2, String.class, "title", false, "title");
        public final static Property Text = new Property(3, String.class, "text", false, "text");
        public final static Property Time = new Property(4, long.class, "time", false, "time");
        public final static Property Weather = new Property(5, String.class, "weather", false, "weather");
        public final static Property Label = new Property(6, String.class, "label", false, "label");
        public final static Property Mood = new Property(7, String.class, "mood", false, "mood");
        public final static Property Location = new Property(8, String.class, "location", false, "location");
        public final static Property ImageSize = new Property(9, int.class, "imageSize", false, "imageSize");
        public final static Property RecordSize = new Property(10, int.class, "recordSize", false, "recordSize");
        public final static Property CreateTime = new Property(11, long.class, "createTime", false, "createTime");
        public final static Property Data = new Property(12, String.class, "data", false, "data");
    }

    private DaoSession daoSession;


    public DiaryInfoDao(DaoConfig config) {
        super(config);
    }
    
    public DiaryInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DIARY_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," + // 0: id
                "\"LAYOUT_TYPE\" INTEGER NOT NULL ," + // 1: layoutType
                "\"title\" TEXT," + // 2: title
                "\"text\" TEXT," + // 3: text
                "\"time\" INTEGER NOT NULL ," + // 4: time
                "\"weather\" TEXT," + // 5: weather
                "\"label\" TEXT," + // 6: label
                "\"mood\" TEXT," + // 7: mood
                "\"location\" TEXT," + // 8: location
                "\"imageSize\" INTEGER NOT NULL ," + // 9: imageSize
                "\"recordSize\" INTEGER NOT NULL ," + // 10: recordSize
                "\"createTime\" INTEGER NOT NULL ," + // 11: createTime
                "\"data\" TEXT NOT NULL );"); // 12: data
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DIARY_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, DiaryInfo entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindLong(2, entity.getLayoutType());
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
 
        String text = entity.getText();
        if (text != null) {
            stmt.bindString(4, text);
        }
        stmt.bindLong(5, entity.getTime());
 
        String weather = entity.getWeather();
        if (weather != null) {
            stmt.bindString(6, weather);
        }
 
        String label = entity.getLabel();
        if (label != null) {
            stmt.bindString(7, label);
        }
 
        String mood = entity.getMood();
        if (mood != null) {
            stmt.bindString(8, mood);
        }
 
        String location = entity.getLocation();
        if (location != null) {
            stmt.bindString(9, location);
        }
        stmt.bindLong(10, entity.getImageSize());
        stmt.bindLong(11, entity.getRecordSize());
        stmt.bindLong(12, entity.getCreateTime());
        stmt.bindString(13, entity.getData());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, DiaryInfo entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindLong(2, entity.getLayoutType());
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
 
        String text = entity.getText();
        if (text != null) {
            stmt.bindString(4, text);
        }
        stmt.bindLong(5, entity.getTime());
 
        String weather = entity.getWeather();
        if (weather != null) {
            stmt.bindString(6, weather);
        }
 
        String label = entity.getLabel();
        if (label != null) {
            stmt.bindString(7, label);
        }
 
        String mood = entity.getMood();
        if (mood != null) {
            stmt.bindString(8, mood);
        }
 
        String location = entity.getLocation();
        if (location != null) {
            stmt.bindString(9, location);
        }
        stmt.bindLong(10, entity.getImageSize());
        stmt.bindLong(11, entity.getRecordSize());
        stmt.bindLong(12, entity.getCreateTime());
        stmt.bindString(13, entity.getData());
    }

    @Override
    protected final void attachEntity(DiaryInfo entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public DiaryInfo readEntity(Cursor cursor, int offset) {
        DiaryInfo entity = new DiaryInfo( //
            cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // layoutType
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // title
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // text
            cursor.getLong(offset + 4), // time
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // weather
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // label
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // mood
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // location
            cursor.getInt(offset + 9), // imageSize
            cursor.getInt(offset + 10), // recordSize
            cursor.getLong(offset + 11), // createTime
            cursor.getString(offset + 12) // data
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, DiaryInfo entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setLayoutType(cursor.getInt(offset + 1));
        entity.setTitle(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setText(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setTime(cursor.getLong(offset + 4));
        entity.setWeather(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setLabel(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setMood(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setLocation(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setImageSize(cursor.getInt(offset + 9));
        entity.setRecordSize(cursor.getInt(offset + 10));
        entity.setCreateTime(cursor.getLong(offset + 11));
        entity.setData(cursor.getString(offset + 12));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(DiaryInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(DiaryInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(DiaryInfo entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
