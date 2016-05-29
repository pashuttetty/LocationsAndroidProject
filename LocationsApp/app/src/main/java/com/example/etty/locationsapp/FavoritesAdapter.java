package com.example.etty.locationsapp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class FavoritesAdapter extends CursorAdapter {
    public FavoritesAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        cursor.setNotificationUri(context.getContentResolver(), PlacesContract.Favorites.CONTENT_URI);
        LayoutInflater inflater= LayoutInflater.from(context);
        View singleItem=  inflater.inflate(R.layout.single_list_item, parent, false);
        return singleItem;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameTV = (TextView)view.findViewById(R.id.nameTV);
        nameTV.setText(cursor.getString(cursor.getColumnIndex(PlacesContract.Favorites.NAME)));

        TextView addressTV = (TextView)view.findViewById(R.id.addressTV);
        addressTV.setText("Address: "+cursor.getString(cursor.getColumnIndex(PlacesContract.Favorites.ADDRESS)));

        TextView distanceTV = (TextView)view.findViewById(R.id.distanceTV);
        //TODO: dynamically switch between m to miles:
        distanceTV.setText("Distance: "+cursor.getString(cursor.getColumnIndex(PlacesContract.Favorites.DISTANCE))+" Meters away");

        /*ImageView placeImageView = (ImageView)view.findViewById(R.id.placeImageView);
        String imageURL =cursor.getString(cursor.getColumnIndex(PlacesContract.Places.PHOTO));*/

        String imageAtBase64= cursor.getString(cursor.getColumnIndex(PlacesContract.Favorites.PHOTO));
        ImageView iv= (ImageView) view.findViewById(R.id.placeImageView);

        Bitmap b = base64ToBitmap(imageAtBase64);
        if(b==null){
            iv.setImageResource(R.mipmap.noimage);
        }else {
            iv.setImageBitmap(b);
        }


    }

    private Bitmap base64ToBitmap(String b64) {

        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

}