package com.wsinf.multitools.fragments.compass.services.compass;

import android.location.Location;

import com.wsinf.multitools.fragments.compass.services.Service;

public interface CompassServiceInterface extends Service {
    void setLocation(final Location location);
}
