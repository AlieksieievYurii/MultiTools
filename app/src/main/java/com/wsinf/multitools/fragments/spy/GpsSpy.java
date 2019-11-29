package com.wsinf.multitools.fragments.spy;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.FirebaseDatabase;
import com.wsinf.multitools.R;
import com.wsinf.multitools.fragments.spy.device.Device;
import com.wsinf.multitools.fragments.spy.device.FbDevice;
import com.wsinf.multitools.fragments.spy.utils.adapter.OnSelection;
import com.wsinf.multitools.fragments.spy.utils.adapter.SelectableListAdapter;
import com.wsinf.multitools.fragments.spy.utils.adapter.ListAdapter;

import java.util.List;


public class GpsSpy extends Fragment implements OnSelection<Device> {
    private FirebaseDatabase firebaseDatabase;

    private Context context;
    private ListView lwDevices;
    private View pbLoading;
    private View rlSelectionOptions;
    private TextView tvNotTrackedDevices;
    private FbObject<Device> fbDevices;
    private SelectableListAdapter<Device> listAdapter;

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

        initSelectionOptions(view);
    }

    private void initSelectionOptions(final View view) {
        rlSelectionOptions = view.findViewById(R.id.cv_selection_options);

        final Button btnCancel = view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAdapter.onCancelSelection();
                rlSelectionOptions.setVisibility(View.GONE);
            }
        });

        final Button btnCheckAll = view.findViewById(R.id.btn_check_all);
        btnCheckAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAdapter.onCheckAll();
            }
        });

        final Button btnSelect = view.findViewById(R.id.btn_select);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAdapter.onSelect();
            }
        });
    }

    private void dataAreReady(final List<Device> devices) {
        pbLoading.setVisibility(View.GONE);
        lwDevices.setVisibility(View.VISIBLE);

        listAdapter = new ListAdapter(context, lwDevices, devices);
        listAdapter.setOnSelection(this);
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
    public void onSelectedDevices(List<Device> list) {
        Toast.makeText(context, "LIST" + list.size(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeviceSelect(Device device) {
        Toast.makeText(context, device.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSelectableMode() {
        rlSelectionOptions.setVisibility(View.VISIBLE);
    }

//    @Override
//    public void onDeviceSelect(Device device) {
//        final DatePickerDialog datePickerDialog = new DatePickerDialog(context);
//        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
//        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                final Calendar calendar = Calendar.getInstance();
//                calendar.set(Calendar.YEAR, year);
//                calendar.set(Calendar.MONTH, month);
//                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//
//                final Intent intent = new Intent(context, SpyMap.class);
//                startActivity(intent);
//
//            }
//        });
//
//        datePickerDialog.show();
//    }
}
