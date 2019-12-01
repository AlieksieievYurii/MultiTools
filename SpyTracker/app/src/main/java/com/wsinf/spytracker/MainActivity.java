package com.wsinf.spytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private Button btnTurnTracker;
    private TextView tvCurrentCoordinates;
    private ProgressBar pbTackerRunning;
    private Intent serviceIntent;

    private BroadcastReceiver brServiceState = new BroadCastReceiverServiceState();
    private BroadcastReceiver brLocationChange = new BroadCastReceiverLocationChange();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serviceIntent = new Intent(this, SpyTrackerService.class);

        btnTurnTracker = findViewById(R.id.btn_turn_service);
        tvCurrentCoordinates = findViewById(R.id.tv_current_coordinates);
        pbTackerRunning = findViewById(R.id.pb_tracker_running);

        btnTurnTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SpyTrackerService.isRunning)
                    stopService(serviceIntent);
                else
                    onTurnTracker();
            }
        });

        refreshView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(brServiceState, new IntentFilter(SpyTrackerService.BROADCAST_SERVICE_STATE_ACTION));
        registerReceiver(brLocationChange, new IntentFilter(SpyTrackerService.BROADCAST_SERVICE_LOCATION_CHANGE_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(brServiceState);
        unregisterReceiver(brLocationChange);
    }

    private void onTurnTracker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            } else
                startGpsTracker();
        }else
            startGpsTracker();
    }

    private void startGpsTracker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        }else
            startService(serviceIntent);
        btnTurnTracker.setEnabled(false);
    }

    private void refreshView() {
        btnTurnTracker.setActivated(!SpyTrackerService.isRunning);
        btnTurnTracker.setText(SpyTrackerService.isRunning ? "OFF" : "ON");
        if (SpyTrackerService.isRunning) {
            btnTurnTracker.setEnabled(true);
            pbTackerRunning.setVisibility(View.VISIBLE);
        } else {
            tvCurrentCoordinates.setText(R.string.default_coordinates);
            pbTackerRunning.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST &&
                permissions.length == 2 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            startGpsTracker();
        } else
            Toast.makeText(this, "You do not have permissions!", Toast.LENGTH_LONG).show();
    }

    private class BroadCastReceiverServiceState extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshView();
        }
    }

    private class BroadCastReceiverLocationChange extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            final double latitude = intent.getDoubleExtra(SpyTrackerService.CURRENT_LATITUDE_EXTRA, -1);
            final double longitude = intent.getDoubleExtra(SpyTrackerService.CURRENT_LONGITUDE_EXTRA, -1);

            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Lat: ");
            stringBuilder.append(latitude == -1 ? "--" : latitude);
            stringBuilder.append(",   Long: ");
            stringBuilder.append(longitude == -1 ? "--" : longitude);

            tvCurrentCoordinates.setText(stringBuilder.toString());
        }
    }
}


