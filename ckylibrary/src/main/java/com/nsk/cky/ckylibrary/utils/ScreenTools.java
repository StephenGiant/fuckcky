package com.nsk.cky.ckylibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;


import java.lang.reflect.Method;

public class ScreenTools {

//	private Context mContext;
	private DisplayMetrics mMetrics = new DisplayMetrics();
	private static DisplayMetrics metrics = new DisplayMetrics();
	public ScreenTools(Context context){
//		this.mContext = context;
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
	}
	/**
	 * 获取宽度像素
	 * @return
	 */
	public int getWidth(){
		return mMetrics.widthPixels;
	}
	
	/**
	 * 获取高度像素
	 * @return
	 */
	public int getHeight(){
		return mMetrics.heightPixels;
	}
	
	/**
	 * 获取屏幕密度
	 * @return
	 */
	public float getDensity(){
		return mMetrics.density;
	}
	/**
	 * dp2px
	 */
	public int dp2px(float dpValue) {
		return (int) (dpValue * getDensity() + 0.5f);
	}
	public int px2dp(float pxValue){
		return (int)(pxValue/getDensity()+0.5f);
	}

	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	public int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}
	public static int getWidth(Context context){
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}

	public static int getHeight(Context context){
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.heightPixels;
	}

	public static int getStatusHeight(Context context){
		int statusBarHeight1 = -1;
//获取status_bar_height资源的ID
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			//根据资源ID获取响应的尺寸值
			statusBarHeight1 = context.getResources().getDimensionPixelSize(resourceId);
		}
		return statusBarHeight1;
	}

	public static int getHeightTotal(Context context){
		int screenHeight = 0;

//		DisplayMetrics metrics = new DisplayMetrics();
		Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
		display.getMetrics(metrics);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2)
		{
			screenHeight = metrics.heightPixels;
		}
		else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.HONEYCOMB_MR2)
		{
			try {

				Method method = display.getClass().getMethod("getRealHeight");
				screenHeight = (Integer) method.invoke(display);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR2) {
			try {
				Method method = display.getClass().getMethod("getRawHeight");
				screenHeight = (Integer) method.invoke(display);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return screenHeight;
	}
	public static float getDensity(Context context){
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.density;
	}
}
