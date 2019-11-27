package com.wsinf.multitools.fragments.enviromental;

import android.graphics.Color;

class Utils {

    private static final double M = 17.62;
    private static final double TN = 243.12;
    private static final double A = 6.112;

    static double calculateDewPoint(float temperature, float humidity) {
        final double a = (M * temperature) / (TN + temperature);
        final double ln = Math.log(humidity / 100);
        final double top = ln + a;
        final double bottom = M - (ln + a);

        return TN * (top/bottom);
    }

    static double calculateAbsoluteHumidity(float temperature, float humidity) {
        final double top = (humidity/100) * A * Math.exp((M*temperature)/(TN + temperature));
        final double bottom = 273.15 + temperature;

        return 216.7 * (top/bottom);
    }

    static int map(int x, int in_min, int in_max, int out_min, int out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}
