package com.wsinf.multitools.fragments.spy;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.FirebaseDatabase;
import com.wsinf.multitools.R;
import com.wsinf.multitools.fragments.gpstracker.Map;
import com.wsinf.multitools.fragments.spy.device.Device;
import com.wsinf.multitools.fragments.spy.device.FbDevice;
import com.wsinf.multitools.fragments.spy.utils.ListAdapter;
import com.wsinf.multitools.fragments.spy.utils.OnDeviceSelection;

import java.util.Calendar;
import java.util.List;


public class GpsSpy extends Fragment implements OnDeviceSelection {
    private FirebaseDatabase firebaseDatabase;

    private Context context;
    private ListView lwDevices;
    private ProgressBar pbLoading;
    private TextView tvNotTrackedDevices;
    private FbObject<Device> fbDevices;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
        fbDevices = new FbDevice(firebaseDatabase.getReference("Devices"));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gps_spy, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lwDevices = view.findViewById(R.id.lv_devices_list);
        pbLoading = view.findViewById(R.id.pb_loading);
        tvNotTrackedDevices = view.findViewById(R.id.tv_not_tracked_devices);
    }

    private void dataAreReady(final List<Device> devices) {
        pbLoading.setVisibility(View.GONE);
        lwDevices.setVisibility(View.VISIBLE);

        final ListAdapter listAdapter = new ListAdapter(devices, context);
        listAdapter.setOnDeviceSelection(GpsSpy.this);
        lwDevices.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

    private void noData() {
        pbLoading.setVisibility(View.GONE);
        tvNotTrackedDevices.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        fbDevices.getAll(new Promise<Device>() {
            @Override
            public void onReceiveAll(List<Device> list) {
                if (list.isEmpty())
                    noData();
                else
                    dataAreReady(list);
            }
        });
    }

    @Override
    public void onDeviceSelect(Device device) {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(context);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            }
        });

        datePickerDialog.show();
    }
}
