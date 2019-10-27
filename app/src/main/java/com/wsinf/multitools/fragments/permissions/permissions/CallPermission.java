package com.wsinf.multitools.fragments.permissions.permissions;

import android.Manifest;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

public class CallPermission extends Permission{
    private static final String PERMISSION = Manifest.permission.CALL_PHONE;
    private static final int REQUEST_CODE = 0x04;


    public CallPermission(FragmentActivity fragmentActivity, Context context) {
        super(fragmentActivity, context, PERMISSION, REQUEST_CODE);
    }
}
