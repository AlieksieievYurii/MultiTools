package com.wsinf.multitools.fragments.wifi.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import java.util.List;

public class WifiService {

    private final OnScan onScan;
    private WifiManager wifiManager;


    public WifiService(final Context context, final OnScan onScan) {
        this.onScan = onScan;

        this.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        final IntentFilter wifiIntentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        final BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success)
                    scanSuccess();
            }
        };

        context.registerReceiver(wifiScanReceiver, wifiIntentFilter);
    }

    private void scanSuccess() {
        final List<ScanResult> list = wifiManager.getScanResults();
        onScan.onSuccessfullyScan(list);
    }

    public boolean scan() {
        return wifiManager.startScan();
    }
}
