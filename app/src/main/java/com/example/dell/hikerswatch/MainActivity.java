package com.example.dell.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView latitude,longitude,accuracy,altitude,address;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);//starting our location manager
            }
        }
    }

    public void updateLocationInfo(Location location){

        latitude = (TextView)findViewById(R.id.latTextView);
        longitude = (TextView)findViewById(R.id.longTextView);
        accuracy = (TextView)findViewById(R.id.accTextView);
        altitude = (TextView)findViewById(R.id.altTextView);
        address = (TextView)findViewById(R.id.addTextView);

        latitude.setText("Latitude: "+ location.getLatitude());
        longitude.setText("Longitude: "+location.getLongitude());
        accuracy.setText("Accuracy: "+ location.getAccuracy());
        altitude.setText("Altitude: "+ location.getAltitude());

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            String addressOfLocation = "Could Not Find Address." ;
            List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (listAddresses != null && listAddresses.size() > 0) {

                addressOfLocation = " ";

                if(listAddresses.get(0).getSubThoroughfare() != null){
                    addressOfLocation += listAddresses.get(0).getSubThoroughfare() + " ";
                }

                if(listAddresses.get(0).getThoroughfare() != null){
                    addressOfLocation += listAddresses.get(0).getThoroughfare() + "\n";
                }

                if(listAddresses.get(0).getLocality() != null){
                    addressOfLocation += listAddresses.get(0).getLocality() + "\n";
                }

                if(listAddresses.get(0).getPostalCode() != null){
                    addressOfLocation += listAddresses.get(0).getPostalCode() + "\n";
                }

                if(listAddresses.get(0).getCountryName() != null){
                    addressOfLocation += listAddresses.get(0).getCountryName() + ".";
                }

            }

            address.setText("Address:\n " + addressOfLocation);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateLocationInfo(location);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        else
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }else
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);//getting the last known location from the GPS Provider

                if(location != null) //checking that if the last known location is not null then only call for the update location method
                {
                    updateLocationInfo(location);
                }
                }
        }
    }
}
