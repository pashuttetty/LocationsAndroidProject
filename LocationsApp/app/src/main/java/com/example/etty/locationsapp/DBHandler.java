package com.example.etty.locationsapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBHandler {
    private DBOpenHelper myDBHelper;

    public DBHandler(Context context) {
     myDBHelper = new DBOpenHelper(context);
     myDBHelper.getReadableDatabase();


    }

    public static void insertPlace(PlaceModel place, Context context) {

        DBOpenHelper dbOpenHelper= new DBOpenHelper(context);
        ContentValues cv = new ContentValues();
        cv.put(PlacesContract.Places.NAME,place.getName());
        cv.put(PlacesContract.Places.ADDRESS,place.getAddress());
        cv.put(PlacesContract.Places.DISTANCE, place.getDistance());
        cv.put(PlacesContract.Places.LOCATION, place.getLocation());
        cv.put(PlacesContract.Places.PHOTO, place.getImageURL());

        //dbOpenHelper.getWritableDatabase().insert(PlacesContract.Places.TABLE_NAME, null, cv);
        context.getContentResolver().insert(PlacesContract.Places.CONTENT_URI, cv);

    }

    public static void insertFavoritePlace(PlaceModel place, Context context) {

        DBOpenHelper dbOpenHelper= new DBOpenHelper(context);
        ContentValues cv = new ContentValues();
        cv.put(PlacesContract.Favorites.NAME,place.getName());
        cv.put(PlacesContract.Favorites.ADDRESS,place.getAddress());
        cv.put(PlacesContract.Favorites.DISTANCE, place.getDistance());
        cv.put(PlacesContract.Favorites.LOCATION, place.getLocation());
        cv.put(PlacesContract.Favorites.PHOTO, place.getImageURL());

        //dbOpenHelper.getWritableDatabase().insert(PlacesContract.Places.TABLE_NAME, null, cv);
        context.getContentResolver().insert(PlacesContract.Favorites.CONTENT_URI,cv);

    }

    public static void insertPlaceToHistory(PlaceModel place, Context context) {

        DBOpenHelper dbOpenHelper= new DBOpenHelper(context);
        ContentValues cv = new ContentValues();
        cv.put(PlacesContract.History.NAME,place.getName());
        cv.put(PlacesContract.History.ADDRESS,place.getAddress());
        cv.put(PlacesContract.History.DISTANCE, place.getDistance());
        cv.put(PlacesContract.History.LOCATION, place.getLocation());
        cv.put(PlacesContract.History.PHOTO, place.getImageURL());

        //dbOpenHelper.getWritableDatabase().insert(PlacesContract.Places.TABLE_NAME, null, cv);
        context.getContentResolver().insert(PlacesContract.History.CONTENT_URI,cv);

    }


    public static Cursor getAllLocations(Context context) {
        DBOpenHelper db = new DBOpenHelper(context);
        Cursor cursor = db.getReadableDatabase().query(PlacesContract.Places.TABLE_NAME, null,null,null,null,null,null);
        return cursor;
    }

    public static void deleteAllPlacesTable(Context context)
    {
        DBOpenHelper db = new DBOpenHelper(context);
        db.getWritableDatabase().execSQL("DELETE FROM " + PlacesContract.Places.TABLE_NAME);
        //db.close();
    }

    public static void deleteAllFavoritesTable(Context context)
    {
        DBOpenHelper db = new DBOpenHelper(context);
        db.getWritableDatabase().execSQL("DELETE FROM " + PlacesContract.Favorites.TABLE_NAME);
        //db.close();
    }


}
