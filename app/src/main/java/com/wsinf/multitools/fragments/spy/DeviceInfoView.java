package com.wsinf.multitools.fragments.spy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.wsinf.multitools.R;
import com.wsinf.multitools.fragments.spy.device.Device;

import org.json.JSONObject;

public class DeviceInfoView implements GoogleMap.InfoWindowAdapter {

    private Context context;

    DeviceInfoView(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        final Device device = Device.toObject(marker.getSnippet());
        assert device != null;

        final View view = LayoutInflater.from(context).inflate(R.layout.info_marker, null, false);
        final TextView tvModel = view.findViewById(R.id.tv_model);
        final TextView tvAndroidId = view.findViewById(R.id.tv_android_id);
        final TextView tvBoard = view.findViewById(R.id.tv_board);
        final TextView tvBrand = view.findViewById(R.id.tv_brand);
        final TextView tvHost = view.findViewById(R.id.tv_host);

        tvModel.setText(device.getModel());
        tvAndroidId.setText(String.format("ID: %s", device.getId()));
        tvBoard.setText(String.format("Board: %s", device.getBoard()));
        tvBrand.setText(String.format("Brand: %s", device.getBrand()));
        tvHost.setText(String.format("Host: %s", device.getHost()));

        return view;
    }
}
