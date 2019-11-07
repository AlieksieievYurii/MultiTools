package com.wsinf.multitools.fragments.camera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wsinf.multitools.R;
import com.wsinf.multitools.fragments.camera.utils.CameraInterface;
import com.wsinf.multitools.fragments.camera.utils.CameraService;
import com.wsinf.multitools.fragments.camera.utils.OnGetPicture;

import static com.wsinf.multitools.fragments.camera.ImagePreview.IMAGE_EXTRA;

public class Camera extends Fragment implements View.OnClickListener {

    private Context context;
    private CameraInterface cameraService;
    private FloatingActionButton btnTakePicture;
    private FloatingActionButton btnSwitchCamera;
    private FloatingActionButton btnOpenGallery;

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

        final SurfaceView svCanvas = view.findViewById(R.id.sv_canvas);
        cameraService = new CameraService(svCanvas);


        btnTakePicture = view.findViewById(R.id.btn_take_picture);
        btnSwitchCamera = view.findViewById(R.id.btn_change_camera);
        btnOpenGallery = view.findViewById(R.id.btn_open_gallery);

        btnSwitchCamera.setOnClickListener(this);
        btnTakePicture.setOnClickListener(this);
        btnOpenGallery.setOnClickListener(this);


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
}
