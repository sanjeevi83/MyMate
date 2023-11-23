package com.aishwarya.mymateapp;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class LocationPage extends AppCompatActivity {

    private static final int MAP_REQUEST_CODE = 1;
    private TextView locationTextView;
    private Button selectLocationButton;
    private Object address;
    private ListView listView;
    private List<Location> locationList;
    private DBHelper DB;
    private ArrayList<Object> locationTitles;
    private ArrayAdapter<Object> adapter;
    private boolean selectedLocation;

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.home:
                            startActivity(new Intent(LocationPage.this, HomePage.class));
                            finish(); // Optional: Finish the current activity to prevent going back to it
                            return true;

                        case R.id.brightness:
                            // Adjust brightness based on ambient light levels
                            startActivity(new Intent(LocationPage.this, BrightnessAdjustmentPage.class));
                            finish(); // Optional: Finish the current activity to prevent going back to it
                            return true;

                        case R.id.location:

                            return true;

                        case R.id.video:
                            startActivity(new Intent(LocationPage.this, VideogalleyPage.class));
                            finish(); // Optional: Finish the current activity to prevent going back to it
                            return true;
                    }
                    return false;
                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_page);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        DB = new DBHelper(this);
        ListView listView = findViewById(R.id.listView1);
        ArrayList<String> locationTitles = getLocationTitles();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, locationTitles);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedLocation = locationTitles.get(position);
                selectedMapLocation(selectedLocation);
            }

        });


        selectLocationButton = findViewById(R.id.buttonSelectLocation);
        selectLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapActivity();
            }
        });


    }

    private void selectedMapLocation(String selectedLocation) {
        Intent intent = new Intent(this, MapActivityPage.class);
        intent.putExtra("selectedLocation", selectedLocation);
        startActivityForResult(intent, MAP_REQUEST_CODE);
    }


    private void openMapActivity() {
        Intent intent = new Intent(this, MapActivityPage.class);
        startActivityForResult(intent, MAP_REQUEST_CODE);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MAP_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            double latitude = data.getDoubleExtra("latitude", 0.0);
            double longitude = data.getDoubleExtra("longitude", 0.0);
            String address = data.getStringExtra("address");

            String selectedLocation = "Latitude: " + latitude + ", Longitude: " + longitude;

            locationTextView.setText(selectedLocation);

            // Insert the address and selectedLocation into the location table in the database using DBHelper
            DBHelper DB = new DBHelper(LocationPage.this);
            boolean insertResult = DB.insertLocation(address, selectedLocation);

            if (insertResult) {
                // Insertion successful
                Toast.makeText(this, "Location inserted successfully", Toast.LENGTH_SHORT).show();
            } else {
                // Insertion failed
                Toast.makeText(this, "Failed to insert location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ArrayList<String> getLocationTitles() {
        return DB.getLocationTitles();

    }
}
