package com.js.phonicdiary.view.listener;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.bilibili.magicasakura.widgets.TintImageView;
import com.js.phonicdiary.R;
import com.js.phonicdiary.utils.LogE;
import com.shizhefei.view.indicator.Indicator.OnTransitionListener;
import com.shizhefei.view.utils.ColorGradient;

/**
 * @author LuckyJayce
 */
public class OnTransitionLayoutListener implements OnTransitionListener {
    private float selectSize = -1;
    private float unSelectSize = -1;
    private ColorGradient gradient;
    private float dFontFize = -1;

    private boolean isPxSize = false;
    public OnTransitionLayoutListener() {
        super();
    }

    public OnTransitionLayoutListener(float selectSize, float unSelectSize, int selectColor, int unSelectColor) {
        super();
        setColor(selectColor, unSelectColor);
        setSize(selectSize, unSelectSize);
    }

    public final OnTransitionLayoutListener setSize(float selectSize, float unSelectSize) {
        isPxSize = false;
        this.selectSize = selectSize;
        this.unSelectSize = unSelectSize;
        this.dFontFize = selectSize - unSelectSize;
        return this;
    }

    public final OnTransitionLayoutListener setValueFromRes(Context context, int selectColorId, int unSelectColorId, int selectSizeId,
                                                            int unSelectSizeId) {
        setColorId(context, selectColorId, unSelectColorId);
        setSizeId(context, selectSizeId, unSelectSizeId);
        return this;
    }

    public final OnTransitionLayoutListener setColorId(Context context, int selectColorId, int unSelectColorId) {
        Resources res = context.getResources();
        setColor(res.getColor(selectColorId), res.getColor(unSelectColorId));
        return this;
    }

    public final OnTransitionLayoutListener setSizeId(Context context, int selectSizeId, int unSelectSizeId) {
        Resources res = context.getResources();
        setSize(res.getDimensionPixelSize(selectSizeId), res.getDimensionPixelSize(unSelectSizeId));
        isPxSize = true;
        return this;
    }

    public final OnTransitionLayoutListener setColor(int selectColor, int unSelectColor) {
        gradient = new ColorGradient(unSelectColor, selectColor, 100);
        return this;
    }

    /**
     * 如果tabItemView 不是目标的TextView，那么你可以重写该方法返回实际要变化的TextView
     *
     * @param tabItemView Indicator的每一项的view
     * @param position    view在Indicator的位置索引
     * @return
     */
    public View getTextView(View tabItemView, int position) {
        return tabItemView;
    }

    @Override
    public void onTransition(View view, int position, float selectPercent) {
        View selectTextView = getTextView(view, position);
        if (selectTextView != null) {
            TextView mText = (TextView) selectTextView.findViewById(R.id.text);
            if (gradient != null) {
                mText.setTextColor(gradient.getColor((int) (selectPercent * 100)));
            }
            if (unSelectSize > 0 && selectSize > 0) {
                if (isPxSize) {
                    mText.setTextSize(TypedValue.COMPLEX_UNIT_PX, unSelectSize + dFontFize * selectPercent);
                } else {
                    mText.setTextSize(unSelectSize + dFontFize * selectPercent);
                }
            }
        }

    }

}
