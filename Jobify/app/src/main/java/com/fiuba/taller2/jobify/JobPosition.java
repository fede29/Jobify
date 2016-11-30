package com.fiuba.taller2.jobify;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class JobPosition implements Serializable {

    @Expose @SerializedName("name")         String name;
    @Expose @SerializedName("description")  String description;
    @Expose @SerializedName("category")     String category;


    public JobPosition() {
        name = description = category = "";
    }

    public JobPosition(String name) {
        this.name = name;
        description = category = "";
    }

    public static List<JobPosition> hydrate(JSONArray jps) {
        LinkedList<JobPosition> list = new LinkedList<>();
        for (int i = 0; i < jps.length(); ++i) {
            try {
                JSONObject json = jps.getJSONObject(i);
                list.add(new Gson().fromJson(json.toString(), JobPosition.class));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Job Positions", e.getMessage());
            }
        }
        return list;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String toString() { return name; }

    @Override
    public boolean equals(Object jp) {
        JobPosition other = (JobPosition) jp;
        return this.name.equals(other.name) &&
                this.description.equals(other.description) &&
                this.category.equals(other.category);
    }

}
