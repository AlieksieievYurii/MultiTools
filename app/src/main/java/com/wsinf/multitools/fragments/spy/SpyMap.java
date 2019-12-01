package com.wsinf.multitools.fragments.spy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wsinf.multitools.R;
import com.wsinf.multitools.fragments.spy.device.Device;
import com.wsinf.multitools.fragments.spy.point.Point;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SpyMap extends AppCompatActivity implements OnMapReadyCallback {
    public static final String DATE_EXTRA = "date.extra";
    public static final String DEVICES_LIST_EXTRA = "devices.list.extra";

    private FirebaseDatabase firebaseDatabase;
    private SupportMapFragment supportMapFragment;

    private Calendar date;
    private List<Device> deviceList;

    private Map<Device, List<Point>> deviceListMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spy_map);

        final Intent intent = getIntent();
        deviceList = intent.getParcelableArrayListExtra(DEVICES_LIST_EXTRA);
        if (deviceList == null)
            throw new RuntimeException("Activity needs list of Devices by the extra key DEVICES_LIST_EXTRA!");

        final Object objectCalendar = intent.getSerializableExtra(DATE_EXTRA);
        if (!(objectCalendar instanceof Calendar))
            throw new RuntimeException("Activity needs Calendar instance by the extra key DATE_EXTRA!");
        else
            date = (Calendar) objectCalendar;

        firebaseDatabase = FirebaseDatabase.getInstance();
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fl_map);
        if (supportMapFragment == null)
            throw new RuntimeException("Unable to start this activity. Map fragment is null!");

        Objects.requireNonNull(supportMapFragment.getView()).setVisibility(View.GONE);

        deviceListMap = new HashMap<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        for (final Device device : deviceList)
            getPointsFor(device);
    }

    public static long getMinTime(final Calendar calendar) {
        final Calendar c = (Calendar) calendar.clone();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        return c.getTimeInMillis();
    }

    private static long getMaxTime(final Calendar calendar){
        final Calendar c = (Calendar) calendar.clone();
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);

        return c.getTimeInMillis();
    }

    private void getPointsFor(final Device device) {
        final List<Point> pointsList = new ArrayList<>();
        final DatabaseReference  fireBasePoints = firebaseDatabase.getReference("Points/" + device.getId());
        fireBasePoints
                .orderByChild("timestamp")
                .startAt(getMinTime(date))
                .endAt(getMaxTime(date))
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot dsp : dataSnapshot.getChildren()) {
                    final Point point = dsp.getValue(Point.class);
                    assert point != null;
                    point.setId(dsp.getKey());
                    pointsList.add(point);
                }
                deviceListMap.put(device, pointsList);

                if (deviceListMap.size() == deviceList.size()) {
                    // All points are loaded for all devices
                    supportMapFragment.getMapAsync(SpyMap.this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Objects.requireNonNull(supportMapFragment.getView()).setVisibility(View.VISIBLE);
        Toast.makeText(this, deviceListMap.size() + "d", Toast.LENGTH_LONG).show();
    }
}
