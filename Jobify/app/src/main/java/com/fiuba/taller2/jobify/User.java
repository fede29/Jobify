package com.fiuba.taller2.jobify;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class User implements Serializable {

    @Expose                     @SerializedName("id")           int id;
    @Expose                     @SerializedName("first_name")   String firstName;
    @Expose                     @SerializedName("last_name")    String lastName;
    @Expose                     @SerializedName("about")        String about;
    @Expose                     @SerializedName("profile_pic")  String pictureURL;
    @Expose                     @SerializedName("job_position") JobPosition jobPosition;
    @Expose(serialize = false)  @SerializedName("location")     Position position;
    @Expose(serialize = false)  @SerializedName("contacts")     ArrayList<Contact> contacts;
    @Expose(serialize = false)  @SerializedName("skills")       ArrayList<Skill> skills;
    @Expose(serialize = false)  @SerializedName("experiences")  ArrayList<Experience> experiences;

    private ArrayList<Chat> chats;


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
        return contacts;
    }

    public Integer getID() {
        return id;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public String getJobPosition() {
        return jobPosition.getName();
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

    public Boolean hasChatsLoaded() {
        return chats != null;
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }

    public ArrayList<Skill> getSkills() {
        return skills;
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
        return experiences;
    }

}
