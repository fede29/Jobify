package com.fiuba.taller2.jobify.view;


import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.facebook.login.LoginManager;
import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.activity.EditProfileActivity;
import com.fiuba.taller2.jobify.activity.LoginActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.software.shell.fab.ActionButton;
import com.squareup.picasso.Picasso;
import com.taller2.fiuba.jobify.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileSection extends RelativeLayout {

    Activity activity;
    User user;

    public final static int EDIT_USER_REQUEST_CODE = 1;


    public ProfileSection(Activity act) {
        super(act);
        activity = act;
        initialize();
    }

    public ProfileSection(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public ProfileSection(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public void setViewsFrom(User u) {
        user = u;
        ((MapFragment) activity.getFragmentManager().findFragmentById(R.id.map))
                .getMapAsync(new OnLocationMapReady());
    }


    /************************************** PRIVATE STUFF *****************************************/

    private void initialize() {
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.unpure_white));
        LayoutInflater.from(getContext()).inflate(R.layout.section_profile, this);
        findViewById(R.id.layout_logout_btn).setVisibility(VISIBLE);
        Button logout = (Button) findViewById(R.id.log_out_btn);
        logout.setVisibility(VISIBLE);
        logout.setOnClickListener(new OnLogoutListener());
    }

    private class OnLogoutListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            LoginManager.getInstance().logOut();
            activity.finish();
            activity.startActivity(LoginActivity.createIntent(getContext()));
        }
    }

    private class EditProfileOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            activity.startActivityForResult(
                    EditProfileActivity.createIntent(getContext(), user),
                    EDIT_USER_REQUEST_CODE
            );
        }
    }

    private class OnLocationMapReady implements OnMapReadyCallback {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            CircleImageView profilePic = (CircleImageView) findViewById(R.id.profile_pic);
            ActionButton editProfile = (ActionButton) findViewById(R.id.edit_profile_btn);
            ProfileBasicLayout basicLayout = (ProfileBasicLayout) findViewById(R.id.basic_layout);
            ProfileExtendedLayout extendedLayout = (ProfileExtendedLayout) findViewById(R.id.extended_layout);

            basicLayout.setViews(user);
            extendedLayout.setViews(activity, user, googleMap);
            editProfile.setVisibility(VISIBLE);
            editProfile.setOnClickListener(new EditProfileOnClickListener());
            if (user.hasPictureLoaded()) profilePic.setImageBitmap(user.getPictureBitmap());
            else if (user.hasPictureURL())
                Picasso.with(getContext()).load(user.getPictureURL()).into(profilePic);
        }
    }

}
