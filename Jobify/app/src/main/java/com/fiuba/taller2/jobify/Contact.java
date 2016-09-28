package com.fiuba.taller2.jobify;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;


public class Contact implements Serializable {

    @Expose @SerializedName("user_id")      int userID;
    @Expose @SerializedName("first_name")   String firstName;
    @Expose @SerializedName("last_name")    String lastName;
    @Expose @SerializedName("profile_pic")  String pictureURL;

    private User user;


    public static Contact hydrate(JSONObject json) {
        return new Gson().fromJson(json.toString(), Contact.class);
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

    public Integer getUserID() {
        return userID;
    }

    public User getUser() {
        return user;
    }

    public String getFullname() {
        return firstName + " " + lastName;
    }

    public Boolean equals(Contact that) {
        return this.userID == that.userID;
    }

}
