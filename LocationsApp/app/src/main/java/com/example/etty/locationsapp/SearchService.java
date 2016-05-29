package com.example.etty.locationsapp;


import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class SearchService extends IntentService{

    private String searchValue;
    private static final  String BASE_URL_SEARCH_BY_TEXT="https://maps.googleapis.com/maps/api/place/textsearch/json?query=";
    static final String KEY ="&key=AIzaSyC4opFC8WiDwtHeG9o-Sd39kWSBdeJSYpI";
    static final String IMAGE_BASE_URL="https://maps.googleapis.com/maps/api/place/photo?maxwidth=100&photoreference=";
   // private static final String IMAGE_URL_FOR_NO_IMAGE_IN_JSON="";

    private static String COMPLETE_URL_SEARCH_BY_TEXT = null;
    private String name;
    private String address;
    private float distance;
    private String lat;
    private String lng;
    private String urlToSearch;

    private static final String BASE_URL_SEARCH_BY_LOCATION="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
    private static String currlat = null;
    private static String currlng = null;
    private static final int RADIUS = 1000;//TODO: give the user option to choose the radius?
    private static String complete_url_search_by_location;
    private Location currLocation;
    private Location destinationLocation;
    private static float distanceInMeters;
    private static float distanceInKM;
    private static float distanceInMiles;
    private Boolean searchingByLocation;


    //TODO: make the JSON strings final?? public final String RESULTS = "results";


    public SearchService(String name) {
        super("");
    }

    public SearchService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //from SeachServiceByLocation: *********
        currlat=intent.getStringExtra("currLatitude");
        currlng= intent.getStringExtra("currLongtitude");
        searchingByLocation= intent.getBooleanExtra("searchByLocation", false);
        currLocation=new Location("");
        if(currlat!=null || currlng!=null) {
            currLocation.setLatitude(Double.parseDouble(currlat));
            currLocation.setLongitude(Double.parseDouble(currlng));
            Log.e("myCurrentLocation", "lat= " + currlat + " lng= " + currlng);
            complete_url_search_by_location=BASE_URL_SEARCH_BY_LOCATION+currlat+","+currlng+"&radius="+RADIUS+KEY;
        }
            //**************
        searchValue = intent.getStringExtra("searchValue");
        searchValue = searchValue.replace(" ", "+");
        if(searchingByLocation == false) {
            COMPLETE_URL_SEARCH_BY_TEXT = BASE_URL_SEARCH_BY_TEXT
                    + searchValue
                    + KEY;
            urlToSearch = COMPLETE_URL_SEARCH_BY_TEXT;
        }

        else{
            complete_url_search_by_location = BASE_URL_SEARCH_BY_LOCATION+currlat+","+currlng+"&radius="+RADIUS+KEY;
            urlToSearch = complete_url_search_by_location;
        }
        String response=null;

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpGet get = new HttpGet(urlToSearch);
            HttpResponse httpResponse = httpClient.execute(get);
            HttpEntity httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {

        } catch (ClientProtocolException e) {

        } catch (IOException e) {

        }
        Log.d("myapp", response);

        getContentResolver().delete(PlacesContract.Places.CONTENT_URI, null, null);
        getContentResolver().delete(PlacesContract.History.CONTENT_URI, null, null);

        try {
            JSONObject fullResultsObject = new JSONObject(response);
            JSONArray fullResultsArray = fullResultsObject.getJSONArray("results");

            for (int i = 0; i < fullResultsArray.length(); i++) {
                JSONObject currObject = fullResultsArray.getJSONObject(i);

                name = currObject.getString("name");
                if(urlToSearch==COMPLETE_URL_SEARCH_BY_TEXT){
                    address = currObject.getString("formatted_address");
                }
                else {
                    address = currObject.getString("vicinity");
                }
                JSONObject geometryObject = currObject.getJSONObject("geometry");
                JSONObject locationObject=geometryObject.getJSONObject("location");
                lat = locationObject.getString("lat");
                lng = locationObject.getString("lng");
               // distance = ;

                String imageURL = "";
                try {
                    JSONArray imageArray = currObject.getJSONArray("photos");
                    JSONObject imageObject = imageArray.getJSONObject(0);
                    String specificImageReference = imageObject.getString("photo_reference");
                        imageURL = IMAGE_BASE_URL
                                + specificImageReference + KEY;

                } catch (JSONException ex) {
                    Log.e("myapp", ex.getMessage());
                }
                //do the download of each photo...
                Bitmap tempBitmap=getBitmapFromURL(imageURL);
                String MyImageInBase64="";
                if(!imageURL.equals("")) {
                    MyImageInBase64 = bitmapToBase64(tempBitmap);
                    //save the photo (NOT URL) in the db
                }
                
                Log.d("debug", "json info: name:" + name + " lat:" + lat + " lng:" + lng + " photo:  " + MyImageInBase64);
//TODO:******************GET CURRLOCATION AND CALCULATE DISTANCE;
                calculateDistance(lat, lng);
                PlaceModel place= new PlaceModel(name,address,lat+","+lng,distanceInMeters, MyImageInBase64);
                DBHandler.insertPlace(place, getApplicationContext());



                DBHandler.insertPlaceToHistory(place, getApplicationContext());

            }


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this,"Can't connect to the internet",Toast.LENGTH_LONG).show();
        }

    }

    private void calculateDistance(String lat,String lng) {
        destinationLocation=new Location("");
        destinationLocation.setLatitude(Double.parseDouble(lat));
        destinationLocation.setLongitude(Double.parseDouble(lng));
        distanceInMeters = currLocation.distanceTo(destinationLocation);
        distanceInKM = distanceInMeters/1000;
        distanceInMiles = (float) (distanceInKM*1.6093);
        Log.e("distance in meters", String.valueOf(distanceInKM));

    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    private Bitmap getBitmapFromURL(String src) {
        try {
            System.out.printf("src", src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            System.out.printf("Bitmap", "returned");
            myBitmap = Bitmap.createScaledBitmap(myBitmap, 100, 100, false);//This is only if u want to set the image size.
            return myBitmap;
            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.printf("Exception", e.getMessage());
            return null;
        }
    }
}