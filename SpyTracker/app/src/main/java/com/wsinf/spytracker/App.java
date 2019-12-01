package com.wsinf.spytracker;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String GPS_TRACKER_CHANNEL_ID = "gps.spy.tracker.channel.id";

    @Override
    public void onCreate() {
        super.onCreate();

        createGpsTrackerNotificationChannel();
    }

    private void createGpsTrackerNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel notificationChannel = new NotificationChannel(
                    GPS_TRACKER_CHANNEL_ID,
                    "Gps spy tracker channel",
                    NotificationManager.IMPORTANCE_HIGH);

            final NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
