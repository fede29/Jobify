package com.fiuba.taller2.jobify.activity;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;

import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.constant.JSONConstants;
import com.fiuba.taller2.jobify.fragment.NewCredentialsFragment;
import com.fiuba.taller2.jobify.fragment.NewUserFragment;
import com.fiuba.taller2.jobify.fragment.RegistrationCompleteFragment;
import com.fiuba.taller2.jobify.utils.AppServerRequest;
import com.fiuba.taller2.jobify.view.LoaderLayout;
import com.fiuba.taller2.jobify.view.NonSwipeableViewPager;
import com.taller2.fiuba.jobify.R;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegistrationActivity extends Activity
        implements NewCredentialsFragment.OnRegistrationListener,
        NewUserFragment.OnUserEditListener,
        RegistrationCompleteFragment.OnRegistrationCompleteListener {

    private String email, password;
    private User newUser;
    private LoaderLayout loaderLayout;
    private CirclePageIndicator pageIndicator;
    private SectionsPagerAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        final ActionBar actionBar = getActionBar();
        if (actionBar != null) actionBar.hide();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        pagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        loaderLayout = (LoaderLayout) findViewById(R.id.loader_layout);
        NonSwipeableViewPager viewPager = (NonSwipeableViewPager) findViewById(R.id.pager);
        pageIndicator = (CirclePageIndicator) findViewById(R.id.page_indicator);
        viewPager.setAdapter(pagerAdapter);
        pageIndicator.setViewPager(viewPager);
    }

    public static Intent createIntent(Context ctx) {
        return new Intent(ctx, RegistrationActivity.class);
    }

    /**
     * LoaderLayout must be here to be on top of the page indicator
     */
    @Override
    public void toggleLoader() {
        loaderLayout.toggleVisibility();
    }

    @Override
    public void onRegistration(User user, String e, String pass) {
        newUser = user;
        email = e;
        password = pass;
        ((NewUserFragment) pagerAdapter.getItem(1)).useUser(newUser);
        pageIndicator.setCurrentItem(1);
    }

    @Override
    public void onUserEdit(User user) {
        ((RegistrationCompleteFragment) pagerAdapter.getItem(2)).useUser(newUser);
        pageIndicator.setCurrentItem(2);
    }

    @Override
    public void onRegistrationComplete() {
        toggleLoader();
        AppServerRequest.login(email, password, new LoginCallback());
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private NewCredentialsFragment newCredentialsFragment;
        private NewUserFragment newUserFragment;
        private RegistrationCompleteFragment registrationCompleteFragment;


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            newCredentialsFragment = NewCredentialsFragment.newInstance();
            newUserFragment = NewUserFragment.newInstance();
            registrationCompleteFragment = RegistrationCompleteFragment.newInstance();
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return newCredentialsFragment;
                case 1:
                    return newUserFragment;
                default:
                    return registrationCompleteFragment;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }


    /*************************************** PRIVATE STUFF ****************************************/

    private class LoginCallback implements Callback {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.e("Registration login", e.getMessage());
            e.printStackTrace();
            startActivity(LoginActivity.createIntent(RegistrationActivity.this));
            finish();
        }

        @Override
        public void onResponse(Call call, Response httpResponse) throws IOException {
            try {
                JSONObject jsonResponse = new JSONObject(httpResponse.body().string());
                if (httpResponse.code() == 200) {
                    AppServerRequest.updateToken(jsonResponse.getString(JSONConstants.TOKEN));
                    startActivity(
                            HomeActivity.createIntent(
                                    RegistrationActivity.this,
                                    User.hydrate(jsonResponse.getJSONObject(JSONConstants.User.USER))
                            )
                    );
                } else {
                    startActivity(LoginActivity.createIntent(RegistrationActivity.this));
                    Log.e(
                            "Registration login",
                            String.format("code=%d, %s", httpResponse.code(), jsonResponse.getString(JSONConstants.ERROR_MESSAGE))
                    );
                }
            } catch (JSONException e) {
                Log.e("JSON load", e.getMessage());
                e.printStackTrace();
                startActivity(LoginActivity.createIntent(RegistrationActivity.this));
            }
            finish();
        }
    }

}
