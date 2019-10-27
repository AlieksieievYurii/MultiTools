package com.wsinf.multitools.fragments.level.components;

import android.graphics.Canvas;

public interface BallPainter {
    void draw(Canvas canvas, final int degree);
    int getBallRadius();
}
