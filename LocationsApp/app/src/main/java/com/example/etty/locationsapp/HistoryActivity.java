package com.example.etty.locationsapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class HistoryActivity extends AppCompatActivity implements HistoryFragment.OnPlaceClicked{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
    }

    @Override
    public void OnPlaceSelected(String latlong, String name, String address) {
        Log.e("testing", latlong);
        MapFragment myMapFragment= new MapFragment();
        Bundle args = new Bundle();
        args.putString("location" , latlong);
        args.putString("name", name);
        args.putString("address", address);
        myMapFragment.setArguments(args);

        FragmentManager fm= getFragmentManager();
        fm.beginTransaction().replace(R.id.history_fragment_container, myMapFragment, "map" ).commit();
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
                    Toast.makeText(this, "go to favorites pressed", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(this, "settings pressed", Toast.LENGTH_LONG).show();
                    Log.e("myapp","go to settings...");
                    startActivity(goToSettingsIntent);
                }
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}