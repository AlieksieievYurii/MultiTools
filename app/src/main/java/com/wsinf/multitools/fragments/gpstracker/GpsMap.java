package com.wsinf.multitools.fragments.gpstracker;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.wsinf.multitools.R;
import java.util.Calendar;

public class GpsMap extends Fragment implements View.OnClickListener {

    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private Button btnTurnTracker;
    private Button btnMap;
    private TextView tvCurrentCoordinates;
    private ProgressBar pbTackerRunning;
    private Context context;
    private Intent serviceIntent;

    private BroadcastReceiver brServiceState = new BroadCastReceiverServiceState();
    private BroadcastReceiver brLocationChange = new BroadCastReceiverLocationChange();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        serviceIntent = new Intent(context, LocationTracker.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gps_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnTurnTracker = view.findViewById(R.id.btn_turn_service);
        btnMap = view.findViewById(R.id.btn_map);
        tvCurrentCoordinates = view.findViewById(R.id.tv_current_coordinates);
        pbTackerRunning = view.findViewById(R.id.pb_tracker_running);

        btnMap.setOnClickListener(this);
        btnTurnTracker.setOnClickListener(this);

        refreshView();
    }

    @Override
    public void onResume() {
        super.onResume();
        context.registerReceiver(brServiceState, new IntentFilter(LocationTracker.BROADCAST_SERVICE_STATE_ACTION));

        context.registerReceiver(brLocationChange, new IntentFilter(LocationTracker.BROADCAST_SERVICE_LOCATION_CHANGE_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        context.unregisterReceiver(brServiceState);
        context.unregisterReceiver(brLocationChange);
    }

    private void refreshView() {
        btnTurnTracker.setActivated(!LocationTracker.isRunning);
        btnTurnTracker.setText(LocationTracker.isRunning ? "OFF" : "ON");
        if (LocationTracker.isRunning) {
            btnTurnTracker.setEnabled(true);
            pbTackerRunning.setVisibility(View.VISIBLE);
        } else {
            tvCurrentCoordinates.setText(R.string.default_coordinates);
            pbTackerRunning.setVisibility(View.GONE);
        }


    }

    private void onOpenMap() {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(context);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                final Intent intent = new Intent(context, Map.class);
                intent.putExtra(Map.TIME_STAMP_EXTRA, calendar);
                startActivity(intent);
            }
        });

        datePickerDialog.show();
    }


    private void onTurnTracker() {
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        } else
            startGpsTracker();
    }

    private void startGpsTracker() {
        context.startForegroundService(serviceIntent);
        btnTurnTracker.setEnabled(false);
    }

    private void onClickStartService() {
        if (LocationTracker.isRunning)
            context.stopService(serviceIntent);
        else
            onTurnTracker();
    }

    @Override
    public void onClick(View v) {
        if (v == btnMap)
            onOpenMap();
        else if (v == btnTurnTracker)
            onClickStartService();
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
            Toast.makeText(context, "You do not have permissions!", Toast.LENGTH_LONG).show();
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

            final double latitude = intent.getDoubleExtra(LocationTracker.CURRENT_LATITUDE_EXTRA, -1);
            final double longitude = intent.getDoubleExtra(LocationTracker.CURRENT_LONGITUDE_EXTRA, -1);

            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Lat: ");
            stringBuilder.append(latitude == -1 ? "--" : latitude);
            stringBuilder.append(",   Long: ");
            stringBuilder.append(longitude == -1 ? "--" : longitude);

            tvCurrentCoordinates.setText(stringBuilder.toString());
        }
    }
}
