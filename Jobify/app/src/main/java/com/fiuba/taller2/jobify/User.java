package com.fiuba.taller2.jobify;


import com.fiuba.taller2.jobify.constant.JSONConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class User implements Serializable {

    private int id;
    private String firstName, lastName, about, pictureURL;
    ArrayList<Contact> contacts;


    public User() {
        id = 0;
        firstName = lastName = about = pictureURL = null;
        contacts = null;
    }

    public static User hydrate (JSONObject json) {
        User u = new User();
        u.loadFrom(json);
        return u;
    }

    public void loadFrom (JSONObject jsonUser) {
        try {
            id = jsonUser.getInt(JSONConstants.ID);
            firstName = jsonUser.getString(JSONConstants.User.FIRST_NAME);
            lastName = jsonUser.getString(JSONConstants.User.LAST_NAME);
            about = jsonUser.getString(JSONConstants.User.ABOUT);
            pictureURL = jsonUser.getString(JSONConstants.User.PROFILE_PIC_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFullname() {
        return firstName + " " + lastName;
    }

    public Boolean hasContactsLoaded() {
        return contacts != null;
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

    public void addContacts(Collection<Contact> c) {
        if (contacts == null) contacts = new ArrayList<>(c.size());
        contacts.addAll(c);
    }


    public String getPictureURL() {
        return pictureURL;
    }
}
