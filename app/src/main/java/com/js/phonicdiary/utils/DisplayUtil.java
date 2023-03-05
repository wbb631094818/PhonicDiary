package com.js.phonicdiary.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;


public class DisplayUtil {
	// 将px值转换为dip或dp值，保证尺寸大小不变

	public static int dip2px(Context context, float dipValue) {

		final float scale = context.getResources().getDisplayMetrics().density;

		return (int) (dipValue * scale + 0.5f);

	}

	public static int sp2px(Context context, float spValue) {

		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;

		return (int) (spValue * fontScale + 0.5f);

	}

	// 设置view 的margin值
	public static void setMargins(View v, int left, int top, int right, int bottom) {
		if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
			ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
			p.setMargins(left, top, right, bottom);
			v.requestLayout();
		}
	}



}
