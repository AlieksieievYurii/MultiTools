package com.wsinf.multitools.fragments.level.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import static com.wsinf.multitools.fragments.level.components.Utils.map;

public class PositionLevelView implements PositionPainter {

    private int xPosition;
    private int yPosition;
    private int radiusSurface;
    private int radiusPoint;

    private int canvasWidth;
    private int canvasHeight;

    private Paint paint;
    private boolean isInitialized = false;


    public PositionLevelView(int xPosition, int yPosition, int radiusSurface) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.radiusSurface = radiusSurface;
        paint = new Paint();
    }

    private void init(Canvas canvas) {
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();
        radiusPoint = xPx(5);
    }

    @Override
    public void onDraw(Canvas canvas, int xDegree, int yDegree) {
        if (!isInitialized) {
            init(canvas);
            isInitialized = false;
        }

        drawSurface(canvas);
        drawMarks(canvas);
        drawPoint(canvas, xDegree, yDegree);
        drawCenterMark(canvas);
    }

    private void drawSurface(Canvas canvas) {
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(xPx(xPosition), yPx(yPosition), xPx(radiusSurface), paint);
    }

    private void drawMarks(Canvas canvas) {
        paint.setStrokeWidth(3);
        paint.setColor(Color.BLACK);

        final int radiusSurf = xPx(radiusSurface);
        final int x = xPx(xPosition);
        final int y = yPx(yPosition);

        canvas.drawLine(x - radiusSurf, y, x + radiusSurf, y, paint);
        canvas.drawLine(x, y - radiusSurf, x, y + radiusSurf, paint);

        final int nLines = 2*radiusSurf / 10;
        final int startYp = y + radiusSurf;
        final int startXp = x - radiusSurf;
        paint.setStrokeWidth(2);
        for (int i = 1; i <= nLines; i++) {
            if (i%2 == 0)
                canvas.drawLine(x - 10, startYp - i*10, x + 10, startYp - i*10, paint);
            else
                canvas.drawLine(x - 15, startYp - i*10, x + 15, startYp - i*10, paint);
        }

        for (int i = 1; i <= nLines; i++) {
            if (i%2 == 0)
                canvas.drawLine(startXp + i*10, y - 10, startXp + i*10, y + 10, paint);
            else
                canvas.drawLine(startXp + i*10, y - 15, startXp + i*10, y + 15, paint);
        }
    }

    private void drawCenterMark(Canvas canvas) {
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawCircle(xPx(xPosition), yPx(yPosition), radiusPoint + 10, paint);
    }

    private void drawPoint(Canvas canvas, int xDegree, int yDegree) {
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);

        if (xDegree < 0)
            xDegree = 0;
        else if (xDegree > 100)
            xDegree = 100;

        if (yDegree < 0)
            yDegree = 0;
        else if (yDegree > 100)
            yDegree = 100;

        int drawX = map(xDegree, 0, 100, xPx(xPosition) - xPx(radiusSurface) + radiusPoint, xPx(xPosition) + xPx(radiusSurface) - radiusPoint);
        int drawY = map(yDegree, 0, 100, yPx(yPosition) - xPx(radiusSurface) + radiusPoint, yPx(yPosition) + xPx(radiusSurface) - radiusPoint);


        canvas.drawCircle(drawX, drawY, radiusPoint, paint);
    }

    private int xPx(final int p) {
        return (p * this.canvasWidth) / 100;
    }

    private int yPx(final int p) {
        return (p * this.canvasHeight) / 100;
    }
}
