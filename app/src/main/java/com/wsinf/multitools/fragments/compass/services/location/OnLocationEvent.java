package com.wsinf.multitools.fragments.compass.services.location;

import android.location.Location;

import java.util.Date;

public interface OnLocationEvent {
    void onLocation(final Location location, final Date sunrise, final Date sunset);
}
