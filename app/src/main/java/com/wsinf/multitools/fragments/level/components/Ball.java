package com.wsinf.multitools.fragments.level.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Ball implements BallPainter {


    private int xLevelPosition;
    private int yLevelPosition;

    private int levelWidth;
    private int levelHeight;
    private Position levelPosition;

    private Paint paint;

    private int radius;

    Ball(int xLevelPosition,
         int yLevelPosition,
         int levelWidth,
         int levelHeight,
         Position levelPosition) {
        this.xLevelPosition = xLevelPosition;
        this.yLevelPosition = yLevelPosition;
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
        this.levelPosition = levelPosition;

        if (levelPosition == Position.HORIZONTAL)
            this.radius = levelHeight / 2;
        else if (levelPosition == Position.VERTICAL)
            this.radius = levelWidth / 2;

        this.paint = new Paint();
        this.paint.setStrokeWidth(1);
        this.paint.setColor(Color.GREEN);
    }

    @Override
    public void draw(Canvas canvas, int degree) {
        if (degree < 0)
            degree = 0;
        else if (degree > 100)
            degree = 100;

        int drawX = 0;
        int drawY = 0;

        if (levelPosition == Position.HORIZONTAL) {
            drawX = xLevelPosition + radius + getXPoint(degree);
            drawY = yLevelPosition + radius;
        } else if (levelPosition == Position.VERTICAL) {
            drawY = yLevelPosition + radius + getHeight(degree);
            drawX = xLevelPosition + radius;
        }
        canvas.drawCircle(drawX, drawY, radius, paint);
    }

    @Override
    public int getBallRadius() {
        return radius;
    }

    private int getXPoint(final int p) {
        return (p * (this.levelWidth - radius * 2)) / 100;
    }

    private int getHeight(final int p) {
        return (p * (this.levelHeight - radius * 2)) / 100;
    }
}
