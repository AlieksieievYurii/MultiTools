package com.wsinf.multitools.fragments.embedded;

import android.content.Context;
import android.hardware.Sensor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wsinf.multitools.R;

import java.util.List;

public class SensorsListAdapter extends BaseAdapter {

    private List<Sensor> sensors;
    private Context context;

    public SensorsListAdapter(List<Sensor> sensors, Context context) {
        this.sensors = sensors;
        this.context = context;
    }

    @Override
    public int getCount() {
        return sensors.size();
    }

    @Override
    public Sensor getItem(int position) {
        return sensors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return sensors.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sensor_item, parent, false);
        }

        final TextView tvSensorName = convertView.findViewById(R.id.tv_sensor_name);
        final TextView tvSensorVendor = convertView.findViewById(R.id.tv_sensor_vendor);
        final TextView tvSensorVersion = convertView.findViewById(R.id.tv_sensor_version);

        final Sensor sensor = getItem(position);
        tvSensorName.setText(sensor.getName());
        tvSensorVendor.setText(sensor.getVendor());
        tvSensorVersion.setText(String.valueOf(sensor.getVersion()));
        return convertView;
    }
}
