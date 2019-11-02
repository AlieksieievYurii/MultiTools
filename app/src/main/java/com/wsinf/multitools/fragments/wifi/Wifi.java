package com.wsinf.multitools.fragments.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wsinf.multitools.R;
import com.wsinf.multitools.fragments.wifi.service.OnConnection;
import com.wsinf.multitools.fragments.wifi.service.OnScan;
import com.wsinf.multitools.fragments.wifi.service.OnWifiTurnOff;
import com.wsinf.multitools.fragments.wifi.service.WifiService;

import java.util.ArrayList;
import java.util.List;

public class Wifi extends Fragment implements OnScan, OnNetWorkSelect, CallBack, OnWifiTurnOff {

    private WifiService wifiService;
    private Context context;
    private ListView lvNetWorksList;
    private ProgressBar pbLoading;
    private List<ScanResult> scanResults;
    private WifiListAdapter wifiListAdapter;
    private LinearLayout lnWifiIsDisable;
    private FrameLayout flConnection;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wifi, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lvNetWorksList = view.findViewById(R.id.lv_networks_list);
        pbLoading = view.findViewById(R.id.pb_loading);
        lnWifiIsDisable = view.findViewById(R.id.ln_wifi_is_disable);
        flConnection = view.findViewById(R.id.fl_connection);
        final Button btnEnableWifi = view.findViewById(R.id.btn_enable_wifi);
        btnEnableWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEnableWifi();
            }
        });

        wifiService = new WifiService(context, this);

        scanResults = new ArrayList<>();
        wifiListAdapter = new WifiListAdapter(scanResults,
                context,
                this,
                wifiService.getCurrentSSID());
        lvNetWorksList.setAdapter(wifiListAdapter);

        wifiService.registerWifiDisconnection(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (wifiService.isEnable()) {
            wifiService.scan();
            setStateLoadingList();
        } else
            setStateDisableWifi();
    }

    private void onEnableWifi() {
        wifiService.setEnable(true);
        setStateLoadingList();
    }

    @Override
    public void onPause() {
        super.onPause();
        scanResults.clear();
        wifiService.unRegisterWifiDisconnection();
    }


    @Override
    public void onSuccessfullyScan(final List<ScanResult> netWorksList) {
        if (wifiService.isEnable() && !wifiService.isConnection()) {
            scanResults.clear();
            scanResults.addAll(netWorksList);
            wifiListAdapter.notifyDataSetChanged();
            setStateDataIsReady();
        }
    }

    @Override
    public void onSelect(ScanResult scanResult) {
        final ConnectionDialog connectionDialog = new ConnectionDialog(context, scanResult, this);
        connectionDialog.show();
    }

    @Override
    public void onConnect(ScanResult scanResult, final String password) {
        setStateConnection();
        wifiService.connect(scanResult.SSID, password, new OnConnection() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, "Connected!", Toast.LENGTH_LONG).show();
                setStateDataIsReady();
            }

            @Override
            public void onFail() {
                Toast.makeText(context, "Fail!", Toast.LENGTH_LONG).show();
                setStateDataIsReady();
            }
        });
    }

    @Override
    public void onWifiTurnedOff() {
        setStateDisableWifi();
    }

    private void setStateConnection() {
        pbLoading.setVisibility(View.GONE);
        flConnection.setVisibility(View.VISIBLE);
    }

    private void setStateLoadingList() {
        pbLoading.setVisibility(View.VISIBLE);
        lvNetWorksList.setVisibility(View.GONE);
        lnWifiIsDisable.setVisibility(View.GONE);
        flConnection.setVisibility(View.GONE);
    }

    private void setStateDisableWifi() {
        pbLoading.setVisibility(View.GONE);
        lvNetWorksList.setVisibility(View.GONE);
        lnWifiIsDisable.setVisibility(View.VISIBLE);
        flConnection.setVisibility(View.GONE);
    }

    private void setStateDataIsReady() {
        pbLoading.setVisibility(View.GONE);
        lvNetWorksList.setVisibility(View.VISIBLE);
        lnWifiIsDisable.setVisibility(View.GONE);
        flConnection.setVisibility(View.GONE);
    }
}
