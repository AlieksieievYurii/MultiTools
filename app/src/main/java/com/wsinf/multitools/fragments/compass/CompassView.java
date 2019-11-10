package com.wsinf.multitools.fragments.compass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.wsinf.multitools.R;

public class CompassView extends View {

    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int width = 0;
    private int height = 0;
    private Matrix matrix;
    private Bitmap bitmap;
    private float bearing;

    public CompassView(Context context) {
        super(context);
        initialize();
    }

    public CompassView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {
        matrix = new Matrix();
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.compass);
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int bitmapWidth = bitmap.getWidth();
        final int bitmapHeight = bitmap.getHeight();
        final int canvasWidth = getWidth();
        final int canvasHeight = getHeight();

        if (bitmapWidth > canvasWidth || bitmapHeight > canvasHeight) {
            bitmap = Bitmap.createScaledBitmap(bitmap,
                    (int) (bitmapWidth * 0.40),
                    (int) (bitmapHeight * 0.40),
                    true);
        }

        final int bitmapX = bitmap.getWidth() / 2;
        final int bitmapY = bitmap.getHeight() / 2;
        final int parentX = width / 2;
        final int parentY = height / 2;
        final int centerX = parentX - bitmapX;
        final int centerY = parentY - bitmapY;

        final int rotation = (int) (360 - bearing);

        matrix.reset();
        matrix.setRotate(rotation, bitmapX, bitmapY);
        matrix.postTranslate(centerX, centerY);
        canvas.drawBitmap(bitmap, matrix, paint);
    }
}
