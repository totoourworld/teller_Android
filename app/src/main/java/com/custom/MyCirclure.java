package com.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MyCirclure extends ImageView {

	private Paint paint;
	private float borderWidth= (float) 0.75;
	private int borderColor=0;

	public MyCirclure(Context context) {
		super(context);
	}

	public MyCirclure(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}


	public void setBorderWidth(float borderWidth) {
		this.borderWidth = borderWidth;
		this.requestLayout();
		this.invalidate();
	}

	public void setBorderColor(int borderColor) {
		this.borderColor = borderColor;
		invalidate();
	}

	private Paint paintImage;

	@Override
	protected void onDraw(Canvas canvas) {
		paint = new Paint();
		paint.setColor(borderColor);
		paint.setAntiAlias(true);
		paintImage = new Paint();
		paintImage.setAntiAlias(true);
		int measuredHeight = getMeasuredHeight();
		int measuredWidth = getMeasuredWidth();
		float radius;
		int size;
		if (measuredHeight <= measuredWidth) {
			radius = measuredHeight / 2;
			size = measuredHeight;
		} else {
			radius = measuredWidth / 2;
			size = measuredWidth;
		}

		Bitmap bitmap = drawableToBitmap(getDrawable());
		if (bitmap != null) {
			BitmapShader shader = new BitmapShader(Bitmap.createScaledBitmap(bitmap, size, size, false), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
			paintImage.setShader(shader);
			canvas.drawCircle(measuredWidth / 2, measuredHeight / 2, radius, paint);
			canvas.drawCircle(measuredWidth / 2, measuredHeight / 2, radius -borderWidth, paintImage);
		}
	}

	private Bitmap getBitmap() {
		Drawable background = getDrawable();
		BitmapDrawable bit = (BitmapDrawable) background;
		Bitmap bitmap = bit.getBitmap();
		return bitmap;
	}

	public Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable == null) {
			Drawable background = getBackground();
			if (background instanceof BitmapDrawable) {
				return ((BitmapDrawable) background).getBitmap();
			}
		} else if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}
//		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//		Canvas canvas = new Canvas(bitmap);
//		int width = canvas.getWidth() <= 0 ? 2 : canvas.getWidth();
//		int height = canvas.getHeight() <= 0 ? 2 : canvas.getHeight();
//		drawable.setBounds(0, 0, width, height);
//		drawable.draw(canvas);
		return null;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
	}
}
