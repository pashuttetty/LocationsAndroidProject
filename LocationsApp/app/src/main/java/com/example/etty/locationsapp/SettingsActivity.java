package com.example.etty.locationsapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SettingsActivity  extends PreferenceActivity implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private static final String TAG = "SettingsActivity";
    private SharedPreferences prefs;
    boolean deleteAllFav=true;
    CheckBoxPreference prefDeleteAllFav;
    ListPreference prefUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String chooseDistanceUnit = prefs.getString("chooseDistanceUnit", "kilometers");
        deleteAllFav = prefs.getBoolean("deleteAllFav", true);


        prefDeleteAllFav =(CheckBoxPreference) findPreference("deleteAllFav");

        prefUnit =(ListPreference) findPreference("chooseDistanceUnit");

        //set list values:
        prefUnit.setEntries(new String[]{"Kilometers", "Miles"});
        prefUnit.setEntryValues(new String[]{"Kilometers", "Miles"});


        //set the summaries:
        prefUnit.setSummary(chooseDistanceUnit);
        prefDeleteAllFav.setEnabled(deleteAllFav);

        prefDeleteAllFav.setOnPreferenceChangeListener(this);
        prefUnit.setOnPreferenceChangeListener(this);

    }

    @Override
    public boolean onPreferenceChange(Preference pref, Object newValue) {
        String key = pref.getKey();
        Log.d(TAG, "changed " + key + " -> " + newValue);

        if(key.equals("chooseDistanceUnit")){
            prefUnit.setSummary((String) newValue);
        }
        else if(key.equals("deleteAllFav")){
            prefDeleteAllFav.setEnabled((Boolean) newValue);
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference pref) {

        String key = pref.getKey();
        Log.d(TAG, "clicked " + key);

        if(key.equals("deleteAllFav")){
            if(deleteAllFav){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Are you sure you want to delete your favorites list?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                DBHandler.deleteAllFavoritesTable(getBaseContext());

                            }

                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("myapp", "Don't delete my favorites list");

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }

        }

        return true;
    }
}
