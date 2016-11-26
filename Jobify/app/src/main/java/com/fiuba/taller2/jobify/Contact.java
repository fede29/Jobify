package com.fiuba.taller2.jobify;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Contact implements Serializable {

    @Expose @SerializedName("id")           String email;
    @Expose @SerializedName("first_name")
    @PropertyName("first_name")             public String firstName;
    @Expose @SerializedName("last_name")
    @PropertyName("last_name")              public String lastName;
    @Expose @SerializedName("profile_pic")
    @PropertyName("profile_pic")            public String pictureURL;
    @Expose @SerializedName("job_position") JobPosition jobPosition;

    private User user;


    public static Contact hydrate(JSONObject json) {
        return new Gson().fromJson(json.toString(), Contact.class);
    }

    public static List<Contact> hydrate(JSONArray array) {
        Type listType = new TypeToken<ArrayList<Contact>>(){}.getType();
        return new Gson().fromJson(array.toString(), listType);
    }

    public String getFullname() {
        return String.format("%s %s", firstName, lastName);
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public String getId() {
        return email;
    }

    public User getUser() {
        return user;
    }

    public JobPosition getJobPosition() {
        return jobPosition != null ? jobPosition : new JobPosition();
    }

    public void setUser(User u) {
        this.user = u;
    }

    public void setId(String id) {
        this.email = id;
    }

    public Boolean hasUserLoaded() {
        return user != null;
    }

    public Boolean hasProfilePic() {
        return pictureURL != null;
    }

}
