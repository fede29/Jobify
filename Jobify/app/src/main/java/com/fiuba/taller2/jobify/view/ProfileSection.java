package com.fiuba.taller2.jobify.view;


import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.activity.EditProfileActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
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
        CircleImageView profilePic = (CircleImageView) findViewById(R.id.profile_pic);
        ActionButton editProfile = (ActionButton) findViewById(R.id.edit_profile_btn);
        ProfileBasicLayout basicLayout = (ProfileBasicLayout) findViewById(R.id.basic_layout);
        ProfileExtendedLayout extendedLayout = (ProfileExtendedLayout) findViewById(R.id.extended_layout);
        GoogleMap map = ((MapFragment) activity.getFragmentManager().findFragmentById(R.id.map)).getMap();

        basicLayout.setViews(user);
        extendedLayout.setViews(activity, user, map);
        editProfile.setVisibility(View.VISIBLE);
        editProfile.setOnClickListener(new EditProfileOnClickListener());
        if (user.hasProfilePic())
            Picasso.with(getContext()).load(user.getPictureURL()).into(profilePic);
    }


    /************************************** PRIVATE STUFF *****************************************/

    private void initialize() {
        LayoutInflater.from(getContext()).inflate(R.layout.section_profile, this);
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

}
