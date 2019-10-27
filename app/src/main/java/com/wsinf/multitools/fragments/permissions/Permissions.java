package com.wsinf.multitools.fragments.permissions;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wsinf.multitools.R;
import com.wsinf.multitools.fragments.permissions.permissions.AccessCoarseLocationPermission;
import com.wsinf.multitools.fragments.permissions.permissions.CallPermission;
import com.wsinf.multitools.fragments.permissions.permissions.CameraPermission;
import com.wsinf.multitools.fragments.permissions.permissions.GetAccountsPermission;
import com.wsinf.multitools.fragments.permissions.permissions.LocationPermission;
import com.wsinf.multitools.fragments.permissions.permissions.Permission;
import com.wsinf.multitools.fragments.permissions.permissions.ReadExternalStorage;
import com.wsinf.multitools.fragments.permissions.permissions.SendSmsPermission;
import com.wsinf.multitools.fragments.permissions.permissions.WriteExternalStorage;

import java.util.ArrayList;
import java.util.List;

public class Permissions extends Fragment implements OnPermissionsRequest {

    private Context context;

    private LinearLayout root;

    private List<Permission> permissions;
    private List<ViewItemPermission> permissionsView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        permissions = new ArrayList<>();
        permissionsView = new ArrayList<>();

        permissions.add(new LocationPermission(getActivity(), context));
        permissions.add(new CameraPermission(getActivity(), context));
        permissions.add(new SendSmsPermission(getActivity(), context));
        permissions.add(new CallPermission(getActivity(), context));
        permissions.add(new AccessCoarseLocationPermission(getActivity(), context));
        permissions.add(new GetAccountsPermission(getActivity(), context));
        permissions.add(new WriteExternalStorage(getActivity(), context));
        permissions.add(new ReadExternalStorage(getActivity(), context));
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

      for (Permission p : permissions)
          createItem(p);
    }

    private void createItem(Permission p)
    {
        ViewItemPermission v = new ViewItemPermission(context, p);
        permissionsView.add(v);
        root.addView(v.getView());
    }

    @Override
    public void onRequest(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (ViewItemPermission p : permissionsView)
            p.onRefresh();

    }
}
