package com.wsinf.multitools.fragments.permissions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wsinf.multitools.R;

public class ViewItemPermission implements View.OnClickListener, OnRefreshStatus {
    private Context context;

    private Permission permission;
    private View view;

    private View vGranted;
    private View vForbidden;

    ViewItemPermission(Context context, Permission permission) {
        this.context = context;
        this.permission = permission;
        init();
    }

    private void init() {
        view = LayoutInflater.from(context).inflate(R.layout.permission_item, null);

        final TextView tvPermissionName = view.findViewById(R.id.tv_permission_name);
        final Button btnRequest = view.findViewById(R.id.btn_request);
        btnRequest.setOnClickListener(this);
        vGranted = view.findViewById(R.id.granted);
        vForbidden = view.findViewById(R.id.forbidden);

        tvPermissionName.setText(permission.getPermissionName());

        onRefresh();
    }

    @Override
    public void onRefresh() {
        if (permission.isPermissionGranted()) {
            vGranted.setVisibility(View.VISIBLE);
            vForbidden.setVisibility(View.GONE);
        } else {
            vGranted.setVisibility(View.GONE);
            vForbidden.setVisibility(View.VISIBLE);
        }
    }

    View getView() {
        return view;
    }

    @Override
    public void onClick(View v) {
        permission.requestPermission();
    }
}
