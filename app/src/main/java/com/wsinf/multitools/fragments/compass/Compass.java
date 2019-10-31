package com.wsinf.multitools.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wsinf.multitools.R;

public class Compass extends Fragment implements SensorEventListener {

    private ImageView imCompass;
    private TextView tvDegree;

    private SensorManager sensorManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compass, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imCompass = view.findViewById(R.id.im_compass);
        tvDegree = view.findViewById(R.id.tv_degree);

        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public void onResume() {
        super.onResume();

        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_O))
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
