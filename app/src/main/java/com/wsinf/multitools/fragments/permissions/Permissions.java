package com.wsinf.multitools.fragments.permissions;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wsinf.multitools.R;

import java.util.ArrayList;
import java.util.List;

public class Permissions extends Fragment {

    private Context context;

    private LinearLayout root;

    private List<Permission> permissionObjects;
    private List<ViewItemPermission> permissionsView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        permissionObjects = new ArrayList<>();
        permissionsView = new ArrayList<>();

        try {
            final String[] permission = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_PERMISSIONS).requestedPermissions;

            for (String permissionName : permission)
                permissionObjects.add(new Permission(this, context, permissionName, 0));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_permissions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        root = view.findViewById(R.id.root);

      for (Permission p : permissionObjects)
          createItem(p);
    }

    private void createItem(Permission p)
    {
        final ViewItemPermission v = new ViewItemPermission(context, p);
        permissionsView.add(v);
        root.addView(v.getView());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (ViewItemPermission p : permissionsView)
            p.onRefresh();
    }
}
