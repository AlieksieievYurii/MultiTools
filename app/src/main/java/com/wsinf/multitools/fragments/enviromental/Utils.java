package com.wsinf.multitools.fragments.enviromental;

public class Utils {

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
}
