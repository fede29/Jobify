package com.fiuba.taller2.jobify;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Skill implements Serializable {

    @Expose @SerializedName("name")         String name;
    @Expose @SerializedName("description")  String description;
    @Expose @SerializedName("category")     String category;


    public static List<Skill> hydrate(JSONArray jsonSkills) {
        Type listType = new TypeToken<ArrayList<Skill>>(){}.getType();
        return new Gson().fromJson(jsonSkills.toString(), listType);
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String toString() { return name; }

    @Override
    public boolean equals(Object skill) {
        Skill other = (Skill) skill;
        return this.name.equals(other.name) &&
                this.description.equals(other.description) &&
                this.category.equals(other.category);
    }

}
