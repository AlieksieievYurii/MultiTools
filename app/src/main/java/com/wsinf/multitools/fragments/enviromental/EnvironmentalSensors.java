package com.wsinf.multitools.fragments.enviromental;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wsinf.multitools.R;

import java.util.Locale;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class EnvironmentalSensors extends Fragment implements SensorEventListener {

    private Context context;
    private MaterialProgressBar mpbTemperature;
    private TextView tvTemperature;
    private TextView tvHumidity;
    private TextView tvDewPoint;
    private TextView tvAbsoluteHumidity;
    private TextView tvNotSupported;
    private RelativeLayout rlBody;

    private SensorManager sensorManager;
    private Sensor temperatureSensor;
    private Sensor humiditySensor;

    private float ambientTemperature = -1;
    private float ambientHumidity = -1;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_environmental_sensors, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mpbTemperature = view.findViewById(R.id.mpb_temperature);
        tvTemperature = view.findViewById(R.id.tv_temperature);
        tvHumidity = view.findViewById(R.id.tv_humidity);
        tvDewPoint = view.findViewById(R.id.tv_dew_point);
        tvAbsoluteHumidity = view.findViewById(R.id.tv_absolute_humidity);
        tvNotSupported = view.findViewById(R.id.tv_hint_not_supported);
        rlBody = view.findViewById(R.id.rl_body);

        initSensors();
    }

    private void initSensors() {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        assert sensorManager != null;
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

        if (temperatureSensor == null || humiditySensor == null) {
            rlBody.setVisibility(View.GONE);
            tvNotSupported.setVisibility(View.VISIBLE);
        }
    }

    private void drawResults(double dewPoint, double absoluteHumidity) {
        tvTemperature.setText(String.format(Locale.getDefault(), "%.0f \u2103", ambientTemperature));
        setTemperatureProgressBar();
        tvHumidity.setText(String.format(Locale.getDefault(), "%.0f %%", ambientHumidity));
        tvDewPoint.setText(String.format(Locale.getDefault(), "%.1f \u2109", dewPoint));
        tvAbsoluteHumidity.setText(String.format(Locale.getDefault(), "%.1f g/m\u00B2", absoluteHumidity));
    }

    private void setTemperatureProgressBar() {

        if (ambientTemperature < -100 || ambientTemperature > 100)
            return;

        final int progress = Utils.map((int) ambientTemperature, -100, 100, 0, 100);
        mpbTemperature.setProgress(progress);

        if (progress < 50)
            mpbTemperature.getProgressDrawable().setColorFilter(Color.rgb(0,0, (int)(255 - progress * 3.5)), PorterDuff.Mode.SRC_IN);
        else
            mpbTemperature.getProgressDrawable().setColorFilter(Color.rgb((int)(100 + (progress - 50) * 3.1),0,0), PorterDuff.Mode.SRC_IN);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (temperatureSensor != null && humiditySensor != null) {
            sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (temperatureSensor != null && humiditySensor != null)
            sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == temperatureSensor)
            ambientTemperature = event.values[0];
        else if (event.sensor == humiditySensor)
            ambientHumidity = event.values[0];

        if (ambientHumidity != -1 && ambientTemperature != -1) {
            final double dewPoint = Utils.calculateDewPoint(ambientTemperature, ambientHumidity);
            final double absoluteHumidity = Utils.calculateAbsoluteHumidity(ambientTemperature, ambientHumidity);

            drawResults(dewPoint, absoluteHumidity);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
