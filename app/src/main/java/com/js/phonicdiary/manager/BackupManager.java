package com.js.phonicdiary.manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.js.phonicdiary.DiaryApplication;
import com.js.phonicdiary.R;
import com.js.phonicdiary.bean.DiaryInfo;
import com.js.phonicdiary.bean.PhotoInfo;
import com.js.phonicdiary.bean.VoiceInfo;
import com.js.phonicdiary.db.DiaryInfoDao;
import com.js.phonicdiary.utils.DateUtil;
import com.js.phonicdiary.utils.FileUtils;
import com.js.phonicdiary.utils.LogE;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.progress.ProgressMonitor;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 备份相关管理
 */
public class BackupManager {

    /**
     * 自动备份数据
     */
    public static void autoBackupData() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                List<DiaryInfo> diaryInfos = DiaryApplication.getInstance().getDaoSession().getDiaryInfoDao()
                        .queryBuilder().list();
                String str = new Gson().toJson(diaryInfos);
                long time = System.currentTimeMillis();
                String path = FileUtils.writeJsonFile(str, FileUtils.getBackupFilePath(), time);
                List<File> infoList = new ArrayList<>();
                ArrayList<PhotoInfo> photoInfoList = (ArrayList<PhotoInfo>) DiaryApplication.getInstance().getDaoSession().getPhotoInfoDao().queryBuilder().list();
                File file = null;
                for (PhotoInfo photoInfo : photoInfoList) {
                    file = new File(photoInfo.getPath());
                    if (file.exists()) {
                        infoList.add(file);
                    }
                }
                // 录音相关
                List<VoiceInfo> voiceList = DiaryApplication.getInstance().getDaoSession().getVoiceInfoDao().loadAll();
                for (VoiceInfo voiceInfo : voiceList) {
                    file = new File(voiceInfo.getFilePath());
                    if (file.exists()) {
                        infoList.add(file);
                    }
                }
                // 数据库
                infoList.add(new File(path));
                String zipPath = FileUtils.getBackupFilePath();
                if (zipPath != null) {
                    FileUtils.deleteFile(new File(zipPath), "auto_", ".zip");
                    String fileName = "auto_" + DateUtil.formatDateTime(time) + ".zip";
                    File zipFile = new File(zipPath, fileName);
                    ZipFile zipFile1 = new ZipFile(zipFile);
                    zipFile1.addFiles(infoList);
                    FileUtils.deleteFile(new File(zipPath), ".json");
                }

                if (LogE.isLog) {
                    LogE.e("wbb", "自动备份完成...");
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

    /**
     * 备份数据
     *
     * @param observe 回调
     */
    public static void backupData(Observer<? super String> observe) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                List<DiaryInfo> diaryInfos = DiaryApplication.getInstance().getDaoSession().getDiaryInfoDao()
                        .queryBuilder().list();
                String str = new Gson().toJson(diaryInfos);
                long time = System.currentTimeMillis();
                String path = FileUtils.writeJsonFile(str, FileUtils.getBackupFilePath(), time);
                List<File> infoList = new ArrayList<>();
                ArrayList<PhotoInfo> photoInfoList = (ArrayList<PhotoInfo>) DiaryApplication.getInstance().getDaoSession().getPhotoInfoDao().queryBuilder().list();
                File file = null;
                for (PhotoInfo photoInfo : photoInfoList) {
                    file = new File(photoInfo.getPath());
                    if (file.exists()) {
                        infoList.add(file);
                    }
                }
                // 录音相关
                List<VoiceInfo> voiceList = DiaryApplication.getInstance().getDaoSession().getVoiceInfoDao().loadAll();
                for (VoiceInfo voiceInfo : voiceList) {
                    file = new File(voiceInfo.getFilePath());
                    if (file.exists()) {
                        infoList.add(file);
                    }
                }
                // 数据库
                infoList.add(new File(path));
                String zipPath = FileUtils.getBackupFilePath();
                if (zipPath != null) {
                    String fileName = DateUtil.formatDateTime(time) + ".zip";
                    File zipFile = new File(zipPath, fileName);
                    ZipFile zipFile1 = new ZipFile(zipFile);
                    zipFile1.addFiles(infoList);
//                    zipFile1.setRunInThread(true);
//                    zipFile.addFolder("/some/folder");
//                    ProgressMonitor progressMonitor = zipFile1.getProgressMonitor();
//                    while (!progressMonitor.getState().equals(ProgressMonitor.State.READY)) {
//                        if (LogE.isLog) {
//                            LogE.e("wbb", "Percentage done: " + progressMonitor.getPercentDone());
//                            LogE.e("wbb", "Current file: " + progressMonitor.getFileName());
//                            LogE.e("wbb", "Current task: " + progressMonitor.getCurrentTask());
//                        }
//                        Thread.sleep(100);
//                    }
                    FileUtils.deleteFile(new File(zipPath), ".json");
                }

                if (LogE.isLog) {
                    LogE.e("wbb", "转换完成...");
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observe);

    }

    /**
     * 恢复数据
     *
     * @param observe 回调
     */
    public static void recoverBackupData(String path, Observer<? super String> observe) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                // 判断是不是zip文件
                if (path == null || !path.endsWith(".zip")) {
                    emitter.onError(new Throwable(DiaryApplication.getInstance().getString(R.string.no_zip_file)));
                    return;
                }
                ZipFile zipFile = new ZipFile(path);
                if (!zipFile.isValidZipFile()) {
                    emitter.onError(new Throwable(DiaryApplication.getInstance().getString(R.string.no_zip_file)));
                    return;
                }
                // 判断是不是我的日记备份文件
                String jsonName = new File(path).getName().replace("zip", "json");
                if (LogE.isLog) {
                    LogE.e("wbb", "jsonName: " + jsonName);
                }
                FileHeader fileHeader = zipFile.getFileHeader(jsonName);
                if (fileHeader == null) {
                    emitter.onError(new Throwable(DiaryApplication.getInstance().getString(R.string.no_zip_file)));
                    return;
                }
                // 是我的开始解压
                String diaryUrl = FileUtils.getDiaryFilePath();
                if (diaryUrl == null) {
                    emitter.onError(new Throwable(DiaryApplication.getInstance().getString(R.string.load_file_erro)));
                    return;
                }
                // 解压
                zipFile.extractAll(FileUtils.getDiaryFilePath());
                String jsonText = FileUtils.getText(zipFile.getInputStream(fileHeader));
                Type type = new TypeToken<ArrayList<DiaryInfo>>() {
                }.getType();
                ArrayList<DiaryInfo> diaryInfos = new Gson().fromJson(jsonText, type);
                if (diaryInfos != null && diaryInfos.size() > 0) {
                    // 插入数据
                    for (DiaryInfo diaryInfo : diaryInfos) {
                        DiaryApplication.getInstance().getDaoSession().getDiaryInfoDao().insertOrReplace(diaryInfo);
                        List<VoiceInfo> voiceInfos = diaryInfo.getVoiceInfos();
                        for (VoiceInfo voiceinfo : voiceInfos) {
//                            // 移动文件
//                            boolean isMove = FileUtils.moveFile(voiceinfo.getFilePath(), FileUtils.getDiaryFilePath("voice"));
//                            if (isMove) {
                            voiceinfo.setFilePath(FileUtils.getDiaryFilePath() + File.separator + voiceinfo.getFileName());
                            DiaryApplication.getInstance().getDaoSession().getVoiceInfoDao().insertOrReplace(voiceinfo);
//                            }
                        }
                        List<PhotoInfo> photoInfos = diaryInfo.getPhotoInfos();
                        for (PhotoInfo photoInfo : photoInfos) {
//                            // 移动文件
//                            boolean isMove = FileUtils.moveFile(photoInfo.getPath(), FileUtils.getDiaryFilePath("photo"));
//                            if (isMove) {
                            photoInfo.setPath(FileUtils.getDiaryFilePath() + File.separator + photoInfo.getName());
                            DiaryApplication.getInstance().getDaoSession().getPhotoInfoDao().insertOrReplace(photoInfo);
//                            }
                        }

                    }
                    // 删除json
                    FileUtils.deleteFile(new File(FileUtils.getDiaryFilePath()), ".json");
                    if (LogE.isLog) {
                        LogE.e("wbb", "转换完成...");
                    }
                    emitter.onComplete();
                } else {
                    emitter.onError(new Throwable(DiaryApplication.getInstance().getString(R.string.load_file_erro)));
                    return;
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observe);

    }


    /**
     * 备份恢复后将媒体文件整理下
     */
    public static void fileMove() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                // 判断是不是zip文件
                List<PhotoInfo> photoInfos = DiaryApplication.getInstance().getDaoSession().getPhotoInfoDao().loadAll();
                if (photoInfos != null && photoInfos.size() > 0) {
                    // 插入数据
                    for (PhotoInfo photoInfo : photoInfos) {
                        String path = photoInfo.getPath();
                        String diaryFilePath = FileUtils.getDiaryFilePath();
                        if (diaryFilePath != null && path.contains(diaryFilePath)) {
                            // 移动文件
                            boolean isMove = FileUtils.moveFile(photoInfo.getPath(), FileUtils.getDiaryFilePath("photo"));
                            if (isMove) {
                                photoInfo.setPath(FileUtils.getDiaryFilePath("photo") + File.separator + photoInfo.getName());
                                DiaryApplication.getInstance().getDaoSession().getPhotoInfoDao().insertOrReplace(photoInfo);
                            }
                        }
                    }
                }
                List<VoiceInfo> voiceInfos = DiaryApplication.getInstance().getDaoSession().getVoiceInfoDao().loadAll();
                for (VoiceInfo voiceinfo : voiceInfos) {
//                            // 移动文件
                    boolean isMove = FileUtils.moveFile(voiceinfo.getFilePath(), FileUtils.getDiaryFilePath("voice"));
                    if (isMove) {
                        voiceinfo.setFilePath(FileUtils.getDiaryFilePath("voice") + File.separator + voiceinfo.getFileName());
                        DiaryApplication.getInstance().getDaoSession().getVoiceInfoDao().insertOrReplace(voiceinfo);
                    }
                }
                // 删除json
                String diaryFilePath = FileUtils.getDiaryFilePath();
                if (diaryFilePath != null) {
                    FileUtils.deleteFile(new File(FileUtils.getDiaryFilePath()), ".json");
                }
                if (LogE.isLog) {
                    LogE.e("wbb", "转换完成...");
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

}
