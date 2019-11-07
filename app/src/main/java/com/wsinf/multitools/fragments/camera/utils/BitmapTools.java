package com.wsinf.multitools.fragments.camera.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import java.util.Random;

public class BitmapTools {

    private BitmapTools() {}

    public static Bitmap[][] separate(final Bitmap bitmap, final int count) {
        final Bitmap[][] bitmaps = new Bitmap[count][count];
        final int width = bitmap.getWidth() / count;
        final int height = bitmap.getHeight() / count;

        for (int y = 0; y < count; y++)
            for (int x = 0; x < count; x++)
                bitmaps[y][x] = Bitmap.createBitmap(bitmap, x * width, y * height, width, height);

        return bitmaps;
    }

    public static Bitmap blindBitmaps(final Bitmap[][] bitmaps) {
        final int widthBitmapPiece = bitmaps[0][0].getWidth();
        final int heightBitmapPiece = bitmaps[0][0].getHeight();
        final int widthBitMap = widthBitmapPiece * bitmaps[0].length;
        final int heightBitMap = heightBitmapPiece * bitmaps.length;
        final Bitmap bitmap = Bitmap.createBitmap(widthBitMap, heightBitMap, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.BLUE);

        for (int y = 0; y < bitmaps.length; y++)
            for (int x = 0; x < bitmaps[0].length; x++)
                canvas.drawBitmap(bitmaps[y][x], widthBitmapPiece * x, heightBitmapPiece * y, null);

        return bitmap;
    }

    public static <T> void shuffle(T[][] array) {
        final Random random = new Random();
        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array[y].length; x++) {
                final int randomCoordinateY = random.nextInt(array.length);
                final int randomCoordinateX = random.nextInt(array[y].length);

                final T buffer = array[randomCoordinateY][randomCoordinateX];
                array[randomCoordinateY][randomCoordinateX] = array[y][x];
                array[y][x] = buffer;
            }
        }
    }
}
