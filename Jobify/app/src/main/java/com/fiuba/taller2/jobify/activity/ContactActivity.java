package com.fiuba.taller2.jobify.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.fiuba.taller2.jobify.Contact;
import com.fiuba.taller2.jobify.utils.HttpCallback;
import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.constant.JSONConstants;
import com.fiuba.taller2.jobify.utils.AppServerRequest;
import com.fiuba.taller2.jobify.view.FollowButton;
import com.fiuba.taller2.jobify.view.ProfileBasicLayout;
import com.fiuba.taller2.jobify.view.ProfileExtendedLayout;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.squareup.picasso.Picasso;
import com.taller2.fiuba.jobify.R;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactActivity extends Activity {

    private Contact contact;
    FollowButton followBtn;

    private static class ExtrasKeys {
        final static String CONTACT = "contact";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        contact = (Contact) getIntent().getExtras().getSerializable(ExtrasKeys.CONTACT);
        if (contact != null && contact.hasUserLoaded())
            setupContactView();
        else if (contact != null)
            AppServerRequest.getUser(contact.getId(), new UserLoadCallback());

        final ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setTitle(contact.getFullname() + "'s profile");
            actionBar.setTitle("Contact profile");
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
        MapFragment mapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(new OnLocationMapReady());
    }

    private class OnFollowClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            AppServerRequest.followUser(contact.getId(), new UserFollowCallback());
        }
    }

    private class UserFollowCallback extends HttpCallback {
        @Override
        public void onResponse() {
            if (statusIs(200)) runOnUiThread(new SetFollowing());
        }

        private class SetFollowing implements Runnable {
            @Override
            public void run() {
                followBtn.setFollowing();
            }
        }
    }

    private class UserLoadCallback extends HttpCallback {

        @Override
        public void onResponse() {
            try {
                JSONObject jsonUser = getJSONResponse().getJSONObject(JSONConstants.User.USER);
                contact.setUser(User.hydrate(jsonUser));
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

    private class OnLocationMapReady implements OnMapReadyCallback {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            followBtn = (FollowButton) findViewById(R.id.follow_btn);
            findViewById(R.id.progress_bar).setVisibility(View.GONE);
            findViewById(R.id.profile_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.message_contact_btn).setVisibility(View.VISIBLE);
            ProfileExtendedLayout profileExtendedLayout =
                    ((ProfileExtendedLayout) findViewById(R.id.extended_layout));

            profileExtendedLayout.setViews(ContactActivity.this, contact.getUser(), googleMap);

            followBtn.setVisibility(View.VISIBLE);
            followBtn.setOnClickListener(new OnFollowClickListener());

            ((ProfileBasicLayout) findViewById(R.id.basic_layout)).setViews(contact.getUser());
            CircleImageView profilePic = (CircleImageView) findViewById(R.id.profile_pic);
            if (contact.hasProfilePic())
                Picasso.with(ContactActivity.this).load(contact.getPictureURL()).into(profilePic);
        }
    }
}
