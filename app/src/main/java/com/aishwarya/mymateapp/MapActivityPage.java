package com.aishwarya.mymateapp;

import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapActivityPage extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marker;
    private LatLng selectedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_page);



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        Button buttonSaveLocation = findViewById(R.id.buttonSaveLocation);
        buttonSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLocation();
            }


        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {


        mMap = googleMap;

        // Set a marker at the default location
        LatLng defaultLocation = new LatLng(37.7749, -122.4194); // Example: San Francisco
        marker = mMap.addMarker(new MarkerOptions().position(defaultLocation).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12.0f));

        // Set a marker drag listener
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                // No action needed
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                // No action needed
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                selectedLocation = marker.getPosition();
            }
        });

        // Set a map click listener
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                marker.setPosition(latLng);
                selectedLocation = latLng;
            }
        });

    }

    private void saveLocation() {

        if (selectedLocation != null) {
            // Show an AlertDialog to prompt the user to enter a location title
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter Location Title");

            // Set up the input
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String locationTitle = input.getText().toString().trim();
                    if (!locationTitle.isEmpty()) {
                        // Insert the location into the database using DBHelper
                        DBHelper DB = new DBHelper(MapActivityPage.this);
                        boolean insertResult = DB.insertLocation(getAddressFromLatLng(MapActivityPage.this, selectedLocation), locationTitle);

                        if (insertResult) {
                            // Insertion successful
                            Toast.makeText(MapActivityPage.this, "Location inserted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Insertion failed
                            Toast.makeText(MapActivityPage.this, "Failed to insert location", Toast.LENGTH_SHORT).show();
                        }

                        // Finish the activity
                        finish();
                    } else {
                        Toast.makeText(MapActivityPage.this, "Please enter a location title", Toast.LENGTH_SHORT).show();
                    }
                }



            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }

    }

    private String getAddressFromLatLng(Context context, LatLng latLng) {
        Geocoder geocoder = new Geocoder(context);
        String address = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address fetchedAddress = addresses.get(0);
                StringBuilder addressBuilder = new StringBuilder();

                // Concatenate address lines
                for (int i = 0; i <= fetchedAddress.getMaxAddressLineIndex(); i++) {
                    addressBuilder.append(fetchedAddress.getAddressLine(i));
                    if (i < fetchedAddress.getMaxAddressLineIndex()) {
                        addressBuilder.append(", ");
                    }
                }

                address = addressBuilder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }



//
}