package com.aishwarya.mymateapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton fab_button;
    DBHelper DB;
    ArrayList<String>  note_id, note_title, note_subtitle, note_description;
    CustomAdapter customAdapter;

    ArrayList<byte[]> note_images;

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.home:
                            startActivity(new Intent(HomePage.this, HomePage.class));
                            finish();
                            return true;

                        case  R.id.brightness:
                            // Adjust brightness based on ambient light levels
                            adjustBrightnessBasedOnLight();
                            return true;
                        case R.id.location:
                            startActivity(new Intent(HomePage.this, LocationPage.class));

                            return true;

                        case R.id.video:
//                            startActivity(new Intent(HomePage.this, VideogalleyPage.class));
                            startActivity(new Intent(HomePage.this, VideogalleyPage.class));
                            return true;
                    }
                    return false;
                }

            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        recyclerView = findViewById(R.id.recyclerView);
        fab_button = findViewById(R.id.fab);

        fab_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, createNotePage.class);
                startActivity(intent);
            }
        });

        DB = new DBHelper(HomePage.this);
        note_id = new ArrayList<>();
        note_title = new ArrayList<>();
        note_subtitle = new ArrayList<>();
        note_description = new ArrayList<>();
        note_images = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter(HomePage.this, note_id, note_title, note_subtitle, note_description);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true));


        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


    }

    void storeDataInArrays() {



        Cursor cursor = DB.readAllData();
        if (cursor.getCount()==0){
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()){
                note_id.add(cursor.getString(0));
                note_title.add(cursor.getString(1));
                note_subtitle.add(cursor.getString(2));
                note_description.add(cursor.getString(3));
                note_images.add(cursor.getBlob(4));
            }
        }

    }

    private void adjustBrightnessBasedOnLight() {
        // Create an Intent to start the BrightnessAdjustmentActivity
        Intent intent = new Intent(HomePage.this, BrightnessAdjustmentPage.class);
        startActivity(intent);
    }

}