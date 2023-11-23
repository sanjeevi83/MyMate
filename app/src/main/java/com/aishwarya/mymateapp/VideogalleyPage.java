package com.aishwarya.mymateapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VideogalleyPage extends AppCompatActivity {

    private static final int PICK_VIDEO_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;

    private ArrayList<String> videoPaths;
    private ArrayAdapter<String> videoAdapter;
    private ListView videoListView;
    private VideoView videoViewer;

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.home:
                            startActivity(new Intent(VideogalleyPage.this, HomePage.class));
                            finish(); // Optional: Finish the current activity to prevent going back to it
                            return true;

                        case R.id.brightness:
                            // Adjust brightness based on ambient light levels
                            startActivity(new Intent(VideogalleyPage.this, BrightnessAdjustmentPage.class));
                            finish(); // Optional: Finish the current activity to prevent going back to it
                            return true;

                        case R.id.location:
                            startActivity(new Intent(VideogalleyPage.this, LocationPage.class));
                            finish(); // Optional: Finish the current activity to prevent going back to it
                            return true;

                        case R.id.video:
                            // You are already in the VideogalleyPage, no need to start the activity again
                            return true;
                    }
                    return false;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videogalley_page);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        videoPaths = new ArrayList<>();
        videoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, videoPaths);
        videoListView = findViewById(R.id.video_list);
        videoListView.setAdapter(videoAdapter);
        videoViewer = findViewById(R.id.video_viewer);

        DBHelper DB = new DBHelper(VideogalleyPage.this);
        ArrayList<String> retrievedVideoPaths = DB.retrieveVideoPaths();
        if (retrievedVideoPaths != null) {
            videoPaths.addAll(retrievedVideoPaths);
            videoAdapter.notifyDataSetChanged();
        }

        Button btnVideo1 = findViewById(R.id.btnVideo1);
        btnVideo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndOpenVideoChooser();
            }

        });
        videoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String videoPath = videoPaths.get(position);
                playVideo(videoPath);
            }
        });

        videoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteConfirmationDialog(id);
                return true;
            }
        });
    }


    private void showDeleteConfirmationDialog(final long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Video");
        builder.setMessage("Are you sure you want to delete this video?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteVideo((int) id);
            }
        });
        builder.setNegativeButton("No", null);
        builder.create().show();
    }

    private void deleteVideo(int position) {
        String videoPath = videoPaths.get(position);
        DBHelper DB = new DBHelper(VideogalleyPage.this);
        DB.deleteVideoFromDB(videoPath);

        videoPaths.remove(position);
        videoAdapter.notifyDataSetChanged();
    }

    private void playVideo(String videoPath) {

        videoViewer.setVideoURI(Uri.parse(videoPath));
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoViewer);
        videoViewer.setMediaController(mediaController);
        videoViewer.start();
    }
        private void checkPermissionAndOpenVideoChooser() {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
            } else {
                openVideoChooser();
            }
        }

    private void openVideoChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openVideoChooser();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri videoUri = data.getData();
            String videoPath = saveVideoToInternalStorage(videoUri); // Save video to internal storage and get the path

            DBHelper DB = new DBHelper(VideogalleyPage.this);
            boolean inserted = DB.addVideo(videoPath);

            if (inserted) {
                videoPaths.add(videoPath);
                videoAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Failed to insert video into the database", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String saveVideoToInternalStorage(Uri videoUri) {
        String filePath;
        try {
            InputStream inputStream = getContentResolver().openInputStream(videoUri);
            File videoFile = createVideoFile();
            if (videoFile != null) {
                filePath = videoFile.getAbsolutePath();
                FileOutputStream outputStream = new FileOutputStream(videoFile);
                copyFile(inputStream, outputStream);
                outputStream.close();
                inputStream.close();
                return filePath;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File createVideoFile() throws IOException {
        String timeStamp;
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String videoFileName = "VIDEO_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        return File.createTempFile(videoFileName, ".mp4", storageDir);
    }

    private void copyFile(InputStream inputStream, FileOutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
    }


}