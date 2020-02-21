package com.example.projectnotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseNotes extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NotesDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "notes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_NOTESTITLE = "note";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";

    public DatabaseNotes(@Nullable Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER NOT NULL CONSTRAINT note_pk PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CATEGORY + " varchar(200) NOT NULL, " +
                COLUMN_NOTESTITLE + " varchar(200) NOT NULL, " +
                COLUMN_DESCRIPTION + " varchar(200) NOT NULL, " +
                COLUMN_DATE + " varchar(200) NOT NULL, " +
                COLUMN_LATITUDE + " double NOT NULL, " +
                COLUMN_LONGITUDE + " double NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        db.execSQL(sql);
        onCreate(db);
    }

    boolean addNotes(String category, String noteTitle, String description, String date, double lat, double lng){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_CATEGORY, category);
        contentValues.put(COLUMN_NOTESTITLE, noteTitle);
        contentValues.put(COLUMN_DESCRIPTION, description);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_LATITUDE, lat);
        contentValues.put(COLUMN_LONGITUDE, lng);

        return sqLiteDatabase.insert(TABLE_NAME,null,contentValues) != -1;
    }



    Cursor getAllCategories(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT DISTINCT " + COLUMN_CATEGORY + " FROM " + TABLE_NAME, null);
    }

    Cursor getAllNotes(String cat){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * " + " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_CATEGORY + "=?", new String[]{cat});
    }
//
//    Cursor getLatLng(){
//        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
//        return sqLiteDatabase.rawQuery( "SELECT ")
//    }


    boolean updateNote(int id, String category, String noteTitle, String description, double lat, double lng){

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CATEGORY, category);
        contentValues.put(COLUMN_NOTESTITLE, noteTitle);
        contentValues.put(COLUMN_DESCRIPTION, description);
        contentValues.put(COLUMN_LATITUDE, lat);
        contentValues.put(COLUMN_LONGITUDE, lng);

        return sqLiteDatabase.update(TABLE_NAME,contentValues,COLUMN_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }
//
    boolean deleteNote(int id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }
}


