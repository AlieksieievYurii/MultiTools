package com.wsinf.multitools.fragments.spy.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wsinf.multitools.R;
import com.wsinf.multitools.fragments.spy.device.Device;

import java.util.List;

public class ListAdapter extends BaseAdapter {

    private List<Device> devices;
    private Context context;
    private OnDeviceSelection onDeviceSelection;

    public ListAdapter(List<Device> devices, Context context) {
        this.devices = devices;
        this.context = context;
    }

    public void setOnDeviceSelection(OnDeviceSelection onDeviceSelection) {
        this.onDeviceSelection = onDeviceSelection;
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Device getItem(int position) {
        return devices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Device device = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.device_item, parent, false);

        final TextView tvModel = convertView.findViewById(R.id.tv_model);
        final TextView tvMacAddress = convertView.findViewById(R.id.tc_mac_address);

        tvModel.setText(device.getModel());
        tvMacAddress.setText(device.getMacAddress());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeviceSelection != null)
                    onDeviceSelection.onDeviceSelect(device);
            }
        });

        return convertView;
    }
}
