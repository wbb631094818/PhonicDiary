package com.js.phonicdiary.manager;


import com.js.phonicdiary.utils.FileUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 文件操作类
 * @author 兵兵
 */
public class FileManager {


    /**
     * 写入文本时间
     *
     * @param text 文本
     * @param time 时间
     */
    public static void writeTextFile(final String text, final long time) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                FileUtils.writeFile(text, time);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

}
