package com.wsinf.multitools.fragments.permissions.permissions;

import android.Manifest;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

public class SendSmsPermission extends Permission {

    private static final String PERMISSION = Manifest.permission.SEND_SMS;
    private static final int REQUEST_CODE = 0x03;

    public SendSmsPermission(FragmentActivity fragmentActivity, Context context) {
        super(fragmentActivity, context, PERMISSION, REQUEST_CODE);
    }
}
