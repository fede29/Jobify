package com.fiuba.taller2.jobify;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Location implements Serializable {

    @SerializedName("latitude")     double latitude;
    @SerializedName("longitude")    double longitude;


    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

    public Double getLat() {
        return latitude;
    }

    public Double getLng() {
        return longitude;
    }
}
