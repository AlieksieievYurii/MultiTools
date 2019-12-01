package com.wsinf.spytracker.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.wsinf.spytracker.Service;

public class LocationService implements Service, LocationListener
{
    private static final String FIXED = "FIXED";

    private static final int LOCATION_MIN_TIME = 5 * 1000;
    private static final int LOCATION_MID_DISTANCE = 10;

    private LocationManager locationManager;

    private LocationEvent locationEvent;

    public LocationService(@NonNull Context context, @NonNull LocationEvent locationEvent) {
        this.locationEvent = locationEvent;

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onStart() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_MIN_TIME, LOCATION_MID_DISTANCE, this);

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            onLocationChanged(location);
            return;
        }

        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            onLocationChanged(location);
            return;
        }

        onLocationChanged(new Location(FIXED));
    }

    @Override
    public void onStop() {
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (!location.getProvider().equals(FIXED))
            this.locationEvent.onLocationChange(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
