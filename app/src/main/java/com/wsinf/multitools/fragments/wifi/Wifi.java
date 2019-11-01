package com.wsinf.multitools.fragments.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        scanResults = new ArrayList<>();
        wifiListAdapter = new WifiListAdapter(scanResults, context);
        lvNetWorksList.setAdapter(wifiListAdapter);

        wifiService = new WifiService(context, this);

    }

    @Override
    public void onResume() {
        super.onResume();
        wifiService.scan();
        isListReady(false);
    }

    private void isListReady(final boolean isReady) {
        lvNetWorksList.setVisibility(isReady ? View.VISIBLE : View.GONE);
        pbLoading.setVisibility( isReady ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onSuccessfullyScan(final List<ScanResult> netWorksList) {
        scanResults.clear();
        scanResults.addAll(netWorksList);
        wifiListAdapter.notifyDataSetChanged();
        isListReady(true);
    }
}
