package com.example.etty.locationsapp;


import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class HistoryFragment  extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,AdapterView.OnItemClickListener {

    public HistoryAdapter adapter;
    public OnPlaceClicked historyActivityThatImplementsOnPlaceClicked;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        historyActivityThatImplementsOnPlaceClicked= (OnPlaceClicked)activity;

    }

    public HistoryFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO: make favoritesActivity&xml and change inflation
        View myViewToReturn = inflater.inflate(R.layout.fragment_history, container, false);
        ListView historyListView = (ListView) myViewToReturn.findViewById(R.id.historyListView);
        adapter = new HistoryAdapter(getActivity(), null);
        historyListView.setAdapter(adapter);
        historyListView.setOnItemClickListener(this);
        getLoaderManager().initLoader(1, null, this);


        //cursor.close();
        return myViewToReturn;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), PlacesContract.History.CONTENT_URI, null, null, null, null);
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
        Cursor c = getActivity().getContentResolver().query(PlacesContract.History.CONTENT_URI, null, null, null, null, null);
        if (c.moveToPosition(position)) {
            String latlong = c.getString(c.getColumnIndex(PlacesContract.History.LOCATION));
            String name = c.getString(c.getColumnIndex(PlacesContract.History.NAME));
            String address = c.getString(c.getColumnIndex(PlacesContract.History.ADDRESS));
            Log.e("myapp", "clicked on item, latlong: " + latlong);
            historyActivityThatImplementsOnPlaceClicked.OnPlaceSelected(latlong, name,address);
        }

    }




    public interface OnPlaceClicked {
        void OnPlaceSelected(String latlong, String name,String address);

    }

}
