package com.wsinf.multitools.fragments.wifi.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.wsinf.multitools.fragments.wifi.Wifi;

public class WifiService {

    private final OnScan onScan;
    private WifiManager wifiManager;
    private Context context;

    private BroadcastReceiver brWifiTurnOff;
    private BroadcastReceiver brConnection;

    private boolean isConnection = false;


    public WifiService(final Context context, final OnScan onScan) {
        this.onScan = onScan;
        this.context = context;

        this.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        final IntentFilter wifiIntentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        final BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success)
                    onScan.onSuccessfullyScan(wifiManager.getScanResults());
            }
        };

        context.registerReceiver(wifiScanReceiver, wifiIntentFilter);
    }

    public boolean isEnable() {
        return wifiManager.isWifiEnabled();
    }

    public void setEnable(final boolean enable) {
        wifiManager.setWifiEnabled(enable);
    }

    public String getCurrentSSID() {
        return wifiManager.getConnectionInfo().getSSID();
    }

    public void registerWifiDisconnection(final OnWifiTurnOff onWifiTurnOff) {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);

        brWifiTurnOff = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final int extraWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE ,
                        WifiManager.WIFI_STATE_UNKNOWN);
                if (extraWifiState == WifiManager.WIFI_STATE_DISABLED)
                    onWifiTurnOff.onWifiTurnedOff();
            }
        };
        context.registerReceiver(brWifiTurnOff, intentFilter);
    }

    public void unRegisterWifiDisconnection(){
        context.unregisterReceiver(brWifiTurnOff);
    }


    public void connect(final String netWork, final String password, final OnConnection onConnection) {
        isConnection = true;
        final WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = String.format("\"%s\"", netWork);
        wifiConfiguration.preSharedKey = String.format("\"%s\"", password);
        wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        wifiConfiguration.status = WifiConfiguration.Status.ENABLED;
        final int id = wifiManager.addNetwork(wifiConfiguration);
        Log.d("WIFI", "ID is " + id);
        //wifiManager.disconnect();
        wifiManager.disableNetwork(wifiManager.getConnectionInfo().getNetworkId());
        Log.d("WIFI", "ENABLE status is " + wifiManager.enableNetwork(id, true));
        registerConnection(onConnection, netWork);
        Log.d("WIFI", "Reconnection status is " + wifiManager.reconnect());

    }

    private void registerConnection(final OnConnection onConnection, final String netWorkName) {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);

        brConnection = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (info != null && info.isConnected())
                {
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    String ssid = wifiInfo.getSSID();
                    if (ssid.equals(netWorkName))
                        onConnection.onSuccess();
                    else
                        onConnection.onFail();
                }

                context.unregisterReceiver(brConnection);
                isConnection = false;
            }
        };
        context.registerReceiver(brConnection, intentFilter);

    }

    public boolean isConnection() {
        return isConnection;
    }

    public void scan() {
        wifiManager.startScan();
    }
}
