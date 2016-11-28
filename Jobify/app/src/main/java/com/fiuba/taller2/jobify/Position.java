package com.fiuba.taller2.jobify;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Position implements Serializable {

    @SerializedName("latitude")     double latitude;
    @SerializedName("longitude")    double longitude;


    public Position(double lat, double lng) {
        latitude = lat;
        longitude = lng;
    }

    public String serialize() {
        JSONObject pos = new JSONObject();
        try {
            pos.put("location", new JSONObject(new Gson().toJson(this)));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Position serialization", e.getMessage());
        }
        return pos.toString();
    }

    public Double getLat() {
        return latitude;
    }

    public Double getLng() {
        return longitude;
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

}
