package com.wsinf.multitools.fragments.gpstracker;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.wsinf.multitools.App;
import com.wsinf.multitools.MainActivity;
import com.wsinf.multitools.R;

public class LocationTracker extends Service implements LocationEvent {

    static boolean isRunning = false;
    static final String BROADCAST_SERVICE_STATE_ACTION = "gps.tacker.service.state";
    static final String BROADCAST_SERVICE_LOCATION_CHANGE_ACTION = "gps.tacker.service.location.change";
    static final String SERVICE_STATUS_EXTRA = "SERVICE_STATUS";
    static final String CURRENT_LATITUDE_EXTRA = "CURRENT_LATITUDE";
    static final String CURRENT_LONGITUDE_EXTRA = "CURRENT_LONGITUDE";
    static final int SERVICE_START = 1;
    static final int SERVICE_STOP = 0;

    private com.wsinf.multitools.fragments.compass.services.Service locationService;

    @Override
    public void onCreate() {
        super.onCreate();
        locationService = new LocationService(this, this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotification();
        locationService.onStart();
        isRunning = true;
        onServiceStateBroadCast(SERVICE_START);

        return START_STICKY;
    }

    private void onServiceStateBroadCast(final int state) {
        final Intent intent = new Intent(BROADCAST_SERVICE_STATE_ACTION);
        intent.putExtra(SERVICE_STATUS_EXTRA,state);
        sendBroadcast(intent);
    }

    private void createNotification() {

        final Intent intent = new Intent(this, MainActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        final Notification notification = new NotificationCompat.Builder(this, App.GPS_TRACKER_CHANNEL_ID)
                .setContentTitle("Gps tracker")
                .setContentText("The Gps tracker is working...")
                .setSmallIcon(R.drawable.ic_gps)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationService.onStop();
        isRunning = false;
        onServiceStateBroadCast(SERVICE_STOP);
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
    }
}
