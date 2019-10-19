package com.wsinf.multitools;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.wsinf.multitools.fragments.Bluetooth;
import com.wsinf.multitools.fragments.Camera;
import com.wsinf.multitools.fragments.CombustionCalculator;
import com.wsinf.multitools.fragments.Compass;
import com.wsinf.multitools.fragments.EmbeddedSensors;
import com.wsinf.multitools.fragments.EnvironmentalSensors;
import com.wsinf.multitools.fragments.GpsMap;
import com.wsinf.multitools.fragments.GpsSpy;
import com.wsinf.multitools.fragments.LevelSensor;
import com.wsinf.multitools.fragments.Wifi;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        loadCombustionCalculatorFragment();
        navigationView.setCheckedItem(R.id.item_combustion);
    }

    private void loadCombustionCalculatorFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new CombustionCalculator()).commit();
    }

    private void loadLevelSensorFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new LevelSensor()).commit();
    }

    private void loadEmbeddedSensorsFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new EmbeddedSensors()).commit();
    }

    private void loadEnvironmentalSensorsFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new EnvironmentalSensors()).commit();
    }

    private void loadCompassFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Compass()).commit();
    }

    private void loadBluetoothFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Bluetooth()).commit();
    }

    private void loadWifiFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Wifi()).commit();
    }

    private void loadCameraFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Camera()).commit();
    }

    private void loadGpsSpyFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new GpsSpy()).commit();
    }

    private void loadGpsMapFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new GpsMap()).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.item_combustion:
                loadCombustionCalculatorFragment();
                break;
            case R.id.item_level:
                loadLevelSensorFragment();
                break;
            case R.id.item_embedded:
                loadEmbeddedSensorsFragment();
                break;
            case R.id.item_environment:
                loadEnvironmentalSensorsFragment();
                break;
            case R.id.item_compass:
                loadCompassFragment();
                break;
            case R.id.item_bluetooth:
                loadBluetoothFragment();
                break;
            case R.id.item_wifi:
                loadWifiFragment();
                break;
            case R.id.item_camera:
                loadCameraFragment();
                break;
            case R.id.item_gps_spy:
                loadGpsSpyFragment();
                break;
            case R.id.item_gps_map:
                loadGpsMapFragment();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}
