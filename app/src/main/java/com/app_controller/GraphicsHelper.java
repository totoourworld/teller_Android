package com.app_controller;

import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Display;

public class GraphicsHelper {

	public static int height;
	public static int width;
	private static float T_HEIGHT = 480.0f;
	private static float T_WIDTH = 800.0f;
	private static DisplayMetrics outMetrics;

	public GraphicsHelper(FragmentActivity activity) {
		Display metrics = activity.getWindowManager().getDefaultDisplay();
		height = metrics.getHeight();
		width = metrics.getWidth();
		outMetrics = new DisplayMetrics();
		metrics.getMetrics(outMetrics);

	}

	public static int getDpX(int x) {
		return (int) (x * (width / T_WIDTH));
	}

	public static int getDpY(int y) {
		return (int) (y * (height / T_HEIGHT));
	}

	public static int getDp(int y) {
		float fpixels = outMetrics.density * y;
		int pixels = (int) (fpixels + 0.5f);
		return pixels;
	}
}
