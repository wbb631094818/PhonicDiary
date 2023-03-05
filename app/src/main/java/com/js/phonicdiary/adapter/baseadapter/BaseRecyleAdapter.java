package com.js.phonicdiary.adapter.baseadapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.ArrayList;

/**
 * recyclerview适配器的封装
 * Created by mamba on 2018/3/6.
 */

public abstract class BaseRecyleAdapter<T> extends RecyclerView.Adapter {


    protected ArrayList<T> list;
    protected LayoutInflater inflater;

    public BaseRecyleAdapter(Context context) {
        this.list = new ArrayList<T>();
        this.inflater = LayoutInflater.from(context);
    }

    /**
     * 添加数据到头部
     */
    public void addDataToTop(T t, boolean isclear) {
        if (t != null) {
            if (isclear) {
                list.clear();
            }
            list.add(0, t);
        }
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    /**
     * 获取数据集合
     * @return 返回该集合
     */
    public ArrayList<T> getAdapterData() {
        return list;

    }

    /**
     *  返回数据集合的数量
     * @return 数量
     */
    public int getListSize(){
        if (list!=null){
            return this.list.size();
        }
       return 0;
    }

    /**
     * 清除所有数据
     */
    public void clearAll() {
        if (list != null) {
            list.clear();
        }

    }

    /**
     * 删除指定数据
     * @param i
     */
    public void clearData(int i) {
        if (list != null && list.size() > i) {
            list.remove(i);
        }
    }

    /**
     * 添加数据到头部
     */
    public void addDataTop(T t, boolean isclear) {
        if (t != null) {
            if (isclear) {
                list.clear();
            }
            list.add(0, t);
        }
    }

    /**
     * 添加新数据集合到头部
     *
     * @param newlist 新数据集合
     * @param isclear 是否清空
     */
    public void addDataListTop(ArrayList<T> newlist, boolean isclear) {
        if (newlist != null) {
            if (isclear) {
                list.clear();
            }
            list.addAll(0, newlist);
        }
    }

    /**
     * 添加新数据集合
     *
     * @param newlist
     * @param isclear
     */
    public void addDataList(ArrayList<T> newlist, boolean isclear) {
        if (newlist != null) {
            if (isclear) {
                list.clear();
            }
            list.addAll(newlist);
        }
    }

    /**
     * 添加新数据集合
     *
     * @param newlist
     * @param isclear
     */
    public void addDataList(ArrayList<T> newlist, int i, boolean isclear) {
        if (newlist != null) {
            if (isclear) {
                list.clear();
            }
            if (list.size() > 0) {
                list.addAll(i, newlist);
            } else {
                list.addAll(newlist);
            }

        }
    }

    /**
     * 添加一个数据
     *
     * @param t
     * @param isclear
     */
    public void appendData(T t, boolean isclear) {
        if (t == null) {
            return;
        }
        if (isclear) {
            list.clear();
        }
        list.add(t);
    }

    /**
     * 添加一个数据到头部
     * @param t 该数据
     * @param isclear 是否删除
     */
    public void appendDataTop(T t, boolean isclear) {
        if (t == null) {
            return;
        }
        if (isclear) {
            list.clear();
        }
        list.add(0, t);
    }

    /**
     * 添加一个数据
     * @param t 该数据
     * @param isclear 是否删除
     */
    public void appendData(T t, int i, boolean isclear) {
        if (t == null) {
            return;
        }
        if (isclear) {
            list.clear();
        }
        if (list.size() > i) {
            list.add(i, t);
        } else {
            list.add(t);
        }
    }

}
