package com.wsinf.multitools.fragments.compass.services.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.wsinf.multitools.fragments.compass.services.Service;

import org.shredzone.commons.suncalc.SunTimes;

import java.util.Calendar;

public class LocationService implements Service, LocationListener {

    public static final String FIXED = "FIXED";

    private static final int LOCATION_MIN_TIME = 30 * 1000;
    private static final int LOCATION_MID_DISTANCE = 10;

    private OnLocationEvent onLocationEvent;
    private Context context;

    private LocationManager locationManager;


    public LocationService(OnLocationEvent onLocationEvent, Context context) {
        this.onLocationEvent = onLocationEvent;
        this.context = context;

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onStart() {
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            throw new SecurityException("Not permission to user location!");

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
        final SunTimes sunTime = SunTimes.compute()
                .on(Calendar.getInstance().getTime())
                .at(location.getLatitude(), location.getLongitude())
                .execute();

        onLocationEvent.onLocation(location, sunTime.getRise(), sunTime.getSet());
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
