package com.aishwarya.mymateapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BrightnessAdjustmentPage extends AppCompatActivity implements SensorEventListener {

    private SeekBar brightnessSeekBar;
    private SensorManager sensorManager;
    private Sensor lightSensor;

    private static final int REQUEST_CODE_WRITE_SETTINGS = 1;

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.home:
                            startActivity(new Intent(BrightnessAdjustmentPage.this, HomePage.class));
                            finish(); // Optional: Finish the current activity to prevent going back to it
                            return true;

                        case R.id.brightness:

                            return true;

                        case R.id.location:
                            startActivity(new Intent(BrightnessAdjustmentPage.this, LocationPage.class));
                            finish(); // Optional: Finish the current activity to prevent going back to it
                            return true;

                        case R.id.video:
                            startActivity(new Intent(BrightnessAdjustmentPage.this, VideogalleyPage.class));
                            finish(); // Optional: Finish the current activity to prevent going back to it
                            return true;
                    }
                    return false;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brightness_adjustment_page);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        brightnessSeekBar = findViewById(R.id.brightnessSeekBar);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        brightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    adjustScreenBrightness(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private void adjustScreenBrightness(int brightness) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED) {
            ContentResolver contentResolver = getContentResolver();
            Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);

            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.screenBrightness = brightness / 255f;
            getWindow().setAttributes(layoutParams);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_SETTINGS}, REQUEST_CODE_WRITE_SETTINGS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_WRITE_SETTINGS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                adjustScreenBrightness(brightnessSeekBar.getProgress());
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lightLevel = event.values[0];
            int brightness = (int) (lightLevel / SensorManager.LIGHT_SUNLIGHT_MAX * 255);
            brightnessSeekBar.setProgress(brightness);
            adjustScreenBrightness(brightness);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}