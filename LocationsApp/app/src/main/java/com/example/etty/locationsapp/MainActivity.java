package com.example.etty.locationsapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;


import android.location.LocationProvider;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.zip.Inflater;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationListener,  ListFragment.OnPlaceClicked{
    private Button goButton;
    private EditText searchValueET;
    private String searchValue;
    private CheckBox useMyLocationCheckBox;
    private String currLatitude;
    private String currLongtitude;
    protected LocationManager locationManager;
    private String provider;
    private Boolean searchingByLocation=false;
    MyPowerReceiver myPowerReceiver;

/*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }*/

    //TODO: Transfer to BaseActivity? what about container id?:
    @Override
    public void OnPlaceSelected(String latlong,String name, String address) {
        Log.e("testing",latlong);
        MapFragment myMapFragment= new MapFragment();
        Bundle args = new Bundle();
        args.putString("location" , latlong);
        args.putString("name", name);
        args.putString("address", address);
        myMapFragment.setArguments(args);
        if(isInSingleFragment()) {
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.fragment_container, myMapFragment, "map").commit();
        }
        else{
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.mymap, myMapFragment, "map").commit();
        }
        /*
        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getFragmentManager().getBackStackEntryCount() == 0) finish();
            }
        });
        */
    }

    protected boolean isInSingleFragment() {
        //try to find the layout with id : layout_singleLayout
        View layout = findViewById(R.id.layout_singleFragment);

        // this will be null on a dual fragment layout

        if (layout != null) {
            // found - we are
            return true;
        } else {
            // not found - we are not.
            return false;
        }
    }

    @Override
    public void OnPlaceLongClicked(final int position, final String latlong, final String name, final String address, final String img) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("choose action: ")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Share this location", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        shareLocation(latlong, name, address);

                    }

                });

        alertDialogBuilder.setNegativeButton("Save to favorites", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("listfragment", "save to favorites selected");
                addLocationToFavorites(latlong, name, address, img);

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void shareLocation(String latlong,String name,String address){

        name = name.replace(" ", "+");
        address = address.replace(" ", "+");
        String uri = "https://www.google.com/maps/place/" + name + "," + address + "%E2%80%AD/@" + latlong;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        //TODO: check how to display only name with no link
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Location" + name + address);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, uri);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void addLocationToFavorites(String latlong,String name,String address, String img){

        PlaceModel favoritePlace= new PlaceModel(name,address,latlong,0, img);
        DBHandler.insertFavoritePlace(favoritePlace, getApplicationContext());


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instantiate();
    }

    private void instantiate() {

        getLastKnownLocation();
        //TODO ****************************************************

        goButton = (Button)findViewById(R.id.goButton);
        searchValueET=(EditText)findViewById(R.id.searchValueEditText);
        goButton.setOnClickListener(this);
        useMyLocationCheckBox=(CheckBox)findViewById(R.id.useMyLocationCheckBox);
        //Creating receiver for charging:
        myPowerReceiver = new MyPowerReceiver();
        IntentFilter filter = new IntentFilter("android.intent.action.ACTION_POWER_CONNECTED");
        registerReceiver(myPowerReceiver, filter);

    }

    private void getLastKnownLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        provider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
        if (lastKnownLocation != null) {
            // found a last location:
            currLatitude= String.valueOf(lastKnownLocation.getLatitude());
            currLongtitude=String.valueOf(lastKnownLocation.getLongitude());
            Log.e("myapp", currLatitude + " " + currLongtitude);
        }
        else{
            Toast.makeText(this,"Can't get find your location. Try searching by text",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        searchValue=searchValueET.getText().toString();
        if(useMyLocationCheckBox.isChecked()){
            searchValueET.setText("");
            searchingByLocation=true;
        }
        if(v.getId()==R.id.goButton){


            //Toast.makeText(this, "goButton Pressed",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,SearchService.class);
            intent.putExtra("searchValue", searchValue);
            intent.putExtra("currLatitude", currLatitude);
            intent.putExtra("currLongtitude", currLongtitude);
            intent.putExtra("searchValue", searchValue);
            intent.putExtra("searchByLocation", searchingByLocation);
            //Toast.makeText(this, "starting SearchService",Toast.LENGTH_SHORT).show();
            startService(intent);
        }

    }
    @Override
    public void onLocationChanged(Location location) {
        currLatitude= String.valueOf(location.getLatitude());
        currLongtitude=String.valueOf(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e("myapp","**status changed\n");
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.e("myapp","** AVAILABLE\n");
                break;

            case LocationProvider.OUT_OF_SERVICE:
                Log.e("myapp", "** OUT_OF_SERVICE\n");
                break;

            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.e("myapp", "** TEMPORARILY_UNAVAILABLE\n");
                break;
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.e("myapp","**provider enabled\n");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.e("myapp", "**provider disabled\n");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 5000, 1, this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // when activity is invisible -
        // stop location updates (to save battery)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);

        Log.e("myapp", "*** stoping updates listener ***\n");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(myPowerReceiver);
    }
//TODO: Transfer to BaseActivity:
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //TODO: Transfer to BaseActivity:
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //if chosen, close the application
            case R.id.favoritesOptionMenu:
                if (item.isChecked()) item.setChecked(false);

                else { //item.setChecked(true);
                    //alert the user to confirm exit
                    Intent goToFavoritesIntent = new Intent(this, FavoritesActivity.class);
                    //Toast.makeText(this, "go to favorites pressed", Toast.LENGTH_LONG).show();
                    startActivity(goToFavoritesIntent);

                    return true;
                }
                //if chosen, delete all the movies from the data base (thus, delete from the list)
            case R.id.historyOptionMenu:
                if (item.isChecked()) item.setChecked(false);
                else {//item.setChecked(true);
                    //go to last search (history table db)
                    Intent goToHistoryIntent = new Intent(this, HistoryActivity.class);
                    //Toast.makeText(this, "go to history pressed", Toast.LENGTH_LONG).show();
                    startActivity(goToHistoryIntent);

                    return true;
                }
            case R.id.settingsOptionMenu:
                if (item.isChecked()) item.setChecked(false);
                else{ //item.setChecked(true);
                    //go to settings
                    Intent goToSettingsIntent = new Intent(this,SettingsActivity.class);
                    //Toast.makeText(this, "settings pressed", Toast.LENGTH_LONG).show();
                    Log.e("myapp","go to settings...");
                    startActivity(goToSettingsIntent);
                }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}