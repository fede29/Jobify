package com.fiuba.taller2.jobify;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


public class User implements Serializable {

    @Expose                     @SerializedName("email")        String email;
    @Expose                     @SerializedName("first_name")   String firstName;
    @Expose                     @SerializedName("last_name")    String lastName;
    @Expose                     @SerializedName("about")        String about;
    @Expose                     @SerializedName("pic")          String picture;
    @Expose                     @SerializedName("fb_pic")       Boolean hasFBPic;
    @Expose(serialize = false)  @SerializedName("location")     Position position;
    @Expose                     @SerializedName("job_position") JobPosition jobPosition;
    @Expose(serialize = false)  @SerializedName("contacts")     LinkedList<Contact> contacts;
    @Expose                     @SerializedName("skills")       LinkedList<Skill> skills;
    @Expose                     @SerializedName("experiences")  LinkedList<Experience> experiences;
    @Expose                     @SerializedName("device_id")    String deviceId;

    private ArrayList<Chat> chats;


    public User() {}

    public User(String email) {
        this.email = email;
    }

    public static User hydrate (JSONObject json) {
        try {
            if (json.getString("job_position").isEmpty()) json.remove("job_position");
        } catch (Exception e) { throw new RuntimeException(e); }
        User newUser = new Gson().fromJson(json.toString(), User.class);
        if (Math.abs(newUser.getPosition().getLat()) < 1 && Math.abs(newUser.getPosition().getLng()) < 1)
            newUser.position = null;
        return newUser;
    }

    public static List<User> hydrate(JSONArray jsons) {
        LinkedList<User> users = new LinkedList<>();
        for(int i = 0; i < jsons.length(); ++i) {
            try { users.add(User.hydrate(jsons.getJSONObject(i))); }
            catch (Exception e) { Log.e("Users load", e.getMessage()); }
        }
        return users;
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

    public Bitmap getPictureBitmap() {
        if (hasPictureLoaded()) {
            byte[] pictureBytes = Base64.decode(picture, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(pictureBytes, 0, pictureBytes.length);
        } else {
            return BitmapFactory.decodeByteArray("".getBytes(), 0, 0);
        }
    }

    public void setPicture(Bitmap imageBitmap) {
        if (imageBitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] byteArray = baos.toByteArray();
            picture = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
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

    public Boolean hasPictureLoaded() {
        return picture != null && picture.length() > 0 && !picture.startsWith("http");
    }

    public Boolean hasPictureURL() {
        return picture != null && picture.startsWith("http");
    }

    public String getPictureURL() {
        return hasPictureURL() ? picture : "";
    }

    public LinkedList<Skill> getSkills() {
        return skills != null ? skills : new LinkedList<Skill>();
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

    public LinkedList<Experience> getExperiences() {
        return experiences != null ? experiences : new LinkedList<Experience>();
    }

    public void setSkills(Collection<Skill> newSkills) {
        skills = new LinkedList<>(newSkills);
    }

    public void setJobPosition(JobPosition jobPosition) {
        this.jobPosition = jobPosition;
    }

    public Position getPosition() { return position != null ? position : new Position(0,0); }

    public Boolean hasLastLocation() {
        return position != null;
    }

    public Boolean hasJobPosition() {
        return jobPosition != null;
    }

    public void setDeviceId(String device) {
        deviceId = device;
    }

    public void setExperiences(Collection<Experience> xps) {
        if (experiences != null) experiences.clear();
        else experiences = new LinkedList<>();
        experiences.addAll(xps);
    }

    public Contact toContact() {
        return Contact.fromUser(this);
    }

}
