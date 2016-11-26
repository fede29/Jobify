package com.fiuba.taller2.jobify.utils;


import android.util.Log;

import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.activity.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class AppServerRequest {

    private static final String BASE_URL = "http://192.168.0.106:8081/api";
    private static final OkHttpClient client = new OkHttpClient();
    private static String token;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static User currentUser;


    public static void setForUser(User u) {
        currentUser = u;
    }

    public static void updateToken(String t) {
        token = t;
    }

    public static void login(String email, String password, Callback callback) {
        JSONObject params = generateJSON(
                RequestConstants.UserParams.EMAIL, email,
                RequestConstants.UserParams.PASSWORD, password
        );
        RequestBody body = RequestBody.create(JSON, params.toString());
        post(generateURL(RequestConstants.Routes.LOGIN), callback, body);
    }

    public static void facebookLogin(String fbToken, Callback callback) {
        get(generateURL(RequestConstants.Routes.LOGIN), callback);
    }

    public static void register(String email, String password, Callback callback) {
        JSONObject params = generateJSON(
                RequestConstants.UserParams.EMAIL, email,
                RequestConstants.UserParams.PASSWORD, password
        );
        RequestBody body = RequestBody.create(JSON, params.toString());
        post(generateURL(RequestConstants.Routes.USERS), callback, body);
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void getContacts(int userID, Callback callback) {
        String route = generateURL(
                RequestConstants.Routes.USERS, userID,
                RequestConstants.Routes.CONTACTS
        ) ;
        get(route, callback);
    }

    public static void getUser(Object userID, Callback callback) {
        get(generateURL(RequestConstants.Routes.USERS, userID), callback);
    }

    public static void updateUser(User user, Callback callback) {
        String route = generateURL(RequestConstants.Routes.USERS, String.valueOf(user.getID()));
        RequestBody body = RequestBody.create(JSON, user.serialize());
        put(route, callback, body);
    }

    public static void updateLocation(User user, Callback callback) {
        String route = generateURL(
                RequestConstants.Routes.USERS, user.getID(),
                RequestConstants.Routes.LOCATION
        );
        RequestBody body = RequestBody.create(JSON, user.getPosition().serialize());
        post(route, callback, body);
    }

    public static void searchUsers(String query, Callback callback) {
        String route = addParameters(generateURL(RequestConstants.Routes.USERS), "query", query);
        get(route, callback);
    }

    public static void followUser(Object followedId, Callback callback) {
        String route = generateURL(
                RequestConstants.Routes.USERS, currentUser.getID(),
                RequestConstants.Routes.CONTACTS
        );
        JSONObject params = new JSONObject();
        try {
            params.put(RequestConstants.UserParams.EMAIL, followedId.toString());
        } catch (JSONException e) {
            Log.e("Follow request", e.getMessage());
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, params.toString());
        post(route, callback, body);
    }

    public static void getSkills(Callback callback) {
        get(generateURL(RequestConstants.Routes.SKILLS), callback);
    }


    public static void get(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void post(String url, Callback callback, RequestBody body) {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void put(String url, Callback callback, RequestBody body) {
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }


    /************************************** PRIVATE STUFF *****************************************/

    private static String generateURL(Object... uris) {
        String url = BASE_URL;
        for (Object uri : uris)
            url += String.format("/%s", uri);
        return url;
    }

    private static String addParameters(String route, Object... params) {
        route += "?";
        for (int i = 0; i < params.length; i += 2)
            route += String.format("%s=%s", params[i], params[i + 1]);
        return route;
    }

    private static JSONObject generateJSON(Object... attributes) {
        JSONObject json = new JSONObject();
        try {
            for (int i = 0; i < attributes.length; i += 2)
                json.put(attributes[i].toString(), attributes[i+1]);
        } catch (JSONException e) {
            Log.e("JSON build", e.getMessage());
            e.printStackTrace();
        }
        return json;
    }

    private static class RequestConstants {
        class Routes {
            final static String LOGIN = "session";
            final static String USERS = "users";
            final static String CONTACTS = "contacts";
            final static String LOCATION = "location";
            final static String SKILLS = "skills";
        }

        class UserParams {
            final static String EMAIL = "email";
            final static String PASSWORD = "password";
            final static String FB_TOKEN = "token";
        }
    }

}
