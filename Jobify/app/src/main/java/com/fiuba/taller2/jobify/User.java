package com.fiuba.taller2.jobify;


import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;


public class User implements Serializable {

    @Expose                     @SerializedName("email")        String email;
    @Expose                     @SerializedName("first_name")   String firstName;
    @Expose                     @SerializedName("last_name")    String lastName;
    @Expose                     @SerializedName("about")        String about;
    @Expose                     @SerializedName("profile_pic")  String pictureURL;
    @Expose(serialize = false)  @SerializedName("last_location")Location lastLocation;
    @Expose                     @SerializedName("job_position") JobPosition jobPosition;
    @Expose(serialize = false)  @SerializedName("location")     Position position;
    @Expose(serialize = false)  @SerializedName("contacts")     ArrayList<Contact> contacts;
    @Expose                     @SerializedName("skills")       ArrayList<Skill> skills;
    @Expose(serialize = false)  @SerializedName("experiences")  ArrayList<Experience> experiences;
    @Expose                     @SerializedName("device_id")    String deviceId;

    private ArrayList<Chat> chats;


    public User(String email) {
        this.email = email;
    }

    public static User hydrate (JSONObject json) {
        return new Gson().fromJson(json.toString(), User.class);
    }

    public String serialize() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
    }

    public void hydrateChats(JSONArray jsonChats) {
        Type listType = new TypeToken<ArrayList<Chat>>(){}.getType();
        chats = new Gson().fromJson(jsonChats.toString(), listType);
        for (Chat chat : chats) chat.setUser(this);
    }

    public String getFullname() {
        return firstName + " " + lastName;
    }

    public List<Contact> getContacts() {
        return contacts == null ? new ArrayList<Contact>() : contacts;
    }

    public String getID() {
        return email;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public JobPosition getJobPosition() {
        return jobPosition != null ? jobPosition : new JobPosition();
    }

    public void setFirstName(String first_name) {
        this.firstName = first_name;
    }

    public void setLastName(String last_name) {
        this.lastName = last_name;
    }

    public String getFirstName() {
        return firstName;
    }

    public Boolean hasProfilePic() {
        return pictureURL != null && !pictureURL.isEmpty();
    }

    public ArrayList<Skill> getSkills() {
        return skills != null ? skills : new ArrayList<Skill>();
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAbout() {
        return about;
    }

    public String getLastName() {
        return lastName;
    }

    public void updatePosition(double latitude, double longitude) {
        position = new Position(latitude, longitude);
    }

    public Position getPosition() { return position; }

    public ArrayList<Experience> getExperiences() {
        return experiences != null ? experiences : new ArrayList<Experience>();
    }

    public void addSkills(ArrayList<Skill> newSkills) {
        if (skills != null) skills.addAll(newSkills);
        else skills = newSkills;
    }

    public Location getLastLocation() { return lastLocation; }

    public Boolean hasLastLocation() {
        return lastLocation != null;
    }

    public void setDeviceId(String device) {
        deviceId = device;
    }
}
