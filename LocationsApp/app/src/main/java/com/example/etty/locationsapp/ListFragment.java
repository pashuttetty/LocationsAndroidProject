package com.example.etty.locationsapp;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


public class ListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public PlacesAdapter adapter;
    public OnPlaceClicked activityThatImplementsOnPlaceClicked;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityThatImplementsOnPlaceClicked= (OnPlaceClicked)activity;

    }

    public ListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myViewToReturn = inflater.inflate(R.layout.fragment_list, container, false);
        ListView serachResultsListView = (ListView) myViewToReturn.findViewById(R.id.serachResultsListView);
        adapter = new PlacesAdapter(getActivity(), null);
        serachResultsListView.setAdapter(adapter);
        serachResultsListView.setOnItemClickListener(this);
        serachResultsListView.setOnItemLongClickListener(this);
        getLoaderManager().initLoader(1, null, this);


        //cursor.close();
        return myViewToReturn;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), PlacesContract.Places.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //open item's location on map fragment.
        Cursor c = getActivity().getContentResolver().query(PlacesContract.Places.CONTENT_URI, null, null, null, null, null);
        if (c.moveToPosition(position)) {
            String latlong = c.getString(c.getColumnIndex(PlacesContract.Places.LOCATION));
            String name = c.getString(c.getColumnIndex(PlacesContract.Places.NAME));
            String address = c.getString(c.getColumnIndex(PlacesContract.Places.ADDRESS));
            Log.e("myapp", "clicked on item, latlong: " + latlong);
            activityThatImplementsOnPlaceClicked.OnPlaceSelected(latlong,name,address);
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

        //TODO: share intent
        //TODO: add location to favourites
        Log.e("myapp", "long clicked pos: " + position);
        Cursor cursor = getActivity().getContentResolver().query(PlacesContract.Places.CONTENT_URI, null, null, null, null, null);
        if (cursor.moveToPosition(position)) {
            String latlong = cursor.getString(cursor.getColumnIndex(PlacesContract.Places.LOCATION));
            String name = cursor.getString(cursor.getColumnIndex(PlacesContract.Places.NAME));
            String address = cursor.getString(cursor.getColumnIndex(PlacesContract.Places.ADDRESS));
            String img = cursor.getString(cursor.getColumnIndex(PlacesContract.Places.PHOTO));
            Log.e("myapp", "clicked on item, latlong: " + latlong);
            activityThatImplementsOnPlaceClicked.OnPlaceLongClicked(position,latlong, name, address, img);


        }



        return true;
    }




    public interface OnPlaceClicked {
        void OnPlaceSelected(String latlong, String name,String address);
        void OnPlaceLongClicked(int position, String latlong, String name, String address, String img);

    }
}
