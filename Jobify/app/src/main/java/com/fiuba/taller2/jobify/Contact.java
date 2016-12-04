package com.fiuba.taller2.jobify;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;
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


@IgnoreExtraProperties
public class Contact implements Serializable {

    @Expose @SerializedName("id")           String email;
    @Expose @SerializedName("first_name")   String firstName;
    @Expose @SerializedName("last_name")    String lastName;
    @Expose @SerializedName("profile_pic")
    @PropertyName("pic")                    String pictureURL;
    @Expose(deserialize  = false) @SerializedName("job_position") JobPosition jobPosition;

    @Exclude private User user;


    public static Contact hydrate(JSONObject json) {
        return new Gson().fromJson(json.toString(), Contact.class);
    }

    public static List<Contact> hydrate(JSONArray array) {
        Type listType = new TypeToken<ArrayList<Contact>>(){}.getType();
        return new Gson().fromJson(array.toString(), listType);
    }

    static Contact fromUser(User u) {
        JSONObject jsonContact = new JSONObject();
        try {
            jsonContact.put("id", u.getID());
            jsonContact.put("first_name", u.getFirstName());
            jsonContact.put("last_name", u.getLastName());
            jsonContact.put("profile_pic", u.getPictureBitmap());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hydrate(jsonContact);
    }

    public void setFirstName(String fn) {
        firstName = fn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String ln) {
        lastName = ln;
    }

    public String getLastName() {
        return lastName;
    }

    public String fullname() {
        return String.format("%s %s", firstName, lastName);
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public String getId() {
        return email;
    }

    public User haveUser() {
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
