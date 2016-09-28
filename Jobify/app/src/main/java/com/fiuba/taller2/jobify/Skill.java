package com.fiuba.taller2.jobify;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Skill implements Serializable {

    @Expose @SerializedName("id")           int id;
    @Expose @SerializedName("name")         String name;
    @Expose @SerializedName("description")  String description;
    @Expose @SerializedName("category")     String category;


    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return id;
    }

    public String toString() { return name; }

}
