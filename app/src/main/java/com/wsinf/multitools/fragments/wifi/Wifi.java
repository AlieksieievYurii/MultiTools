package com.wsinf.multitools.fragments.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wsinf.multitools.R;
import com.wsinf.multitools.fragments.wifi.service.OnScan;
import com.wsinf.multitools.fragments.wifi.service.WifiService;

import java.util.ArrayList;
import java.util.List;

public class Wifi extends Fragment implements OnScan {

    private WifiService wifiService;
    private Context context;
    private ListView lvNetWorksList;
    private ProgressBar pbLoading;
    private List<ScanResult> scanResults;
    private WifiListAdapter wifiListAdapter;
    private LinearLayout lnWifiIsDisable;

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
        final Button btnEnableWifi = view.findViewById(R.id.btn_enable_wifi);
        btnEnableWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEnableWifi();
            }
        });

        scanResults = new ArrayList<>();
        wifiListAdapter = new WifiListAdapter(scanResults, context);
        lvNetWorksList.setAdapter(wifiListAdapter);

        wifiService = new WifiService(context, this);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (wifiService.isEnable()) {
            wifiService.scan();
            lvNetWorksList.setVisibility(View.GONE);
            pbLoading.setVisibility(View.VISIBLE);
        } else {
            lvNetWorksList.setVisibility(View.GONE);
            pbLoading.setVisibility(View.GONE);
            lnWifiIsDisable.setVisibility(View.VISIBLE);
        }
    }

    private void onEnableWifi()
    {
        wifiService.setEnable(true);
        lnWifiIsDisable.setVisibility(View.GONE);
        lvNetWorksList.setVisibility(View.GONE);
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        scanResults.clear();
    }


    @Override
    public void onSuccessfullyScan(final List<ScanResult> netWorksList) {
        scanResults.clear();
        scanResults.addAll(netWorksList);
        wifiListAdapter.notifyDataSetChanged();
        lvNetWorksList.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.GONE);
    }
}
