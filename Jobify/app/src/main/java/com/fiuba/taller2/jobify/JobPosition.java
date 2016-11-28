package com.fiuba.taller2.jobify;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

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

}
