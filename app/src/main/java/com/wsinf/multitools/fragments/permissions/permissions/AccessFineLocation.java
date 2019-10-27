package com.wsinf.multitools.fragments.permissions.permissions;

import android.Manifest;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

public class AccessFineLocation extends Permission {
    private static final String PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int REQUEST_CODE = 0x06;


    public AccessFineLocation(FragmentActivity fragmentActivity, Context context) {
        super(fragmentActivity, context, PERMISSION, REQUEST_CODE);
    }
}
