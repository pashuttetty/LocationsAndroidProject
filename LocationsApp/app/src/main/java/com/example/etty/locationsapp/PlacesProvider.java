package com.example.etty.locationsapp;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.List;

public class PlacesProvider extends ContentProvider{
    DBOpenHelper dbOpenHelper;
    @Override
    public boolean onCreate() {
        dbOpenHelper = new DBOpenHelper(getContext());
        return false;
    }

    String getTableName(Uri uri)
    {
        //get table name from URI
        List <String> pathSegments= uri.getPathSegments();
        return  pathSegments.get(0);
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {


        Cursor cursor = dbOpenHelper.getReadableDatabase().query(getTableName(uri),projection,selection,selectionArgs,null,null,sortOrder);
        //getContext().getContentResolver().notifyChange(uri, null);
        cursor.setNotificationUri(getContext().getContentResolver(), PlacesContract.Places.CONTENT_URI);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        long id = db.insertWithOnConflict(getTableName(uri), null, values, SQLiteDatabase.CONFLICT_REPLACE);

        // notify the change
        getContext().getContentResolver().notifyChange(uri, null);

        if (id > 0) {
            return ContentUris.withAppendedId(uri, id);
        } else {
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int result = db.delete(getTableName(uri), selection, selectionArgs);


        // notify the change
        getContext().getContentResolver().notifyChange(uri, null);

        //return the number of rows deleted
        return result;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

       /* SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int result = db.updateWithOnConflict(getTableName(uri), values, selection, selectionArgs, SQLiteDatabase.CONFLICT_REPLACE);

        // notify the change
        getContext().getContentResolver().notifyChange(uri, null);*/

        return 0;
    }
}
