package com.fei.mobileguard.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;

import java.sql.SQLOutput;

public class MyLocationService extends Service {

    private LocationManager mLm;
    private SharedPreferences mSharedPreferences;

    public MyLocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLm = (LocationManager) getSystemService(LOCATION_SERVICE);
        mSharedPreferences = getSharedPreferences("config",MODE_PRIVATE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(true);
        String bestProvider = mLm.getBestProvider(criteria, true);
        mLm.requestLocationUpdates(bestProvider,0,0,new MyLocationListener());
        stopSelf();
    }

    class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            String latitude = "Latitude: " + location.getLatitude();
            String longitude = "Longitude: " + location.getLongitude();
            String accuracy = "Accuracy: " + location.getAccuracy();
            String altitude = "Altitude: "+location.getAltitude();

            mSharedPreferences.edit().putString("location","latitude: "+latitude
                    +"longitude: "+longitude).commit();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            System.out.println("onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) {
            System.out.println("onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            System.out.println("onProviderDisabled");
        }
    }

}
