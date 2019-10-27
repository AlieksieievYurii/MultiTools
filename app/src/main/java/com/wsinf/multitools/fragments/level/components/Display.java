package com.wsinf.multitools.fragments.level.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Display implements Painter {

    private int xPosition;
    private int yPosition;
    private String text;

    private int canvasWidth;
    private int canvasHeight;

    private Paint paint;
    private boolean isInitialization = false;

    public Display(int xPosition, int yPosition, String text) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.text = text;

        paint = new Paint();
    }

    private void init(Canvas canvas) {
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();
    }

    @Override
    public void draw(Canvas canvas, int degree) {
        if (!isInitialization) {
            init(canvas);
            isInitialization = true;
        }

        paint.setColor(Color.RED);
        paint.setTextSize(80);

        String stringBuilder = text + " " + degree;
        canvas.drawText(stringBuilder, getXPx(xPosition), getYPx(yPosition), paint);
    }

    private int getXPx(final int p) {
        return (p * this.canvasWidth) / 100;
    }

    private int getYPx(final int p) {
        return (p * this.canvasHeight) / 100;
    }
}
