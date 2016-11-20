package com.fiuba.taller2.jobify.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiuba.taller2.jobify.utils.FirebaseHelper;
import com.fiuba.taller2.jobify.utils.HttpCallback;
import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.constant.JSONConstants;
import com.fiuba.taller2.jobify.utils.AppServerRequest;
import com.fiuba.taller2.jobify.view.LoaderLayout;
import com.taller2.fiuba.jobify.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;


public class LoginActivity extends Activity {

    ImageView logoName;
    EditText emailEntry, passEntry;
    TextView createAccountText;
    Button loginButton;
    LoaderLayout loaderLayout;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseHelper.initialize(this);
        final ActionBar actionBar = getActionBar();
        if (actionBar != null) actionBar.hide();

        loaderLayout = (LoaderLayout) findViewById(R.id.loader_layout);
        logoName = (ImageView) findViewById(R.id.logo_name);
        emailEntry = (EditText) findViewById(R.id.email_entry);
        passEntry = (EditText) findViewById(R.id.password_entry);
        loginButton = (Button) findViewById(R.id.login_btn);
        createAccountText = (TextView) findViewById(R.id.create_account_text);
        createAccountText.setPaintFlags(createAccountText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loaderLayout.toggleVisibility();
                String email = emailEntry.getText().toString(),
                        password = passEntry.getText().toString();
                AppServerRequest.login(email, password, new LoginCallback());
                FirebaseHelper.login(email, password);
            }
        });

        createAccountText.setOnClickListener(new OnCreateAccountClickListener());
    }

    public void startApplication (User user) {
        startActivity(HomeActivity.createIntent(this, user));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Intent createIntent(Context ctx) {
        return new Intent(ctx, LoginActivity.class);
    }


    /*************************************** PRIVATE STUFF: ***************************************/

    private class OnCreateAccountClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            startActivity(RegistrationActivity.createIntent(LoginActivity.this));
            finish();
        }
    }

    private class LoginCallback extends HttpCallback {

        LoginCallback() { super(LoginActivity.this); }

        @Override
        public void onFailure(Call call, IOException e) {
            super.onFailure(call, e);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loaderLayout.toggleVisibility();
                }
            });
        }

        @Override
        public void onResponse() {
            try {
                JSONObject response = getJSONResponse();
                User user = User.hydrate(response.getJSONObject(JSONConstants.User.USER));
                AppServerRequest.updateToken(response.getString(JSONConstants.TOKEN));
                AppServerRequest.setForUser(user);
                startApplication(user);
            } catch (JSONException e) {
                Log.e("Login", e.getMessage());
                announceError("There was a problem, please try again later");
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loaderLayout.toggleVisibility();
                }
            });
        }
    }
}
