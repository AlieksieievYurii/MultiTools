package com.wsinf.multitools.fragments.level;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.SENSOR_SERVICE;

public class LevelSensor extends Fragment implements SensorEventListener {

    private Context context;
    private SensorManager sensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private CanvasView rootView;

    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];

    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;

    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.sensorManager = (SensorManager) this.context.getSystemService(SENSOR_SERVICE);

        assert this.sensorManager != null;
        this.mAccelerometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.mMagnetometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public void onResume() {
        super.onResume();

        this.sensorManager.registerListener(this, this.mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        this.sensorManager.registerListener(this, this.mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.sensorManager.unregisterListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        this.rootView.surfaceDestroyed(null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = new CanvasView(this.context);
        return this.rootView;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
       if (event.sensor == this.mAccelerometer) {
           System.arraycopy(event.values, 0, this.mLastAccelerometer, 0, event.values.length);
           this.mLastAccelerometerSet = true;
       }
       else if (event.sensor == this.mMagnetometer) {
           System.arraycopy(event.values, 0, this.mLastMagnetometer, 0, event.values.length);
           this.mLastMagnetometerSet = true;
       }

       if (this.mLastAccelerometerSet && this.mLastMagnetometerSet) {
           SensorManager.getRotationMatrix(this.mR, null, this.mLastAccelerometer, this.mLastMagnetometer);
           SensorManager.getOrientation(this.mR, this.mOrientation);
       }

       double y = Math.toDegrees(mOrientation[1]);
       double x = Math.toDegrees(mOrientation[2]);

        rootView.setValues(-(int)x,(int)y);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
