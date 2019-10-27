package com.wsinf.multitools.fragments.permissions.permissions;

import android.Manifest;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

public class ReadExternalStorage extends Permission {
    private static final String PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int REQUEST_CODE = 0x09;


    public ReadExternalStorage(FragmentActivity fragmentActivity, Context context) {
        super(fragmentActivity, context, PERMISSION, REQUEST_CODE);
    }
}
