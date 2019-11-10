package com.wsinf.multitools.fragments.compass;

class Utils {
    private static final float ALPHA = 0.2f;

    static float[] lowPassFilter(float[] input, float[] output) {
        if (output == null)
            return input;

        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }
}
