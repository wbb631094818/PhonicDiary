package com.js.phonicdiary.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 *  录音实体类
 * @author 兵兵
 */
@Entity
public class VoiceInfo implements Serializable {

    private static final long serialVersionUID = 100002L;

    @Id(autoincrement = true)
    private long id;

    /**
     * 对应日记创建时间
     */
    @Property(nameInDb = "createTime")@NotNull
    private long createTime;
    /**
     * 文件创建时间
     */
    @Property(nameInDb = "fileCreateDate")
    private long fileCreateDate;
    /**
     * 文件名
     */
    @Property(nameInDb = "fileName")
    private String fileName;
    /**
     * 文件大小
     */
    @Property(nameInDb = "fileSize")
    private long fileSize;
    /**
     * 文件路径
     */
    @Property(nameInDb = "filePath")
    private String filePath;


    @Generated(hash = 342582725)
    public VoiceInfo(long id, long createTime, long fileCreateDate, String fileName,
            long fileSize, String filePath) {
        this.id = id;
        this.createTime = createTime;
        this.fileCreateDate = fileCreateDate;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.filePath = filePath;
    }

    @Generated(hash = 418546498)
    public VoiceInfo() {
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getFileCreateDate() {
        return fileCreateDate;
    }

    public void setFileCreateDate(long fileCreateDate) {
        this.fileCreateDate = fileCreateDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
