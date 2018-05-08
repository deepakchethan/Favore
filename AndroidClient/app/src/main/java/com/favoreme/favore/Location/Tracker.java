package com.favoreme.favore.Location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * This method controls the gps tracker and stuff
 */
public class Tracker implements LocationListener {

    String TAG ="DOPE";
    private Context context;
    LocationManager lm;
    public Tracker(Context context) {
        this.context = context;
    }

    public Location getLocation() {
        Location l=null;
        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.d("DOPE","isGPSEnabled"+isGPSEnabled);
        if (isGPSEnabled) {
            boolean dope = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;

            if (dope) {
                Toast.makeText(context,"Location permission not granted",Toast.LENGTH_SHORT).show();
                return null;
            }
            Log.d(TAG, "getLocation: GPS Enabled");

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(l == null) Log.d(TAG, "getLocation: l is null");
            Log.d("DOPE","perm "+l);
            return l;
        }else{
            Toast.makeText(context,"Please Enable GPS",Toast.LENGTH_SHORT).show();

        }
        Toast.makeText(context,"This is being printed",Toast.LENGTH_LONG).show();
        return l;
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.d("DOPE", "onLocationChanged: "+location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
