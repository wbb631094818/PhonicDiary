package com.js.phonicdiary.utils;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * 修复界面状态栏全屏或者透明时，设置android:fitsSystemWindows="false"时，键盘弹起不会将输入框及底部控件顶起
 * 通过监听键盘动态修改布局高度，滚动。以此适应键盘布局
 *
 * @author 兵兵
 */
public class AndroidBugWorkaround {

    public static AndroidBugWorkaround assistActivity(View content) {
        return new AndroidBugWorkaround(content);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private ViewGroup.LayoutParams frameLayoutParams;
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;

    private AndroidBugWorkaround(View content) {
        if (content != null) {
            mChildOfContent = content;
            onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    possiblyResizeChildOfContent();
                }
            };
            mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
            frameLayoutParams = mChildOfContent.getLayoutParams();
        }
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            //如果两次高度不一致
            //将计算的可视高度设置成视图的高度
            frameLayoutParams.height = usableHeightNow;
            mChildOfContent.requestLayout();//请求重新布局
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        //计算视图可视高度
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom);
    }

//    public void removeOnGlobalLayoutListener() {
//        if (mChildOfContent != null && onGlobalLayoutListener != null) {
//            mChildOfContent.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
//        }
//    }
//    public void addOnGlobalLayoutListener() {
//        if (mChildOfContent != null && onGlobalLayoutListener != null) {
//            mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
//        }
//    }
}
