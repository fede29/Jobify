package com.fiuba.taller2.jobify;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Skill implements Serializable {

    @SerializedName("id")           int id;
    @SerializedName("name")         String name;
    @SerializedName("description")  String description;
    @SerializedName("category")     String category;


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
