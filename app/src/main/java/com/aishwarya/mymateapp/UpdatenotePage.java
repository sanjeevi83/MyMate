package com.aishwarya.mymateapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class UpdatenotePage extends AppCompatActivity {
    EditText editTitle, editSubtitle, editDescription;
    ImageView Back, Done;

    DBHelper DB;
    String id, title, subtitle, description;
     ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatenote_page);

        editTitle = findViewById(R.id.textTitle2);
        editSubtitle = findViewById(R.id.textSubtitle2);
        editDescription = findViewById(R.id.textDescription2);
        Back = findViewById(R.id.imageBack);
        Done = findViewById(R.id.imageUpdate);

        //first we cll this
        getAndSetIntentData();
//        UpdateData();


        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper DB = new DBHelper(UpdatenotePage.this);
                title = editTitle.getText().toString().trim();
                subtitle = editSubtitle.getText().toString().trim();
                description = editDescription.getText().toString().trim();

                DB.UpdateNoteData(id, title, subtitle, description);
            }
        });

    }


    void getAndSetIntentData () {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("title") && getIntent().hasExtra("subtitle") &&
                getIntent().hasExtra("description")) {
            //getting data from intent
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            subtitle = getIntent().getStringExtra("subtitle");
            description = getIntent().getStringExtra("description");

            //setting intent data

            editTitle.setText(title);
            editSubtitle.setText(subtitle);
            editDescription.setText(description);


        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }
}












