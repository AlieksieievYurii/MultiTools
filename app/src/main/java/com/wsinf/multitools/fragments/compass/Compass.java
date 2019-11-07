package com.wsinf.multitools.fragments.compass;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wsinf.multitools.R;

public class Compass extends Fragment implements CompassEvent {

    private ImageView imCompass;
    private TextView tvDegree;

    private MySensor sensor;
    private float mCurrentDegree = 0f;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compass, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imCompass = view.findViewById(R.id.im_compass);
        tvDegree = view.findViewById(R.id.tv_degree);

        sensor = new CompassSensor(getContext(), this);
}

    @Override
    public void onResume() {
        super.onResume();
        sensor.onStart();

    }

    @Override
    public void onPause() {
        super.onPause();
        sensor.onStop();
    }

    @Override
    public void onCompassChange(int degree) {
        tvDegree.setText(String.format("%s C", String.valueOf(degree)));
        RotateAnimation ra = new RotateAnimation(
                mCurrentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        ra.setDuration(250);

        ra.setFillAfter(true);

        imCompass.startAnimation(ra);
        mCurrentDegree = -degree;
    }
}
