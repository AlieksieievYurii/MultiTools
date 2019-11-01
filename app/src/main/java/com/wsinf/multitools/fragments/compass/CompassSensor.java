package com.wsinf.multitools.fragments.compass;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import static android.content.Context.SENSOR_SERVICE;

public class CompassSensor implements MySensor, SensorEventListener {

    private Context context;
    private CompassEvent compassEvent;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;

    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;

    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];


    CompassSensor(Context context, CompassEvent compassEvent) {
        this.context = context;
        this.compassEvent = compassEvent;

        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        assert mSensorManager != null;

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }

    @Override
    public void onStop() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onStart() {
        mLastAccelerometerSet = false;
        mLastMagnetometerSet = false;

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            if (SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer)) {
                SensorManager.getOrientation(mR, mOrientation);
                float[] ex = new float[3];
                applyLowPassFilter(mOrientation, ex);
                final int azimuthInDegree = (int) (Math.toDegrees(ex[0]) + 360) % 360;
                compassEvent.onCompassChange((azimuthInDegree));
            }
        }
    }

    private static final float ALPHA = 0.2f;
    //lower alpha should equal smoother movement


    private float[] applyLowPassFilter(float[] input, float[] output) {
        if (output == null) return input;

        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
