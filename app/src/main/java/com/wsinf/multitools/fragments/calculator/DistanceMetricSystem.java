package com.wsinf.multitools.fragments.calculator;

import androidx.annotation.NonNull;

public enum DistanceMetricSystem {
    KM("Km"),
    M("M"),
    CM("Cm");

    private String inString;

    DistanceMetricSystem(String value) {
        this.inString = value;
    }

    @NonNull
    @Override
    public String toString() {
        return this.inString;
    }
}
