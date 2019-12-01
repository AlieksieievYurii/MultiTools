package com.wsinf.spytracker;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.FirebaseDatabase;
import com.wsinf.spytracker.location.LocationEvent;
import com.wsinf.spytracker.location.LocationService;

public class SpyTrackerService extends Service implements LocationEvent {

    static final String BROADCAST_SERVICE_LOCATION_CHANGE_ACTION = "gps.spy.tacker.service.location.change";
    static final String SERVICE_STATUS_EXTRA = "SERVICE_STATUS";
    static final String CURRENT_LATITUDE_EXTRA = "CURRENT_LATITUDE";
    static final String CURRENT_LONGITUDE_EXTRA = "CURRENT_LONGITUDE";
    static final String BROADCAST_SERVICE_STATE_ACTION = "gps.spy.tacker.service.state";
    static final int SERVICE_START = 1;
    static final int SERVICE_STOP = 0;

    static boolean isRunning = false;

    private com.wsinf.spytracker.Service locationService;
    private FirebaseDatabase firebaseDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        locationService = new LocationService(this, this);
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotification();
        locationService.onStart();
        isRunning = true;
        onServiceStateBroadCast(SERVICE_START);
        return START_STICKY;
    }

    private void createNotification() {

        final Intent intent = new Intent(this, MainActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        final Notification notification = new NotificationCompat.Builder(this, App.GPS_TRACKER_CHANNEL_ID)
                .setContentTitle("Gps spy tracker")
                .setContentText("The Gps spy tracker is working...")
                .setSmallIcon(R.drawable.ic_gps)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
    }

    private void onServiceStateBroadCast(final int state) {
        final Intent intent = new Intent(BROADCAST_SERVICE_STATE_ACTION);
        intent.putExtra(SERVICE_STATUS_EXTRA,state);
        sendBroadcast(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChange(double latitude, double longitude) {
        final Intent intent = new Intent(BROADCAST_SERVICE_LOCATION_CHANGE_ACTION);
        intent.putExtra(CURRENT_LATITUDE_EXTRA, latitude);
        intent.putExtra(CURRENT_LONGITUDE_EXTRA, longitude);
        sendBroadcast(intent);

        //TODO here must be inserting location points to the db
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        locationService.onStop();
        onServiceStateBroadCast(SERVICE_STOP);
    }
}
