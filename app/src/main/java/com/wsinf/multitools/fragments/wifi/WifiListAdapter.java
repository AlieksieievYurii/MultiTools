package com.wsinf.multitools.fragments.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wsinf.multitools.R;

import java.util.List;

public class WifiListAdapter extends BaseAdapter {

    private List<ScanResult> scanResults;
    private Context context;

    WifiListAdapter(List<ScanResult> scanResults, Context context) {
        this.scanResults = scanResults;
        this.context = context;
    }

    @Override
    public int getCount() {
        return scanResults.size();
    }

    @Override
    public ScanResult getItem(int position) {
        return scanResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LayoutInflater layoutInflater = LayoutInflater.from(context);

        final ScanResult scanResult = getItem(position);

        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.network_item, parent, false);

        final TextView tvNetWorkName = convertView.findViewById(R.id.tv_network_name);
        final TextView tvNetWorkAddress  = convertView.findViewById(R.id.tv_network_address);
        final ImageView ivSignalLevel = convertView.findViewById(R.id.iv_signal);

        switch (WifiManager.calculateSignalLevel(scanResult.level, 5)) {
            case 0: ivSignalLevel.setImageResource(R.drawable.ic_signal_0); break;
            case 1: ivSignalLevel.setImageResource(R.drawable.ic_signal_1); break;
            case 2: ivSignalLevel.setImageResource(R.drawable.ic_signal_2); break;
            case 3: ivSignalLevel.setImageResource(R.drawable.ic_signal_3); break;
            case 4: ivSignalLevel.setImageResource(R.drawable.ic_signal_4); break;
        }

        tvNetWorkName.setText(scanResult.SSID);
        tvNetWorkAddress.setText(scanResult.BSSID);

        return convertView;
    }
}
