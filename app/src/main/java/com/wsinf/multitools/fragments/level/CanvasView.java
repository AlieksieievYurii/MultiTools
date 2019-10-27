package com.wsinf.multitools.fragments.level;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.wsinf.multitools.fragments.level.components.Display;
import com.wsinf.multitools.fragments.level.components.LevelView;
import com.wsinf.multitools.fragments.level.components.Painter;
import com.wsinf.multitools.fragments.level.components.Position;
import com.wsinf.multitools.fragments.level.components.PositionLevelView;
import com.wsinf.multitools.fragments.level.components.PositionPainter;

import static com.wsinf.multitools.fragments.level.components.Utils.map;

interface DrawThreadCallBack {
    Canvas onDrawView(Canvas canvas);
}

public class CanvasView extends SurfaceView implements SurfaceHolder.Callback, DrawThreadCallBack {

    private Painter xLevelPainter;
    private Painter yLevelPainter;

    private PositionPainter positionPainter;

    private Painter tvXLevel;
    private Painter tvYLevel;

    private int xDegreeP = 0;
    private int yDegreeP = 0;

    private int xDegree = 0;
    private int yDegree = 0;

    private DrawThread drawThread;

    public CanvasView(Context context) {
        super(context);
        xLevelPainter = new LevelView(5, 5, 90, 5, Position.HORIZONTAL);
        yLevelPainter = new LevelView(5, 15, 90, 5, Position.VERTICAL);
        positionPainter = new PositionLevelView(55, 45, 30);
        tvXLevel = new Display(30, 85,"X:");
        tvYLevel = new Display(60, 85, "Y:");

        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getHolder(), this);
        drawThread.setRunning(true);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawThread.setRunning(false);
        boolean retry = true;
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void setValues(int x, int y) {
        xDegree = x;
        yDegree = y;
        xDegreeP = map(x, -60, 60, 0, 100);
        yDegreeP = map(y, -60, 60, 0, 100);
    }


    public Canvas onDrawView(Canvas canvas) {
        canvas.drawARGB(255, 0, 0, 255);

        xLevelPainter.draw(canvas, xDegreeP);
        yLevelPainter.draw(canvas, yDegreeP);

        positionPainter.onDraw(canvas, xDegreeP, yDegreeP);
        tvXLevel.draw(canvas, xDegree);
        tvYLevel.draw(canvas, yDegree);

        return canvas;
    }

    class DrawThread extends Thread {
        private boolean running = false;
        private SurfaceHolder surfaceHolder;
        private DrawThreadCallBack drawThreadCallBack;

        DrawThread(final SurfaceHolder surfaceHolder,
                          final DrawThreadCallBack drawThreadCallBack) {
            this.surfaceHolder = surfaceHolder;
            this.drawThreadCallBack = drawThreadCallBack;
        }

        void setRunning(final boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            Canvas canvas;

            while (running) {
                canvas = null;

                try {
                    canvas = surfaceHolder.lockCanvas(null);
                    if (canvas == null)
                        continue;
                    canvas = drawThreadCallBack.onDrawView(canvas);
                } finally {
                    if (canvas != null)
                        surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
