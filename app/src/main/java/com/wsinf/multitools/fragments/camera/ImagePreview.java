package com.wsinf.multitools.fragments.camera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.wsinf.multitools.R;
import com.wsinf.multitools.fragments.camera.utils.BitmapTools;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Objects;

public class ImagePreview extends AppCompatActivity implements View.OnClickListener {

    public static final String IMAGE_EXTRA = "image";

    private ImageView imageView;
    private Button btnSave;
    private Button btnStir;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        imageView = findViewById(R.id.img_canvas);
        btnSave = findViewById(R.id.btn_save);
        btnStir = findViewById(R.id.btn_stir);
        btnSave.setOnClickListener(this);
        btnStir.setOnClickListener(this);

        final Intent intent = getIntent();

        bitmap = intent.getParcelableExtra(IMAGE_EXTRA);

        if (bitmap != null) {
            rotateImage();
            imageView.setImageBitmap(bitmap);
        }
    }

    private void rotateImage() {
        Matrix matrix = new Matrix();

        matrix.postRotate(90);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        bitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
    }

    private void savePicture() {
        String filename = String.format("%s.jpeg", Calendar.getInstance().getTime().toString());
        File sd = Environment.getExternalStorageDirectory();
        File dest = new File(sd, filename);

        try {
            FileOutputStream out = new FileOutputStream(dest);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }

    private void stirPicture() {
        Bitmap[][] bitmaps = BitmapTools.separate(bitmap, 5);
        BitmapTools.shuffle(bitmaps);
        bitmap = BitmapTools.blindBitmaps(bitmaps);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSave)
            savePicture();
        else if (v == btnStir)
            stirPicture();
    }


}
