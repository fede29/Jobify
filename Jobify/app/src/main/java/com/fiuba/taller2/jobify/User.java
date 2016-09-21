package com.fiuba.taller2.jobify;


import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class User implements Serializable {

    @SerializedName("id")           int id;
    @SerializedName("first_name")   String firstName;
    @SerializedName("last_name")    String lastName;
    @SerializedName("about")        String about;
    @SerializedName("profile_pic")  String pictureURL;
    @SerializedName("contacts")
    @Expose(serialize = false)      ArrayList<Contact> contacts;
    @Expose(serialize = false, deserialize = false)
                                    ArrayList<Chat> chats;
    @SerializedName("skills")
    @Expose(serialize = false)      ArrayList<Skill> skills;


    public static User hydrate (JSONObject json) {
        return new Gson().fromJson(json.toString(), User.class);
    }

    public String serialize() {
        return new Gson().toJson(this);
    }

    public void hydrateChats(JSONArray jsonChats) {
        Type listType = new TypeToken<ArrayList<Chat>>(){}.getType();
        chats = new Gson().fromJson(jsonChats.toString(), listType);
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

    public static User _createDummyUser() {
        String jsonString = "{ \"id\": 1, \"first_name\": \"Dummy\", \"last_name\": \"User\", " +
                "\"email\": \"dummy@email.com\", \"about\": \"Lorem ipsum blah blah asda...\", " +
                "\"profile_pic\": \"https://slm-assets1.secondlife.com/assets/9889950/view_large/(GL)_training_dummy_3.jpg?1403622529\"}";
        try {
            JSONObject jsonUser = new JSONObject(jsonString);
            return User.hydrate(jsonUser);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public String getJobPosition() {
        return "Brrom traffic consultant";
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
}
