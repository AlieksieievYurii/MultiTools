package com.wsinf.multitools.fragments.permissions.permissions;

import android.Manifest;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

public class AccessCoarseLocationPermission extends Permission{
    private static final String PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int REQUEST_CODE = 0x05;


    public AccessCoarseLocationPermission(FragmentActivity fragmentActivity, Context context) {
        super(fragmentActivity, context, PERMISSION, REQUEST_CODE);
    }
}
