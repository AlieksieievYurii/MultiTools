package com.wsinf.multitools.fragments.level.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class LevelView implements Painter {

    private int canvasWidth;
    private int canvasHeight;

    private int xPosition;
    private int yPosition;
    private int width;
    private int height;
    private Position position;


    private boolean isInitialization = false;
    private RectF rectF;
    private Paint paint;

    private BallPainter ballPainter;

    public LevelView(final int xP,
                     final int yP,
                     final int width,
                     final int height,
                     final Position position) {
        this.xPosition = xP;
        this.yPosition = yP;
        this.position = position;
        this.width = width;
        this.height = height;
        this.paint = new Paint();
    }

    private void init(Canvas canvas) {
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();
        final int x_rect = getXPx(xPosition);
        final int y_rect = getYPx(yPosition);
        if (position == Position.HORIZONTAL) {
            this.rectF = new RectF(x_rect, y_rect, x_rect + getXPx(this.width), y_rect + getYPx(this.height));
            this.ballPainter = new Ball(x_rect, y_rect, getXPx(this.width), getYPx(this.height), position);
        } else if (position == Position.VERTICAL) {
            this.rectF = new RectF(x_rect, y_rect, x_rect + getYPx(this.height), y_rect + getXPx(this.width));
            this.ballPainter = new Ball(x_rect, y_rect, getYPx(this.height), getXPx(this.width), position);
        }

    }

    @Override
    public void draw(Canvas canvas, final int degree) {
        if (!isInitialization) {
            init(canvas);
            isInitialization = true;
        }

        drawLevelLine(canvas);
        ballPainter.draw(canvas, degree);
        drawMarks(canvas);
    }

    private void drawLevelLine(final Canvas canvas) {
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        canvas.drawRoundRect(this.rectF, 5, 5, paint);

    }

    private void drawMarks(final Canvas canvas) {
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        if (position == Position.HORIZONTAL) {
            float rightMark = rectF.centerX() + ballPainter.getBallRadius();
            float leftMark = rectF.centerX() - ballPainter.getBallRadius();

            canvas.drawLine(rightMark, rectF.top, rightMark, rectF.bottom, paint);
            canvas.drawLine(leftMark, rectF.top, leftMark, rectF.bottom, paint);
        } else if (position == Position.VERTICAL) {
            float bottomMark = rectF.centerY() + ballPainter.getBallRadius();
            float topMark = rectF.centerY() - ballPainter.getBallRadius();

            canvas.drawLine(rectF.left, bottomMark, rectF.right, bottomMark, paint);
            canvas.drawLine(rectF.left, topMark, rectF.right, topMark, paint);
        }
    }

    private int getXPx(final int p) {
        return (p * this.canvasWidth) / 100;
    }

    private int getYPx(final int p) {
        return (p * this.canvasHeight) / 100;
    }

}
