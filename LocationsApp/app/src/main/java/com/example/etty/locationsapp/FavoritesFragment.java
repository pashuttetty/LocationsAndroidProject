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


public class FavoritesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,AdapterView.OnItemClickListener {

    public FavoritesAdapter adapter;
    public OnPlaceClicked favoriteActivityThatImplementsOnPlaceClicked;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        favoriteActivityThatImplementsOnPlaceClicked= (OnPlaceClicked)activity;

    }

    public FavoritesFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myViewToReturn = inflater.inflate(R.layout.fragment_favorites, container, false);
        ListView favoritesListView = (ListView) myViewToReturn.findViewById(R.id.favoritesListView);
        adapter = new FavoritesAdapter(getActivity(), null);
        favoritesListView.setAdapter(adapter);
        favoritesListView.setOnItemClickListener(this);
        getLoaderManager().initLoader(1, null, this);


        //cursor.close();
        return myViewToReturn;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), PlacesContract.Favorites.CONTENT_URI, null, null, null, null);
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
        Cursor c = getActivity().getContentResolver().query(PlacesContract.Favorites.CONTENT_URI, null, null, null, null, null);
        if (c.moveToPosition(position)) {
            String latlong = c.getString(c.getColumnIndex(PlacesContract.Favorites.LOCATION));
            String name = c.getString(c.getColumnIndex(PlacesContract.Favorites.NAME));
            String address = c.getString(c.getColumnIndex(PlacesContract.Favorites.ADDRESS));
            Log.e("myapp", "clicked on item, latlong: " + latlong);
            favoriteActivityThatImplementsOnPlaceClicked.OnPlaceSelected(latlong, name,address);
        }

    }




    public interface OnPlaceClicked {
        void OnPlaceSelected(String latlong, String name,String address);

    }

}
