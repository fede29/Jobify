package com.fiuba.taller2.jobify;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.fiuba.taller2.jobify.utils.AppServerRequest;
import com.fiuba.taller2.jobify.utils.NonResponsiveCallback;

public class PositionManager {

    LocationManager locManager;
    User user;
    PositionListener listener;

    public final static int REFRESH_TIME = 60000;


    public PositionManager(LocationManager lm, User user) {
        this.user = user;
        locManager = lm;
        listener = new PositionListener();
    }

    public void initiate() {
        try {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, REFRESH_TIME, 0, listener);
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, REFRESH_TIME, 0, listener);
        } catch (SecurityException e) {
            Log.w("Position permission", e.getMessage());
        }
    }


    /***************************************** PRIVATE STUFF **************************************/

    private class PositionListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            user.updatePosition(location.getLatitude(), location.getLongitude());
            AppServerRequest.updateLocation(user, new NonResponsiveCallback());
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {}

        @Override
        public void onProviderEnabled(String s) {}

        @Override
        public void onProviderDisabled(String s) {}
    }
}
