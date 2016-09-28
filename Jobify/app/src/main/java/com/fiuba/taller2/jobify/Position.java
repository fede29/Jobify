package com.fiuba.taller2.jobify;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Position implements Serializable {

    @SerializedName("latitude")     double latitude;
    @SerializedName("longitude")    double longitude;


    public Position(double lat, double lng) {
        latitude = lat;
        longitude = lng;
    }

    public String serialize() {
        return new Gson().toJson(this);
    }

}
