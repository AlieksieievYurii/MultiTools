package com.wsinf.multitools.fragments.permissions.permissions;

import android.Manifest;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

public class CameraPermission extends Permission {

    private static final String PERMISSION = Manifest.permission.CAMERA;
    private static final int REQUEST_CODE = 0x02;

    public CameraPermission(FragmentActivity fragmentActivity, Context context) {
        super(fragmentActivity, context, PERMISSION, REQUEST_CODE);
    }
}