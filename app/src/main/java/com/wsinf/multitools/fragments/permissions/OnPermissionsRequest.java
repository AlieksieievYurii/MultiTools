package com.wsinf.multitools.fragments.permissions;

import androidx.annotation.NonNull;

public interface OnPermissionsRequest
{
    void onRequest(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
