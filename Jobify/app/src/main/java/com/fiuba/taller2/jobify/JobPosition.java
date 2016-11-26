package com.fiuba.taller2.jobify;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class JobPosition implements Serializable {

    @Expose @SerializedName("id")           private int id;
    @Expose @SerializedName("name")         String name;
    @Expose @SerializedName("description")  String description;
    @Expose @SerializedName("category")     String category;


    public JobPosition() {}

    public JobPosition(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() { return name; }

}
