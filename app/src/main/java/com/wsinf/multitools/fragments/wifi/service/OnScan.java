package com.wsinf.multitools.fragments.wifi.service;


import android.net.wifi.ScanResult;

import java.util.List;

public interface OnScan
{
    void onSuccessfullyScan(final List<ScanResult> netWorksList);
}
