package com.wsinf.multitools.fragments.permissions.permissions;

import android.Manifest;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

public class GetAccountsPermission extends Permission {
    private static final String PERMISSION = Manifest.permission.GET_ACCOUNTS;
    private static final int REQUEST_CODE = 0x07;


    public GetAccountsPermission(FragmentActivity fragmentActivity, Context context) {
        super(fragmentActivity, context, PERMISSION, REQUEST_CODE);
    }
}
