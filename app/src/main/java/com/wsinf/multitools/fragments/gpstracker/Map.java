package com.wsinf.multitools.fragments.gpstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.wsinf.multitools.App;
import com.wsinf.multitools.R;
import com.wsinf.multitools.fragments.gpstracker.room.Point;

import java.util.Calendar;
import java.util.List;

public class Map extends AppCompatActivity implements OnMapReadyCallback {

    public static final String TIME_STAMP_EXTRA = "timestamp.extra";

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        final Object objCalendar = getIntent().getSerializableExtra(TIME_STAMP_EXTRA);
        if (!(objCalendar instanceof Calendar)) {
            throw new RuntimeException("Expects calendar instance!");
        }

        calendar = (Calendar) objCalendar;

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fl_map);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        final PolylineOptions polylineOptions = getLine();

        if (polylineOptions.getPoints().isEmpty())
            return;

        polylineOptions.color(Color.RED);
        googleMap.addPolyline(polylineOptions);

        googleMap.addCircle(new CircleOptions()
                .center(polylineOptions.getPoints().get(0))
                .radius(1)
                .strokeWidth(0)
                .fillColor(Color.GREEN));

        googleMap.addCircle(new CircleOptions()
                .center(polylineOptions.getPoints().get(polylineOptions.getPoints().size() - 1))
                .radius(1)
                .strokeWidth(0)
                .fillColor(Color.BLACK));

        googleMap.moveCamera(moveCamera(polylineOptions));
    }


    private CameraUpdate moveCamera(final PolylineOptions polylineOptions) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(polylineOptions.getPoints().get(0));
        builder.include(polylineOptions.getPoints().get(polylineOptions.getPoints().size() - 1));
        return CameraUpdateFactory.newLatLngBounds(builder.build(), 48);
    }

    private PolylineOptions getLine() {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        final long startDateTime = calendar.getTimeInMillis();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        final long endDateTime = calendar.getTimeInMillis();

        final PolylineOptions polylineOptions = new PolylineOptions();

        for (final Point point : getPointsFromDataBase(startDateTime, endDateTime)) {
            polylineOptions.add(new LatLng(point.getLatitude(), point.getLongitude()));
        }

        return polylineOptions;
    }

    private List<Point> getPointsFromDataBase(final long startTime, final long endTime) {
        final App app = ((App) getApplication());
        return app.getLocalDataBase().pointDao().getAllPointsByTimeStamp(startTime, endTime);
    }
}
