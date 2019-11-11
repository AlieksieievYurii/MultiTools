package com.wsinf.multitools.fragments.compass;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.wsinf.multitools.R;
import com.wsinf.multitools.fragments.compass.services.Service;
import com.wsinf.multitools.fragments.compass.services.compass.CompassService;
import com.wsinf.multitools.fragments.compass.services.compass.CompassServiceInterface;
import com.wsinf.multitools.fragments.compass.services.compass.OnCompassEvent;
import com.wsinf.multitools.fragments.compass.services.location.LocationService;
import com.wsinf.multitools.fragments.compass.services.location.OnLocationEvent;
import com.wsinf.multitools.fragments.permissions.OnPermissionsRequest;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Compass extends Fragment implements OnCompassEvent, OnLocationEvent, OnPermissionsRequest {

    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private static final String NA = "N/A";

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    private Context context;

    private TextView tvSunRise;
    private TextView tvSunSet;
    private TextView tvAltitude;
    private TextView tvLat;
    private TextView tvLong;
    private CompassView compassView;

    private CompassServiceInterface compassService;
    private Service locationService;


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
        tvSunRise = view.findViewById(R.id.tv_sunrise);
        tvSunSet = view.findViewById(R.id.tv_sunset);
        tvAltitude = view.findViewById(R.id.tv_altitude);
        tvLat = view.findViewById(R.id.tv_lat);
        tvLong = view.findViewById(R.id.tv_long);
        compassView = view.findViewById(R.id.cv_compass);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        compassService = new CompassService(this, context);
        locationService = new LocationService(this, context);
    }

    @Override
    public void onStart() {
        super.onStart();
        compassService.onStart();
        try {
            locationService.onStart();
        } catch (SecurityException e) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        locationService.onStop();
        compassService.onStop();
    }

    @Override
    public void onAzimuth(int azimuth) {
        compassView.onAzimuth(azimuth);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onLocation(Location location, Date sunrise, Date sunset) {

        if (location.getProvider().equals(LocationService.FIXED)) {
            tvLat.setText(NA);
            tvAltitude.setText(NA);
            tvSunSet.setText(NA);
            tvLong.setText(NA);
            tvSunRise.setText(NA);
        }

        compassService.setLocation(location);

        final DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        final NumberFormat formatCoordinates = new DecimalFormat("#0.00", dfs);
        tvLat.setText(String.format("Lat: %s", formatCoordinates.format(location.getLatitude())));
        tvLong.setText(String.format("Long: %s", formatCoordinates.format(location.getLongitude())));
        tvSunRise.setText(timeFormat.format(sunrise));
        tvSunSet.setText(timeFormat.format(sunset));
        tvAltitude.setText(String.format("%d m", Math.round(location.getAltitude())));
    }

    @Override
    public void onRequest(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST && grantResults.length > 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            locationService.onStart();
        }
    }
}
