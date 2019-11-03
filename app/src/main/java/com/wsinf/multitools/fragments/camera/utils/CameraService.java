package com.wsinf.multitools.fragments.camera.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraService implements CameraInterface, SurfaceHolder.Callback {

    private static final String TAG = "CameraService";

    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private boolean pending;
    private int currentCameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;

    public CameraService(SurfaceView svCanvas) {
        this.surfaceHolder = svCanvas.getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public void takePicture(final OnGetPicture onGetPicture) {
        if (!pending) {
            pending = true;
            camera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    onGetPicture.onGetPicture(convert(data));
                    refresh();
                }
            });
        }
    }

    private Bitmap convert(final byte[] data) {
        final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return bitmap;
    }

    @Override
    public void switchCamera() {
        onStopCamera();

        if (currentCameraFacing == Camera.CameraInfo.CAMERA_FACING_BACK)
            currentCameraFacing = Camera.CameraInfo.CAMERA_FACING_FRONT;
        else if (currentCameraFacing == Camera.CameraInfo.CAMERA_FACING_FRONT)
            currentCameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
        else
            throw new IllegalStateException("Unknown camera facing state: " + currentCameraFacing);

        onStartCamera();
    }

    private void refresh() {
        camera.stopPreview();
        camera.startPreview();
        pending = false;
    }


    private void onStartCamera() {
        try {
            camera = Camera.open(currentCameraFacing);
            camera.setDisplayOrientation(90);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Error of opening the camera: " + e.getMessage());
        }

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error of preview the camera: " + e.getMessage());
        }
    }


    private void onStopCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        onStartCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        onStopCamera();
    }
}
