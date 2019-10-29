package com.wsinf.multitools.fragments.permissions;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

class Permission {
    private FragmentActivity fragmentActivity;
    private Context context;
    private String perm;
    private int requestCode;

    Permission(final FragmentActivity fragmentActivity,
                      final Context context,
                      final String perm,
                      final int requestCode) {
        this.context = context;
        this.fragmentActivity = fragmentActivity;
        this.requestCode = requestCode;

        if (perm == null)
            throw new IllegalArgumentException("Permission is null");
        else
            this.perm = perm;
    }

    boolean isPermissionGranted() {
        return ActivityCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED;
    }

    void requestPermission() {
        ActivityCompat.requestPermissions(fragmentActivity, new String[]{perm}, requestCode);
    }

    String getPermissionName() {
        return perm.split("\\.")[2];
    }
}
