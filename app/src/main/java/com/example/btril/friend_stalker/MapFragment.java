package com.example.btril.friend_stalker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by deeksha on 8/2/2017.
 */

public class MapFragment extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap maps;
    Intent intent;
    String email, lat, longi;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //action bar item clicks are handled here.
        int id = item.getItemId();
        if (id == R.id.home) {
            startActivity(new Intent(this, SignInSuccess.class));
            return true;
        }
        if (id == R.id.signout) {
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_fragment);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        intent = getIntent();
        email = intent.getExtras().getString("email");
        lat = intent.getExtras().getString("lat");
        longi = intent.getExtras().getString("lon");

        LocationManager lctnMgr = (LocationManager) getSystemService
                (Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        String provider = lctnMgr.getBestProvider(criteria, false);
        Location location = lctnMgr.getLastKnownLocation(provider);

        MyLctnListener mylistener = new MyLctnListener();
        if (location != null) {
            mylistener.onLocationChanged(location);
        } else {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        // location updates
        lctnMgr.requestLocationUpdates(provider, 10, 0, mylistener);
    }
    @Override
    protected void onStart() {
        super.onStart();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mymap);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        maps = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        maps.setMyLocationEnabled(true);

        LatLng frnlctn = new LatLng(Double.parseDouble(lat),Double.parseDouble(longi));
        maps.addMarker(new MarkerOptions().position(frnlctn).title(email));
        maps.moveCamera(CameraUpdateFactory.newLatLng(frnlctn));
        maps.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
    }

    @Override
    protected void onResume(){
        super.onResume();
    }
    private class MyLctnListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            Toast.makeText(MapFragment.this, "Location has been changed!" + String.valueOf(location.getLatitude()) + String.valueOf(location.getLongitude()),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(MapFragment.this, provider + "'s status changed to "+status +"!",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(MapFragment.this, "Provider " + provider + " enabled!",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(MapFragment.this, "Provider " + provider + " disabled!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
