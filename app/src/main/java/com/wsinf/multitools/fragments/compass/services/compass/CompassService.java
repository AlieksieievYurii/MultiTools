package com.wsinf.multitools.fragments.compass.services.compass;

import android.content.Context;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import androidx.annotation.NonNull;
import com.wsinf.multitools.fragments.compass.Utils;

public class CompassService implements CompassServiceInterface, SensorEventListener {
    private OnCompassEvent onCompassEvent;
    private Context context;

    private SensorManager sensorManager;

    private Sensor sensorGravity;
    private Sensor sensorMagnetic;

    private float[] gravity = new float[3];
    private float[] geomagnetic = new float[3];
    private float[] rotation = new float[9];
    private float[] orientation = new float[3];

    private GeomagneticField geomagneticField;

    private boolean isReadyGravity = false;
    private boolean isReadyGeomagnetic = false;


    public CompassService(@NonNull OnCompassEvent onCompassEvent, @NonNull Context context) {
        this.onCompassEvent = onCompassEvent;
        this.context = context;
        init();
    }

    private void init() {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        assert sensorManager != null;
        sensorGravity = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public void onStart() {
        sensorManager.registerListener(this, sensorGravity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorMagnetic, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onStop() {
        sensorManager.unregisterListener(this, sensorMagnetic);
        sensorManager.unregisterListener(this, sensorGravity);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Utils.lowPassFilter(event.values, gravity);
            isReadyGravity = true;
        }else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            Utils.lowPassFilter(event.values, geomagnetic);
            isReadyGeomagnetic = true;
        }

        if (isReadyGravity && isReadyGeomagnetic) {
            SensorManager.getRotationMatrix(rotation, null, gravity, geomagnetic);
            SensorManager.getOrientation(rotation, orientation);

            double bearing = Math.toDegrees(orientation[0]);

            if (geomagneticField != null)
                bearing += geomagneticField.getDeclination();

            if (bearing < 0)
                bearing += 360;

            onCompassEvent.onAzimuth((int)bearing);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void setLocation(Location location) {
        geomagneticField = new GeomagneticField(
                (float) location.getLatitude(),
                (float) location.getLongitude(),
                (float) location.getAltitude(),
                System.currentTimeMillis());
    }
}
