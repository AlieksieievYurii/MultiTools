package com.wsinf.multitools.fragments.camera;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wsinf.multitools.R;
import com.wsinf.multitools.fragments.camera.utils.CameraInterface;
import com.wsinf.multitools.fragments.camera.utils.CameraService;
import com.wsinf.multitools.fragments.camera.utils.OnGetPicture;

import static com.wsinf.multitools.fragments.camera.ImagePreview.IMAGE_EXTRA;

public class Camera extends Fragment implements View.OnClickListener {

    private static final int PERMISSION_REQUEST_CODE = 1;

    private Context context;
    private CameraInterface cameraService;
    private FloatingActionButton btnTakePicture;
    private FloatingActionButton btnSwitchCamera;
    private FloatingActionButton btnOpenGallery;
    private RelativeLayout rlPermissionView;
    private RelativeLayout rlCameraView;
    private SurfaceView svCanvas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rlPermissionView = view.findViewById(R.id.rl_permission_view);
        rlCameraView = view.findViewById(R.id.rl_camera_view);
        btnTakePicture = view.findViewById(R.id.btn_take_picture);
        btnSwitchCamera = view.findViewById(R.id.btn_change_camera);
        btnOpenGallery = view.findViewById(R.id.btn_open_gallery);
        svCanvas = view.findViewById(R.id.sv_canvas);

        btnSwitchCamera.setOnClickListener(this);
        btnTakePicture.setOnClickListener(this);
        btnOpenGallery.setOnClickListener(this);

        final Button btnPermissionRequest = view.findViewById(R.id.btn_permission_request);
        btnPermissionRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            rlPermissionView.setVisibility(View.VISIBLE);
            rlCameraView.setVisibility(View.GONE);
        } else
            initCameraView();
    }

    private void initCameraView() {
        rlPermissionView.setVisibility(View.GONE);
        rlCameraView.setVisibility(View.VISIBLE);
        cameraService = new CameraService(svCanvas);
    }

    private void takePicture() {
        cameraService.takePicture(new OnGetPicture() {
            @Override
            public void onGetPicture(Bitmap bitmap) {
                final Intent intent = new Intent(context, ImagePreview.class);
                intent.putExtra(IMAGE_EXTRA, bitmap);
                startActivity(intent);
            }
        });
    }

    private void switchCamera() {
        cameraService.switchCamera();
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v == btnTakePicture)
            takePicture();
        else if (v == btnSwitchCamera)
            switchCamera();
        else if (v == btnOpenGallery)
            openGallery();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE &&
                permissions.length > 0 &&
                permissions[0].equals(Manifest.permission.CAMERA) &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED)
            initCameraView();

    }
}
