package com.js.phonicdiary.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;

@Entity
public class PhotoInfo implements Serializable {

    private static final long serialVersionUID = 100003L;

    @Id(autoincrement = true)
    private long id;
    @Property(nameInDb = "name")
    private String name;//图片名称
    @Property(nameInDb = "path")
    private String path;//图片全路径
    @Property(nameInDb = "type")
    private String type;//图片类型
    @Property(nameInDb = "width")
    private int width;//图片宽度
    @Property(nameInDb = "height")
    private int height;//图片高度
    @Property(nameInDb = "size")
    private long size;//图片文件大小，单位：Bytes
    @Property(nameInDb = "duration")
    private long duration;//视频时长，单位：毫秒
    @Property(nameInDb = "time")
    private long time;//图片拍摄的时间戳,单位：毫秒

    @NotNull
    @Property(nameInDb = "createTime")
    private long createTime;


    @Generated(hash = 395536510)
    public PhotoInfo(long id, String name, String path, String type, int width,
            int height, long size, long duration, long time, long createTime) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.type = type;
        this.width = width;
        this.height = height;
        this.size = size;
        this.duration = duration;
        this.time = time;
        this.createTime = createTime;
    }

    @Generated(hash = 2143356537)
    public PhotoInfo() {
    }

   
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }


}
