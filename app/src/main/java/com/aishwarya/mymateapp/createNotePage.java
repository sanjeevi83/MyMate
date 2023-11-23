package com.aishwarya.mymateapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class createNotePage extends AppCompatActivity {
    String READ_EXTERNAL_STORAGE;
    ImageView Back, Done, imageCamera;
    EditText editTitle, editSubtitle, editDescription;

    Uri selectedImageUri;


    LinearLayout layout5;
    DBHelper DB;

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_PICK_CODE = 1001;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note_page);

        Back = (ImageView) findViewById(R.id.imageBack);
        Done = (ImageView) findViewById(R.id.imageSave);

        editTitle = findViewById(R.id.textTitle);
        editSubtitle = findViewById(R.id.textSubtitle);
        editDescription = findViewById(R.id.textDescription);

// imageView = findViewById(R.id.imageView);

        DB = new DBHelper(this);
        addData();

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent);
            }
        });

        //        For image
        layout5 = findViewById(R.id.layout5);
        imageCamera = findViewById(R.id.imageCamera);
        imageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        pickImagefromGallery();
                    }
                } else {
                    pickImagefromGallery();
                }
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    pickImagefromGallery();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void pickImagefromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    private void StartActivityForResult(Intent intent, int imagePickCode) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            Uri imageUri = data.getData();
            selectedImageUri = imageUri;
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                addImageToLayout(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void addImageToLayout(Bitmap bitmap) {
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(10, 10, 10, 10);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageBitmap(bitmap);
        layout5.addView(imageView);
    }


    public void addData() {
        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the note data from the EditText fields
                String title = editTitle.getText().toString();
                String subtitle = editSubtitle.getText().toString();
                String description = editDescription.getText().toString();

                // Check if an image is selected
                if (selectedImageUri != null) {
                    // Convert the image URI to byte array
                    byte[] imageData = convertImageUriToByteArray(selectedImageUri);

                    // Insert the note data along with the image data
                    boolean isInserted = DB.insertNoteData(title, subtitle, description, imageData);

                    if (isInserted) {
                        Toast.makeText(createNotePage.this, "Data Inserted Successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(createNotePage.this, "Data Not Inserted Successfully", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Insert the note data without the image
                    boolean isInserted = DB.insertNoteData(title, subtitle, description, new byte[0]);

                    if (isInserted) {
                        Toast.makeText(createNotePage.this, "Data Inserted Successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(createNotePage.this, "Data Not Inserted Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private byte[] convertImageUriToByteArray(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}



