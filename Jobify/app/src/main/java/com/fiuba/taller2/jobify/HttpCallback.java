package com.fiuba.taller2.jobify;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.fiuba.taller2.jobify.constant.JSONConstants;
import com.fiuba.taller2.jobify.utils.AppServerRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * This class is a skelleton for a HTTP request callback from the AppServer. It implements basic
 *  and generic error handling.
 * It is not ment to be instanciated, but only to be extended.
 * Child classes can implement their own onFailure() method, and should implement onResponse(),
 *  in both cases calling the super methods first. Besides, child classes can override methods
 *  onStatusXXX() to perform particular task according to the server status given
 */
public abstract class HttpCallback implements Callback {

    private Activity activity;
    private JSONObject jsonResponse;


    public HttpCallback (Activity a) {
        activity = a;
    }

    public JSONObject getJSONResponse() {
        return jsonResponse;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Log.w("Request fail", e.getMessage());
        String message;
        if (e instanceof SocketTimeoutException) {
            message = "Connection failed. Please, check your Internet connection.";
        } else {
            message = "There was an error. Please try again later.";
        }
        announceError(message);
        onPostFailure(call, e);
    }

    @Override
    public final void onResponse(Call call, Response response) throws IOException {
        //* if metadata == json
        try {
            jsonResponse = new JSONObject(response.body().string());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //*/
        switch (response.code()) {
            case 200:
                onStatus200(call, response); break;
            case 201:
                onStatus201(call, response); break;
            case 404:
                onStatus404(call, response); break;
            case 500:
                onStatus500(call, response); break;
        }
        onPostResponse(call, response);
    }

    protected void onStatus200(Call c, Response r) {}

    protected void onStatus201(Call c, Response r) {}

    protected void onStatus404(Call c, Response r) {
        Log.w("Server status", "404");
        announceDefaultError();
    }

    protected void onStatus500(Call c, Response r) {
        Log.e("Server status", "500");
        announceDefaultError();
    }

    protected void onPostResponse(Call c, Response r) {}

    protected void onPostFailure(Call c, IOException e) {}

    /*************************************** PRIVATE STUFF ****************************************/

    protected void announceError(String message) {
        showLongToast(message);
    }

    protected void announceDefaultError() {
        showLongToast("There was an error. Please, try again later");
    }

    protected void showLongToast (final String message) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        });
    }

}
