package com.example.googlemaps_markers;

import androidx.fragment.app.FragmentActivity;

import android.icu.text.Edits;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.support.v4.app.INotificationSideChannel;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    HashMap<Integer, Integer> coordinatesMapToMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        HashMap<Integer, Integer> coordinatesMap = new HashMap<>();
        coordinatesMap.put(-34, 151);
        coordinatesMap.put(-50, 152);

        coordinatesMapToMap = coordinatesMap;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//
        Iterator iterator = coordinatesMapToMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)iterator.next();
            LatLng mapCoordinates = new LatLng(Double.valueOf(mapElement.getKey().toString()), Double.valueOf(mapElement.getValue().toString()));

            mMap.addMarker(new MarkerOptions()
                    .position(mapCoordinates).title(mapCoordinates.toString()));

        }

    }
}
