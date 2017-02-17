package com.eraare.satellitemenu;

import android.content.Context;

/**
 * @file DisplayUtil.java
 * @author Leo
 * @version 1
 * @detail 显示中用到的工具类
 * @since 2017/2/17
 */

/**
 * 文件名：DisplayUtil.java
 * 作  者：Leo
 * 版  本：1
 * 日  期：2017/2/17
 * 描  述：显示中用到的工具类
 */
public final class DisplayUtils {
    /**
     * px转dip
     * 原理 px / dip = scale
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * dip转px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static float dip2px(Context context, int dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dipValue * scale + 0.5f;
    }

    /**
     * px转sp
     * 原理 px / dip = scale
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param context
     * @param spValue
     * @return
     */
    public static float sp2px(Context context, int spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return spValue * scale + 0.5f;
    }
}
