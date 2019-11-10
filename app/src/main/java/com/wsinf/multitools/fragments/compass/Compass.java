package com.wsinf.multitools.fragments.compass;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wsinf.multitools.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Objects;

public class Compass extends Fragment implements SensorEventListener, LocationListener {

    private static final String NA = "N/A";
    private static final String FIXED = "FIXED";

    private static final int LOCATION_MIN_TIME = 30 * 1000;
    private static final int LOCATION_MID_DISTANCE = 10;
    private float[] gravity = new float[3];
    private float[] geomagnetic = new float[3];
    private float[] rotation = new float[9];
    private float[] orination = new float[3];
    private float[] smoothed = new float[3];

    private SensorManager sensorManager;
    private Sensor sensorGravity;
    private Sensor sensorMagnetic;
    private LocationManager locationManager;
    private Location currentLocation;
    private GeomagneticField geomagneticField;
    private double bearing = 0;
    private TextView tvDegree;
    private TextView tvLat;
    private TextView tvLong;
    private TextView tvPermissionRequired;
    private LinearLayout lnLatLong;
    private CompassView compassView;

    private Context context;
    private boolean isLocationAllowed = true;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compass, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvLat = view.findViewById(R.id.tv_lat);
        tvLong = view.findViewById(R.id.tv_long);
        tvDegree = view.findViewById(R.id.tv_degree);
        compassView = view.findViewById(R.id.cv_compass);
        tvPermissionRequired = view.findViewById(R.id.tv_permission_required);
        lnLatLong = view.findViewById(R.id.ln_lat_long);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            isLocationAllowed = false;
            tvPermissionRequired.setVisibility(View.VISIBLE);
            lnLatLong.setVisibility(View.GONE);
        }

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        assert sensorManager != null;
        sensorGravity = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    private boolean checkPermissions() {

        if (context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Objects.requireNonNull(getActivity()).requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return false;
        }

        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Objects.requireNonNull(getActivity()).requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
            return false;
        }
        return true;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();

        if (isLocationAllowed)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_MIN_TIME, LOCATION_MID_DISTANCE, this);

        sensorManager.registerListener(this, sensorGravity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorMagnetic, SensorManager.SENSOR_DELAY_NORMAL);

        if (!isLocationAllowed)
            return;

        final Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (gpsLocation != null) {
            currentLocation = gpsLocation;
        } else {
            final Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (networkLocation != null)
                currentLocation = networkLocation;
            else {
                currentLocation = new Location(FIXED);
                currentLocation.setAltitude(1);
                currentLocation.setLatitude(43.29);
                currentLocation.setLongitude(5.23);
            }
            onLocationChanged(currentLocation);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, sensorMagnetic);
        sensorManager.unregisterListener(this, sensorGravity);
        locationManager.removeUpdates(this);
    }

    private void upDateLocationView(final Location location) {
        if (FIXED.equals(location.getProvider())) {
            tvLong.setText(NA);
            tvLat.setText(NA);
        }

        final DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        final NumberFormat format = new DecimalFormat("#0.00", dfs);
        tvLat.setText(String.format("Lat: %s", format.format(location.getLatitude())));
        tvLong.setText(String.format("Long: %s", format.format(location.getLongitude())));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        boolean accelOrMagnetic = false;

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            smoothed = Utils.lowPassFilter(event.values, gravity);
            gravity[0] = smoothed[0];
            gravity[1] = smoothed[1];
            gravity[2] = smoothed[2];
            accelOrMagnetic = true;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            smoothed = Utils.lowPassFilter(event.values, geomagnetic);
            geomagnetic[0] = smoothed[0];
            geomagnetic[1] = smoothed[1];
            geomagnetic[2] = smoothed[2];
            accelOrMagnetic = true;
        }

        SensorManager.getRotationMatrix(rotation, null, gravity, geomagnetic);
        SensorManager.getOrientation(rotation, orination);
        bearing = Math.toDegrees(orination[0]);

        if (geomagneticField != null)
            bearing += geomagneticField.getDeclination();

        if (bearing < 0)
            bearing += 360;

        compassView.setBearing((float) bearing);

        if (accelOrMagnetic)
            compassView.postInvalidate();

        updateTextDirection(bearing);

    }

    private void updateTextDirection(double bearing) {
        int range = (int) (bearing / (360f / 16f));
        String dirTxt = "";

        if (range == 15 || range == 0)
            dirTxt = "N";
        if (range == 1 || range == 2)
            dirTxt = "NE";
        if (range == 3 || range == 4)
            dirTxt = "E";
        if (range == 5 || range == 6)
            dirTxt = "SE";
        if (range == 7 || range == 8)
            dirTxt = "S";
        if (range == 9 || range == 10)
            dirTxt = "SW";
        if (range == 11 || range == 12)
            dirTxt = "W";
        if (range == 13 || range == 14)
            dirTxt = "NW";

        tvDegree.setText("" + ((int) bearing) + ((char) 176) + " "
                + dirTxt); // char 176 ) = degrees ...
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        upDateLocationView(location);
        geomagneticField = new GeomagneticField(
                (float) currentLocation.getLatitude(),
                (float) currentLocation.getLongitude(),
                (float) currentLocation.getAltitude(),
                System.currentTimeMillis());
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
