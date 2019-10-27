package com.wsinf.multitools.fragments.level.components;

public class Utils {

    private Utils() {}

    public static int map(int value, int in_min, int in_max, int out_min, int out_max) {
        return (value - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}
