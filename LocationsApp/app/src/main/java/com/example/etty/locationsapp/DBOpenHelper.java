package com.example.etty.locationsapp;


import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper{

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "places.db";


    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlPlaces =
                "CREATE TABLE " + PlacesContract.Places.TABLE_NAME
                +"("
                +PlacesContract.Places._ID+"  INTEGER PRIMARY KEY AUTOINCREMENT ,"
                +PlacesContract.Places.NAME+" TEXT ,"
                +PlacesContract.Places.ADDRESS+" TEXT ,"
                +PlacesContract.Places.LOCATION+" TEXT ,"
                +PlacesContract.Places.DISTANCE+" REAL ,"
                +PlacesContract.Places.PHOTO+" TEXT "
                +") ";
        db.execSQL(sqlPlaces);

        String sqlHistory =
                "CREATE TABLE " + PlacesContract.History.TABLE_NAME
                        +"("
                        +PlacesContract.History._ID+"  INTEGER PRIMARY KEY AUTOINCREMENT ,"
                        +PlacesContract.History.NAME+" TEXT ,"
                        +PlacesContract.History.ADDRESS+" TEXT ,"
                        +PlacesContract.History.LOCATION+" TEXT ,"
                        +PlacesContract.History.DISTANCE+" REAL ,"
                        +PlacesContract.History.PHOTO+" TEXT "
                        +") ";
        db.execSQL(sqlHistory);


        String sqlFavorites =
                "CREATE TABLE " + PlacesContract.Favorites.TABLE_NAME
                        +"("
                        +PlacesContract.Favorites._ID+"  INTEGER PRIMARY KEY AUTOINCREMENT ,"
                        +PlacesContract.Favorites.NAME+" TEXT ,"
                        +PlacesContract.Favorites.ADDRESS+" TEXT ,"
                        +PlacesContract.Favorites.LOCATION+" TEXT ,"
                        +PlacesContract.Favorites.DISTANCE+" REAL ,"
                        +PlacesContract.Favorites.PHOTO+" TEXT "
                        +") ";
        db.execSQL(sqlFavorites);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + PlacesContract.Places.TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

}
