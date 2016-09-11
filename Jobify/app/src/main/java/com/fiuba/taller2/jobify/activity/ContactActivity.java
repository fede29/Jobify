package com.fiuba.taller2.jobify.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.fiuba.taller2.jobify.Contact;
import com.fiuba.taller2.jobify.HttpCallback;
import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.constant.JSONConstants;
import com.fiuba.taller2.jobify.utils.AppServerRequest;
import com.fiuba.taller2.jobify.view.ProfileBasicLayout;
import com.fiuba.taller2.jobify.view.ProfileExtendedLayout;
import com.squareup.picasso.Picasso;
import com.taller2.fiuba.jobify.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

public class ContactActivity extends Activity {

    private Contact contact;

    private static class ExtrasKeys {
        public final static String CONTACT = "contact";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        contact = (Contact) getIntent().getExtras().getSerializable(ExtrasKeys.CONTACT);
        AppServerRequest.getUser(contact.getUserID(), new UserLoadCallback(this));
        final ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(contact.getFullName() + "'s profile");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Intent createIntent(Context ctx, Contact c) {
        Intent intent = new Intent(ctx, ContactActivity.class);
        intent.putExtra(ExtrasKeys.CONTACT, c);
        return intent;
    }


    /************************************** PRIVATE STUFF *****************************************/

    private void setupContactView() {
        findViewById(R.id.progress_bar).setVisibility(View.GONE);
        findViewById(R.id.profile_layout).setVisibility(View.VISIBLE);
        ((ProfileBasicLayout) findViewById(R.id.basic_layout)).setViews(contact.getUser());
        ((ProfileExtendedLayout) findViewById(R.id.extended_layout)).setViews(contact.getUser());
        CircleImageView profilePic = (CircleImageView) findViewById(R.id.profile_pic);
        Picasso.with(this).load(contact.getPictureURL()).into(profilePic);
    }

    private class UserLoadCallback extends HttpCallback {

        public UserLoadCallback(Activity a) {
            super(a);
        }

        @Override
        public void onStatus200(Call call, Response httpResponse) {
            try {
                contact.setUser(User.hydrate(getJSONResponse().getJSONObject(JSONConstants.User.USER)));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setupContactView();
                    }
                });
            } catch (JSONException e) {
                Log.e("User json response", e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
