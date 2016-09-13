package com.fiuba.taller2.jobify.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Loader;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.fiuba.taller2.jobify.view.LoaderLayout;
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
    TextView createAccountText;
    Button loginButton;
    LoaderLayout loaderLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
                AppServerRequest.login(
                        emailEntry.getText().toString(),
                        passEntry.getText().toString(),
                        new LoginCallback(LoginActivity.this)
                );
            }
        });

        createAccountText.setOnClickListener(new OnCreateAccountClickListener());

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

    private class OnCreateAccountClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            startActivity(RegistrationActivity.createIntent(LoginActivity.this));
        }
    }

    private class LogoNameAnimation implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) {
            View[] views = {
                    emailEntry, passEntry, loginButton, findViewById(R.id.or_layout_2),
                    createAccountText
            };
            int delaySum = 0;
            for (View view : views) {
                Animation anim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.login_view);
                anim.setStartOffset(anim.getStartOffset() + delaySum);
                delaySum += 100;
                anim.setAnimationListener(new VisibilityAnimationListener(view));
                view.startAnimation(anim);
            }
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
                    loaderLayout.toggleVisibility();
                }
            });
        }

        @Override
        public void onPostResponse(Call call, Response response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loaderLayout.toggleVisibility();
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
            } catch (JSONException e) {
                Log.e("Login", e.getMessage());
                announceError("There was a problem, please try again later");
            }
        }
    }
}
