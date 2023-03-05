package com.js.phonicdiary.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.format.Formatter;

import androidx.core.content.FileProvider;

import com.js.phonicdiary.DiaryApplication;
import com.js.phonicdiary.bean.FileInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static Uri getUriWithPath(Context context, String filepath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //7.0以上的读取文件uri要用这种方式了
            return FileProvider.getUriForFile(context.getApplicationContext(), "com.js.phonicdiary.fileprovider", new File(filepath));
        } else {
            return Uri.fromFile(new File(filepath));
        }
    }

    /**
     * 获取文本，录音文件，图片存储的路径
     *
     * @return 路径
     */
    public static String getDiaryFilePath() {
        File file = DiaryApplication.getInstance().getExternalFilesDir(null);
        if (file != null) {
            String dir = file.getPath() + File.separator + "diary";
            File diary = new File(dir);
            if (!diary.exists()) {
                if (!diary.exists()) {
                    diary.mkdirs();
                }
            }
            return dir;
        }
        return null;
    }

    /**
     * 获取录音文件，图片存储的路径
     *
     * @return 路径
     */
    public static String getDiaryFilePath(String folder) {
        File file = DiaryApplication.getInstance().getExternalFilesDir(null);
        if (file != null) {
            String dir = file.getPath() + File.separator + "diary" + File.separator + folder;
            File diary = new File(dir);
            if (!diary.exists()) {
                if (!diary.exists()) {
                    diary.mkdirs();
                }
            }
            return dir;
        }
        return null;
    }

    /**
     * 获取备份文件路径
     *
     * @return 路径
     */
    public static String getBackupFilePath() {
        if (isSDCardAvailable()) {
            String name = "DiaryBackup";
            File file = new File(Environment.getExternalStorageDirectory(), name);
            if (file != null) {
                if (!file.exists()) {
                    file.mkdirs();
                }
                if (LogE.isLog) {
                    LogE.e("wbb", "BackupFilePath: " + file.getPath());
                }
                return file.getPath();
            }
        }
        return null;
    }

    /**
     * SD卡是否可用
     */
    public static boolean isSDCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 写入文件
     *
     * @param text 文本内容
     */
    public static void writeFile(String text, long time) {
        // 自定义文件路径
        String rootPath = getDiaryFilePath();// 外部存储路径（根目录）
//        String filePath = rootPath + "/abc/";
        String fileName = DateUtil.formatDateTime(time) + ".txt";
        File file = new File(rootPath, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 写入json文件
     *
     * @param text 文本内容
     */
    public static String writeJsonFile(String text, String path, long time) {
        // 自定义文件路径
        String fileName = DateUtil.formatDateTime(time) + ".json";
        File file = new File(path, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file.getPath();
    }

    /**
     * 删除文件夹下所有数据
     *
     * @param path 文件夹路径
     */
    public static void deleteFile(String path) {
        try {
            File file = new File(path);
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        File f = files[i];
                        deleteFile(f.getPath());
                    }
                }
                file.delete();//如要保留文件夹，只删除文件，请注释这行
            } else if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件夹下所有的json文件及png,jpg文件
     *
     * @param file
     */
    public static void deleteFile(File file, String suffix) {
        if (file.isDirectory()) { // 是否是文件夹
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    if (f != null && (f.getPath().endsWith(suffix))) {
                        // 如果是该后缀文件
                        f.delete();
                    }
                }
            }
        }
    }

    /**
     * 删除文件夹下所有包含该字符的文件
     *
     * @param file
     */
    public static void deleteFile(File file, String containsString, String suffix) {
        if (file.isDirectory()) { // 是否是文件夹
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    if (f != null && (f.getPath().contains(containsString) && f.getPath().endsWith(suffix))) {
                        // 如果是该后缀文件
                        f.delete();
                    }
                }
            }
        }
    }

    /**
     * 获取指定目录内所有文件路径
     *
     * @param dirPath 需要查询的文件目录
     * @param type    查询类型，比如mp3什么的
     */
    public static List<FileInfo> getAllFiles(String dirPath, String type) {
        File f = new File(dirPath);
        if (!f.exists()) {//判断路径是否存在
            return null;
        }

        File[] files = f.listFiles();
        //判断权限
        if (files == null) {
            return null;
        }
        List<FileInfo> fileList = new ArrayList<>();
        for (File file : files) {
            //遍历目录
            if (file.isFile() && file.getName().endsWith(type)) {
                String name = file.getName();
                //获取文件路径
                String filePath = file.getAbsolutePath();
                //获取文件名
                String fileName = file.getName().substring(0, name.length() - 4);
                try {
                    FileInfo fInfo = new FileInfo();
                    fInfo.setFileName(fileName);
                    fInfo.setFilePath(filePath);
                    fInfo.setFileDate(DateUtil.getFormatDataShort(file.lastModified()));
                    String fileSize = Formatter.formatFileSize(DiaryApplication.getInstance(), file.length());
                    fInfo.setFileSize(fileSize);
                    fileList.add(fInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (file.isDirectory()) {
                //查询子目录
                getAllFiles(file.getAbsolutePath(), type);
            }
        }
        return fileList;
    }


    /**
     * 读取文件信息
     *
     * @param inputStream 文件流
     * @return
     */
    public static String getText(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    /**
     * 移动目录
     *
     * @param srcDirName  源目录完整路径
     * @param destDirName 目的目录完整路径
     * @return 目录移动成功返回true，否则返回false
     */
    public static boolean moveDirectory(String srcDirName, String destDirName) {

        File srcDir = new File(srcDirName);
        if (!srcDir.exists() || !srcDir.isDirectory()) {
            return false;
        }

        File destDir = new File(destDirName);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        /**
         * 如果是文件则移动，否则递归移动文件夹。删除最终的空源文件夹
         * 注意移动文件夹时保持文件夹的树状结构
         */
        File[] sourceFiles = srcDir.listFiles();
        for (File sourceFile : sourceFiles) {
            if (sourceFile.isFile()) {
                moveFile(sourceFile.getAbsolutePath(), destDir.getAbsolutePath());
            } else if (sourceFile.isDirectory()) {
                moveDirectory(sourceFile.getAbsolutePath(),
                        destDir.getAbsolutePath() + File.separator + sourceFile.getName());
            }
        }
        return srcDir.delete();
    }


    /**
     * 移动文件
     *
     * @param srcPath  源文件完整路径
     * @param destPath 目的目录完整路径
     * @return 文件移动成功返回true，否则返回false
     */
    public static boolean moveFile(String srcPath, String destPath) {
        File srcFile = new File(srcPath);
        if (!srcFile.exists()) {
            return false;
        }
        if (srcFile.isDirectory()) {
            return false;
        }

        boolean rename = srcFile.renameTo(new File((destPath + File.separator + srcFile.getName())));
        if (!rename) {
            copyFile(srcPath, destPath);
            return srcFile.delete();
        } else {
            return true;
        }
    }


    /**
     * 移动该文件夹下所有指定后缀文件到指定目录中
     *
     * @param srcFileName 源文件完整路径
     * @param destDirName 目的目录完整路径
     * @param suffix
     */
    public static void moveFileWhitSuffix(String srcFileName, String destDirName, String suffix) {
        File file = new File(srcFileName);
        if (file.isDirectory()) { // 是否是文件夹
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    if (f != null && (f.getPath().endsWith(suffix))) {
                        // 如果是该后缀文件
                        // 移动
                        moveFile(f.getPath(), destDirName);
                    }
                }
            }
        }
    }

    /**
     * 复制单个文件
     *
     * @param oldPath$Name String 原文件路径+文件名 如：data/user/0/com.test/files/abc.txt
     * @param newPath$Name String 复制后路径+文件名 如：data/user/0/com.test/cache/abc.txt
     * @return <code>true</code> if and only if the file was copied;
     * <code>false</code> otherwise
     */
    public static boolean copyFile(String oldPath$Name, String newPath$Name) {
        try {
            File oldFile = new File(oldPath$Name);
            if (!oldFile.exists()) {
                return false;
            } else if (!oldFile.isFile()) {
                return false;
            } else if (!oldFile.canRead()) {
                return false;
            }
            FileInputStream fileInputStream = new FileInputStream(oldPath$Name);
            FileOutputStream fileOutputStream = new FileOutputStream(newPath$Name);
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = fileInputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取文件选择器选中的文件路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null) {
                    int column_index = cursor.getColumnIndexOrThrow("_data");
                    if (cursor.moveToFirst()) {
                        String path = cursor.getString(column_index);
                        cursor.close();
                        return path;
                    }
                }
            } catch (Exception e) {
                // Eat it
            }
            if (cursor != null) {
                cursor.close();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

}
