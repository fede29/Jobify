package com.fiuba.taller2.jobify;

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

    @Expose @SerializedName("user_id")      String userID;
    @Expose @SerializedName("first_name")   String firstName;
    @Expose @SerializedName("last_name")    String lastName;
    @Expose @SerializedName("profile_pic")  String pictureURL;
    @Expose @SerializedName("job_position") JobPosition jobPosition;

    private User user;


    public static Contact hydrate(JSONObject json) {
        return new Gson().fromJson(json.toString(), Contact.class);
    }

    public static List<Contact> hydrate(JSONArray array) {
        Type listType = new TypeToken<ArrayList<Contact>>(){}.getType();
        return new Gson().fromJson(array.toString(), listType);
    }

    public String getFullName() {
        return firstName + ' ' + lastName;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public Boolean hasProfilePic() {
        return pictureURL != null;
    }

    public void setUser(User u) {
        this.user = u;
    }

    public void setUserID(String id) {
        userID = id;
    }

    public String getUserID() {
        return userID;
    }

    public User getUser() {
        return user;
    }

    public String getFullname() {
        return firstName + " " + lastName;
    }

    public JobPosition getJobPosition() {
        return jobPosition != null ? jobPosition : new JobPosition();
    }

    public Boolean hasUserLoaded() {
        return user != null;
    }

}
