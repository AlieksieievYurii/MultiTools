package com.wsinf.multitools.fragments.compass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.wsinf.multitools.R;

public class CompassView extends View {

    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int width = 0;
    private int height = 0;
    private Matrix matrix;
    private Bitmap bitmap;
    private Bitmap pointer;
    private float bearing;

    private boolean isInitialized = false;

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
        pointer = Utils.getAsBitmap(getContext(), R.drawable.ic_navigation);
    }

    void onAzimuth(final int azimuth) {
        this.bearing = azimuth;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private void initializeBitmaps() {
        final int bitmapWidth = bitmap.getWidth();
        final int bitmapHeight = bitmap.getHeight();
        final int canvasWidth = getWidth();
        final int canvasHeight = getHeight();

        if (bitmapWidth > canvasWidth || bitmapHeight > canvasHeight) {
            bitmap = Bitmap.createScaledBitmap(bitmap,
                    (int) (bitmapWidth * 0.60),
                    (int) (bitmapHeight * 0.60),
                    true);
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInitialized) {
            initializeBitmaps();
            isInitialized = true;
        }

        drawCompass(canvas);
        drawPointer(canvas);
        drawAzimuth(canvas);
    }

    @SuppressLint("DefaultLocale")
    private String getAzimuth() {
        int range = (int) (bearing / (360f / 16f));
        String dirTxt = "";

        if (range == 15 || range == 0)
            dirTxt = "N";
        else if (range == 1 || range == 2)
            dirTxt = "NE";
        else if (range == 3 || range == 4)
            dirTxt = "E";
        else if (range == 5 || range == 6)
            dirTxt = "SE";
        else if (range == 7 || range == 8)
            dirTxt = "S";
        else if (range == 9 || range == 10)
            dirTxt = "SW";
        else if (range == 11 || range == 12)
            dirTxt = "W";
        else if (range == 13 || range == 14)
            dirTxt = "NW";

        return String.format("%d%c %s", (int) bearing, 176, dirTxt);
    }

    private void drawAzimuth(final Canvas canvas) {
        final int bitmapCenterY = bitmap.getHeight() / 2;
        final int parentCenterX = width / 2;
        paint.reset();
        paint.setColor(Color.WHITE);
        paint.setTextSize(120);
        final String text = getAzimuth();
        canvas.drawText(text, parentCenterX - (text.length() * paint.getTextSize()) / 4, bitmapCenterY + 250, paint);
    }

    private void drawCompass(final Canvas canvas) {
        final int bitmapCenterX = bitmap.getWidth() / 2;
        final int bitmapCenterY = bitmap.getHeight() / 2;
        final int parentCenterX = width / 2;
        final int parentCenterY = height / 2;
        final int centerX = parentCenterX - bitmapCenterX;
        final int centerY = parentCenterY - bitmapCenterY;

        final int rotation = (int) (360 - bearing);
        paint.reset();
        matrix.reset();
        matrix.setRotate(rotation, bitmapCenterX, bitmapCenterY);
        matrix.postTranslate(centerX, centerY);
        canvas.drawBitmap(bitmap, matrix, paint);
    }

    private void drawPointer(final Canvas canvas) {
        final int parentCenterX = width / 2;
        final int bitmapCenterY = bitmap.getHeight() / 2;
        paint.reset();
        matrix.reset();
        matrix.postTranslate(parentCenterX - pointer.getWidth() / 2, bitmapCenterY - pointer.getHeight() + 30);
        canvas.drawBitmap(pointer, matrix, paint);
    }
}
