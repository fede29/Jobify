package com.fiuba.taller2.jobify.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fiuba.taller2.jobify.HttpCallback;
import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.constant.JSONConstants;
import com.fiuba.taller2.jobify.listener.VisibilityAnimationListener;
import com.fiuba.taller2.jobify.utils.AppServerRequest;
import com.taller2.fiuba.jobify.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;


/**
 * This activity acts as a splash screen if the user is already logged in.
 * Else, it acts as the name says.
 */
public class LoginActivity extends Activity {

    ImageView logoName;
    EditText emailEntry, passEntry;
    Button loginButton;
    RelativeLayout loaderLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final ActionBar actionBar = getActionBar();
        if (actionBar != null) actionBar.hide();

        loaderLayout = (RelativeLayout) findViewById(R.id.loader_layout);
        logoName = (ImageView) findViewById(R.id.logo_name);
        emailEntry = (EditText) findViewById(R.id.email_entry);
        passEntry = (EditText) findViewById(R.id.password_entry);
        loginButton = (Button) findViewById(R.id.login_btn);
        TextView createAccountText = (TextView) findViewById(R.id.create_account_text);
        createAccountText.setPaintFlags(createAccountText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleLoader();
                AppServerRequest.login(
                        emailEntry.getText().toString(),
                        passEntry.getText().toString(),
                        new LoginCallback(LoginActivity.this)
                );
            }
        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                // TODO: if user is logged, loadApp() and startApplication(), else animateViews()
                loadApplication();
                animateViews();
            }
        });
    }

    public void startApplication (User user) {
        startActivity(HomeActivity.createIntent(this, user));
        finish();
    }

    public void toggleLoader() {
        if (loaderLayout.getVisibility() == View.GONE) loaderLayout.setVisibility(View.VISIBLE);
        else loaderLayout.setVisibility(View.GONE);
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


    /*************************************** PRIVATE STUFF: ***************************************/

    private void loadApplication() {
    }

    private void animateViews() {
        Animation logoAnim = AnimationUtils.loadAnimation(this, R.anim.login_logo);
        logoAnim.setAnimationListener(new LogoNameAnimation());
        logoAnim.setFillAfter(true);
        logoName.startAnimation(logoAnim);
    }

    private class LogoNameAnimation implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) {
            Animation emailAnim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.login_email),
                    passwordAnim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.login_password),
                    loginBtnAnim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.login_btn);
            emailAnim.setAnimationListener(new VisibilityAnimationListener(emailEntry));
            passwordAnim.setAnimationListener(new VisibilityAnimationListener(passEntry));
            loginBtnAnim.setAnimationListener(new VisibilityAnimationListener(loginButton));
            emailEntry.startAnimation(emailAnim);
            passEntry.startAnimation(passwordAnim);
            loginButton.startAnimation(loginBtnAnim);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}
    }

    private class LoginCallback extends HttpCallback {

        public LoginCallback (Activity a) {
            super(a);
        }

        @Override
        public void onPostFailure(Call call, IOException e) {
            showLongToast("Loading dummy user");
            startApplication(User._createDummyUser());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toggleLoader();
                }
            });
        }

        @Override
        public void onPostResponse(Call call, Response response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toggleLoader();
                }
            });
        }

        @Override
        public void onStatus200(Call call, Response httpResponse) {
            try {
                JSONObject response = getJSONResponse();
                AppServerRequest.updateToken(response.getString(JSONConstants.TOKEN));
                User user = User.hydrate(response.getJSONObject(JSONConstants.User.USER));
                startApplication(user);
            } catch (Exception e) {
                e.printStackTrace();
                announceError("There was a problem, please try again later");
            }
        }
    }
}
