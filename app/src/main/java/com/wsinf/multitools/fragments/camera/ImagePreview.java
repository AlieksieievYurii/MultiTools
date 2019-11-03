package com.wsinf.multitools.fragments.camera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.wsinf.multitools.R;

import java.util.Objects;

public class ImagePreview extends AppCompatActivity {

    public static final String IMAGE_EXTRA = "image";

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        imageView = findViewById(R.id.img_canvas);

        final Intent intent = getIntent();

        final Bitmap bitmap = intent.getParcelableExtra(IMAGE_EXTRA);

        if (bitmap != null)
            setImage(bitmap);
    }

    private void setImage(final Bitmap bitmap) {
        Matrix matrix = new Matrix();

        matrix.postRotate(90);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        imageView.setImageBitmap(rotatedBitmap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }
}
