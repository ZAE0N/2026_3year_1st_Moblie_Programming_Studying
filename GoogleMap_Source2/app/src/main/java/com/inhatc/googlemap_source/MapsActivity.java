package com.inhatc.googlemap_source;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.inhatc.googlemap_source.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LatLng objLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        long minTime = 1000;
        float minDistance = 1;

        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        //Event Handler
        LocationListener locationListener = new LocationListener() {
            //Automatically calling when current position changed
            public void onLocationChanged(Location location) {
                mUpdateMap(location);
            }
            //Automatically calling when provider status changed
            public void onStatusChanged(String provider, int status, Bundle extras) {
                mAlertStatus(provider);
            }
            //Automatically calling when provider enable status
            public void onProviderEnabled(String provider) {
                mAlertProvider(provider);
            }
            //Automatically calling when provider disable status
            public void onProviderDisabled(String provider) {
                mCheckProvider(provider);
            }
        };

        LocationManager locationManager;
        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        //Check Uses-Permission
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
            return;
        }

        String strLocationProvider = LocationManager.GPS_PROVIDER;
        locationManager.requestLocationUpdates(strLocationProvider, minTime, minDistance, locationListener);
        strLocationProvider = LocationManager.NETWORK_PROVIDER;
        locationManager.requestLocationUpdates(strLocationProvider, minTime, minDistance, locationListener);
    }

    public void mUpdateMap(Location location) {
        double dLatitude = location.getLatitude();
        double dLongitude = location.getLongitude();
        objLocation = new LatLng(dLatitude, dLongitude);

        Marker objMK = mMap.addMarker(new MarkerOptions().position(objLocation)
                .title("Current Position"));
        objMK.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(objLocation, 15f));
    }

    public void mCheckProvider(String strProvider) {
        Toast.makeText(this, strProvider + ": Location service turn off ... " +
                "Please Turn on location service...", Toast.LENGTH_SHORT).show();
        //Create Intent to setting provider
        Intent objIntent;
        objIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(objIntent);
    }

    public void mAlertProvider(String strProvider) {
        Toast.makeText(this, strProvider + "Starting location service !",
                Toast.LENGTH_LONG).show();
    }

    public void mAlertStatus(String strProvider) {
        Toast.makeText(this, "Changing location service : " + strProvider,
                Toast.LENGTH_LONG).show();
    }
}