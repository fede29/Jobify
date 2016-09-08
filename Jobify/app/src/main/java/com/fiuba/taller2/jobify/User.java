package com.fiuba.taller2.jobify;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.service.media.MediaBrowserService;
import android.util.Log;
import android.widget.ImageView;

import com.fiuba.taller2.jobify.utils.AppServerRequest;
import com.fiuba.taller2.jobify.constant.JSONConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class User implements Serializable {

    private int id;
    private String firstName, lastName, about, profilePicUrl;


    public User() {
        id = 0;
        firstName = lastName = about = profilePicUrl = null;
    }

    public static User hydrate (JSONObject json) {
        User u = new User();
        u.loadFrom(json);
        return u;
    }

    public void loadFrom (JSONObject jsonUser) {
        try {
            firstName = jsonUser.getString(JSONConstants.User.FIRST_NAME);
            lastName = jsonUser.getString(JSONConstants.User.LAST_NAME);
            about = jsonUser.getString(JSONConstants.User.ABOUT);
            profilePicUrl = jsonUser.getString(JSONConstants.User.PROFILE_PIC_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFullname() {
        return firstName + " " + lastName;
    }

    public void loadProfilePic(ImageView img) {
        new DownloadProfilePicTask(img).execute(profilePicUrl);
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


    /*************************************** PRIVATE STUFF ****************************************/

    private class DownloadProfilePicTask extends AsyncTask<String, Void, Bitmap> {

        ImageView img;

        public DownloadProfilePicTask(ImageView img) {
            this.img = img;
        }

        @Override
        protected Bitmap doInBackground(String[] urls) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                img.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

    }

}
