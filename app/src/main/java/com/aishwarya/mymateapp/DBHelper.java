package com.aishwarya.mymateapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private Context context;
    public static final String DBNAME = "MymateApp.db";
    private static final String TABLE_NAME1 = "note_table";
    private static final String TABLE_NAME2 = "user_table";

    private static final String TABLE_NAME3 = "videogallery_table";

    private static final String TABLE_NAME4 = "location_table";


    private static final String COL_NID = "NID";
    private static final String COL_TITLE = "TITLE";
    private static final String COL_SUB = "SUBTITLE";
    private static final String COL_DES = "DESCRIPTION";
    private static final String COL_IMAGE = "IMAGE";

    private static final String COL_UID = "UID";
    private static final String COL_UNAME = "USERNAME";
    private static final String COL_PASS = "PASSWORD";
    private static final String COL_DOB = "DATEOFBIRTH";
    private static final String COL_GEN = "GENDER";
    private static final String COL_OCCU = "OCCUPATION";
    private static final String COL_DATE = "DATE";


    public static final String COL_VID = "VID";
    public static final String COL_VIDEOPATH = "VIDEO_PATH";


    private static final String COL_LID = "LID";
    private static final String COL_ADDRESS = "ADDRESS";
    private static final String COL_ADDTITLE = "LOC_TITLE";
    private String address;
    private Location location;
    private Location MyLocation;

    DBHelper(Context context) {
        super(context, "MymateApp.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create table " + TABLE_NAME1 + "(" +
                "NID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "TITLE TEXT, " +
                "SUBTITLE TEXT, " +
                "DESCRIPTION TEXT," +
                "IMAGE BLOB)");

//        IMAGE BLOB


        MyDB.execSQL("create table " + TABLE_NAME2 + "(UID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "USERNAME TEXT, PASSWORD TEXT, DATEOFBIRTH DATE, GENDER TEXT, OCCUPATION TEXT, DATE DATE)");

        MyDB.execSQL(" create table " + TABLE_NAME3 + "(VID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "VIDEO_PATH TEXT UNIQUE)");

        MyDB.execSQL("create table " + TABLE_NAME4 + "(LID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " COL_ADDRESS  TEXT, COL_ADDTITLE TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        MyDB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        MyDB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
        MyDB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME4);
        onCreate(MyDB);
    }


    public Boolean checkusername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME2 + " where username = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean checkusernamepassword(String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME2 + " where username = ? and password = ?", new String[]{username, password});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;

    }

    public Boolean insertUserData(String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues userValues = new ContentValues();
        userValues.put("username", username);
        userValues.put("password", password);
        long result = MyDB.insert("user_table", null, userValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Boolean insertNoteData(String title, String subtitle, String description, byte[] imageData) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues notevalues = new ContentValues();
        notevalues.put(COL_TITLE, title);
        notevalues.put(COL_SUB, subtitle);
        notevalues.put(COL_DES, description);
        notevalues.put(COL_IMAGE, imageData);
//        byte[] imageData
////       notevalues.put(COL_IMAGE, imageData);
        long result1 = MyDB.insert(TABLE_NAME1, null, notevalues);
        if (result1 == -1)
            return false;
        else
            return true;

    }


    public boolean addVideo(String videoPath) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues videovalues = new ContentValues();
        videovalues.put(COL_VIDEOPATH, videoPath);
        long result2 = MyDB.insert(TABLE_NAME3, null, videovalues);


        if (result2 == -1)
            return false;
        else
            return true;
    }

    public boolean insertLocation(String address, String locTitle) {
        this.address = address;
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues locationValues = new ContentValues();
        locationValues.put(COL_ADDRESS, address);
        locationValues.put(COL_ADDTITLE, locTitle);

        long result3 = MyDB.insert(TABLE_NAME4, null, locationValues);
        if (result3 == -1)
            return false;
        else
            return true;
    }

    Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME1;
        SQLiteDatabase MyDB = this.getReadableDatabase();

        Cursor cursor = null;
        if (MyDB != null) {
            cursor = MyDB.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor getVideos() {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        return MyDB.query(TABLE_NAME3, null, null, null, null, null, null);
    }

    boolean UpdateNoteData(String row_id, String title, String subtitle, String description) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues notevalues = new ContentValues();
        notevalues.put(COL_NID, row_id);
        notevalues.put(COL_TITLE, title);
        notevalues.put(COL_SUB, subtitle);
        notevalues.put(COL_DES, description);
//        notevalues.put(COL_IMAGE, imageData);


        long result2 = MyDB.update(TABLE_NAME1, notevalues, "NID=?", new String[]{row_id});
        if (result2 == -1) {
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Updated", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    public void deleteVideoFromDB(String id) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        MyDB.delete(TABLE_NAME3, COL_VID + "=?", new String[]{String.valueOf(id)});
        MyDB.close();
    }


    public ArrayList<String> retrieveVideoPaths() {
        ArrayList<String> videoPaths = new ArrayList<>();

        SQLiteDatabase MyDB = this.getWritableDatabase();
        String query = "SELECT " + COL_VIDEOPATH + " FROM " + TABLE_NAME3;

        Cursor cursor = MyDB.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String videoPath = cursor.getString(cursor.getColumnIndex(COL_VIDEOPATH));
                videoPaths.add(videoPath);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return videoPaths;
    }

    public List<com.aishwarya.mymateapp.MyLocation> getAllLocations() {
        List<Location> locationList = new ArrayList<>();

        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM " + TABLE_NAME4, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int locationId = cursor.getInt(cursor.getColumnIndex(COL_LID));
                @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex(COL_ADDRESS));
                @SuppressLint("Range") String locTitle = cursor.getString(cursor.getColumnIndex(COL_ADDTITLE));

                MyLocation location = new MyLocation(locationId, address, locTitle);
                locationList.add(MyLocation);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return null;
    }


    public ArrayList<String> getLocationTitles() {
        ArrayList<String> locationTitles = new ArrayList<>();
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.query(TABLE_NAME4, new String[]{COL_ADDTITLE}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(COL_ADDTITLE));
                locationTitles.add(title);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return locationTitles;
    }

}

