package com.android.zh.liveranklistgradientexample.util;

import android.content.Context;

public class ViewUtils {
    /**
     * dip换算成像素数量
     */
    public static int dipToPx(Context context, float dip) {
        float density = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return roundUp(dip * context.getResources().getDisplayMetrics().density);
    }

    private static int roundUp(float f) {
        return (int) (0.5f + f);
    }
}
