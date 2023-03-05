package com.js.phonicdiary.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.WindowInsets;

public class SoftInputCoordinatorLayout extends CoordinatorLayout {
    public SoftInputCoordinatorLayout(Context context) {
        super(context);
    }

    public SoftInputCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SoftInputCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            insets.left = 0;
            insets.top = 0;
            insets.right = 0;
        }
        return super.fitSystemWindows(insets);
    }

    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(0, 0, 0, insets.getSystemWindowInsetBottom()));
        } else {
            return insets;
        }
    }
}
