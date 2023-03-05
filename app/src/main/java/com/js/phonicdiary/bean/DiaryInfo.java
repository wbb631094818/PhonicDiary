package com.js.phonicdiary.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.DaoException;
import com.js.phonicdiary.db.DaoSession;
import com.js.phonicdiary.db.PhotoInfoDao;
import com.js.phonicdiary.db.VoiceInfoDao;
import com.js.phonicdiary.db.DiaryInfoDao;

/**
 * 日记信息
 *
 * @author 兵兵
 */
@Entity
public class DiaryInfo implements Serializable {

    private static final long serialVersionUID = 100001L;


    // 日记布局
    public final static int DIARY_LAYOUT = 0;
    // 标题布局
    public final static int TITLE_LAYOUT = 1;
    // 头部布局
    public final static int TOP_LAYOUT = 2;

    @Id(autoincrement = true)
    private long id;

    /**
     * 布局类型（ 标题布局还是日记布局）
     */
    private int layoutType;

    /**
     * 布局标题，（这周的，上周的，这个月的，今年的，） 或者日记标题
     */
    @Property(nameInDb = "title")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getWeather() {
        return this.weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMood() {
        return this.mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getImageSize() {
        return this.imageSize;
    }

    public void setImageSize(int imageSize) {
        this.imageSize = imageSize;
    }

    public int getRecordSize() {
        return this.recordSize;
    }

    public void setRecordSize(int recordSize) {
        this.recordSize = recordSize;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 807132452)
    public List<VoiceInfo> getVoiceInfos() {
        if (voiceInfos == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            VoiceInfoDao targetDao = daoSession.getVoiceInfoDao();
            List<VoiceInfo> voiceInfosNew = targetDao
                    ._queryDiaryInfo_VoiceInfos(id);
            synchronized (this) {
                if (voiceInfos == null) {
                    voiceInfos = voiceInfosNew;
                }
            }
        }
        return voiceInfos;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1294165174)
    public synchronized void resetVoiceInfos() {
        voiceInfos = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1855527429)
    public List<PhotoInfo> getPhotoInfos() {
        if (photoInfos == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PhotoInfoDao targetDao = daoSession.getPhotoInfoDao();
            List<PhotoInfo> photoInfosNew = targetDao
                    ._queryDiaryInfo_PhotoInfos(id);
            synchronized (this) {
                if (photoInfos == null) {
                    photoInfos = photoInfosNew;
                }
            }
        }
        return photoInfos;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 333940102)
    public synchronized void resetPhotoInfos() {
        photoInfos = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 256551761)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDiaryInfoDao() : null;
    }


    /**
     * 日记正文
     */
    @Property(nameInDb = "text")
    private String text;

    /**
     * 日记生成时间
     */
    @Property(nameInDb = "time")
    private long time;

    /**
     * 天气
     */
    @Property(nameInDb = "weather")
    private String weather;

    /**
     * 标签
     */
    @Property(nameInDb = "label")
    private String label;

    /**
     * 心情
     */
    @Property(nameInDb = "mood")
    private String mood;

    /**
     * 地址
     */
    @Property(nameInDb = "location")
    private String location;

    /**
     * 图片数量
     */
    @Property(nameInDb = "imageSize")
    private int imageSize;

    /**
     * 录音数量
     */
    @Property(nameInDb = "recordSize")
    private int recordSize;

    @NotNull
    @Property(nameInDb = "createTime")
    private long createTime;

    /**
     *  日期
     */
    @NotNull
    @Property(nameInDb = "data")
    private String data;

    /**
     * 每个父亲对应的孩子列表
     */
    @ToMany(referencedJoinProperty = "createTime")
    private List<VoiceInfo> voiceInfos;
    @ToMany(referencedJoinProperty = "createTime")
    private List<PhotoInfo> photoInfos;


    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;


    /** Used for active entity operations. */
    @Generated(hash = 731371862)
    private transient DiaryInfoDao myDao;

    @Generated(hash = 1402166699)
    public DiaryInfo(long id, int layoutType, String title, String text, long time,
            String weather, String label, String mood, String location,
            int imageSize, int recordSize, long createTime, @NotNull String data) {
        this.id = id;
        this.layoutType = layoutType;
        this.title = title;
        this.text = text;
        this.time = time;
        this.weather = weather;
        this.label = label;
        this.mood = mood;
        this.location = location;
        this.imageSize = imageSize;
        this.recordSize = recordSize;
        this.createTime = createTime;
        this.data = data;
    }

    @Generated(hash = 1385338142)
    public DiaryInfo() {
    }




}
