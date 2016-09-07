package com.fiuba.taller2.jobify.utils;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class AppServerRequest {

    private static final String BASE_URL = "http://192.168.0.106:5000/"; // https://api.twitter.com/1.1/
    private static final OkHttpClient client = new OkHttpClient();
    private static String token;

    public static void updateToken(String t) {
        token = t;
    }

    public static void login(String email, String password, Callback callback) {
        // TODO: Modify parameters, send via JSON
        RequestBody formBody = new FormBody.Builder().build();
        Request request = new Request.Builder().url(BASE_URL + RequestConstants.LOGIN)
                .post(formBody)
                .addHeader(RequestConstants.User.EMAIL, email)
                .addHeader(RequestConstants.User.PASSWORD, password)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }


    /************************************** PRIVATE STUFF *****************************************/

    private static void get(String route, Callback callback) {
        Request request = new Request.Builder().url(BASE_URL + route).get().build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    private static class RequestConstants {
        public final static String LOGIN = "session";

        public class User {
            public final static String EMAIL = "email";
            public final static String PASSWORD = "password";
        }
    }
}
