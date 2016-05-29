package com.example.etty.locationsapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment {


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = /*(LinearLayout)*/inflater.inflate(R.layout.fragment_map, container, false);

        Bundle b=getArguments();
        String latlong = b.getString("location");
        String name = b.getString("name");
        String address = b.getString("address");
        Log.d("fragment...", latlong);

        String[] latlongarr= latlong.split(",");

        double lat= Double.parseDouble(latlongarr[0]);
        double lon= Double.parseDouble(latlongarr[1]);

        TextView mapNameHeadlineTV = (TextView) view.findViewById(R.id.mapNameHeadlineTV);
        TextView mapAddressHeadlineTV = (TextView) view.findViewById(R.id.mapAddressHeadlineTV);


        FragmentManager fm = getFragmentManager();
        //TODO********** id= fragment
        com.google.android.gms.maps.MapFragment mapFragment = (com.google.android.gms.maps.MapFragment) getChildFragmentManager().findFragmentById(R.id.mymap);
        // get the map object from the fragment:

        GoogleMap map = mapFragment.getMap();

        if(map!= null) {
            // setup the map type:
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            // setup map position and zoom
            LatLng position = new LatLng(lat, lon);
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(position, 15);

            map.addMarker(new MarkerOptions().position(position).title(name));
            map.moveCamera(update);
            mapNameHeadlineTV.setText(name);
            mapAddressHeadlineTV.setText(address);

        }


        return view;
    }




}
