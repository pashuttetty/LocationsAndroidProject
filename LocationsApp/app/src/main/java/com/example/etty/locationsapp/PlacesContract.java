package com.example.etty.locationsapp;


import android.net.Uri;

public class PlacesContract {
    public static final String AUTHORITY = "com.example.etty.locationsapp.provider";

    public static class Places {



        public static String TABLE_NAME="places";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/"+TABLE_NAME);
        public static String _ID="_id";
        public static String NAME="name";
        public static String ADDRESS="address";
        public static String DISTANCE="distance";
        public static String LOCATION="location";
        public static String PHOTO="photo";

    }


    public static class History {


        public  static String TABLE_NAME="history";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/"+TABLE_NAME);
        public static String _ID="_id";
        public static String NAME="name";
        public static String ADDRESS="address";
        public static String DISTANCE="distance";
        public static String LOCATION="location";
        public static String PHOTO="photo";

    }




    public static class Favorites {
        public  static String TABLE_NAME="favorites";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/"+TABLE_NAME);
        public static String _ID="_id";
        public static String NAME="name";
        public static String ADDRESS="address";
        public static String DISTANCE="distance";
        public static String LOCATION="location";
        public static String PHOTO="photo";

    }
}
