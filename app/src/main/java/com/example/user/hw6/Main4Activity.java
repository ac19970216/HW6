package com.example.user.hw6;

import android.database.sqlite.SQLiteDatabase;

import android.location.Address;
import android.location.Geocoder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.util.List;

public class Main4Activity extends AppCompatActivity {
    SQLiteDatabase dbrw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        Bundle bundle = getIntent().getExtras();
        final String name = bundle.getString("name");
        Bundle bundle1 = getIntent().getExtras();
        final String local = bundle1.getString("local");



        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.clear();

                Geocoder geocoder = new Geocoder(getBaseContext());
                List<Address> addressList = null;
                int maxResults = 1;
                try {
                    addressList = geocoder
                            .getFromLocationName(local, maxResults);
                } catch (IOException e) {
                    Log.e("GeocoderActivity", e.toString());
                }

                if(addressList == null || addressList.isEmpty()){

                }
                else{
                    Address address = addressList.get(0);
                    LatLng position = new LatLng(address.getLatitude(),address.getLongitude());
                    String snippet = address.getAddressLine(0);
                    googleMap.addMarker(new MarkerOptions().position(position).title(name).snippet(snippet));

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(15).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        });
    }
}
