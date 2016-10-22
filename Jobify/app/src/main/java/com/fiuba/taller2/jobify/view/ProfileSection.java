package com.fiuba.taller2.jobify.view;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.activity.EditProfileActivity;
import com.fiuba.taller2.jobify.view.ProfileBasicLayout;
import com.fiuba.taller2.jobify.view.ProfileExtendedLayout;
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
        ImageButton editProfile = (ImageButton) findViewById(R.id.edit_profile_btn);
        ProfileBasicLayout basicLayout = (ProfileBasicLayout) findViewById(R.id.basic_layout);
        ProfileExtendedLayout extendedLayout = (ProfileExtendedLayout) findViewById(R.id.extended_layout);

        basicLayout.setViews(user);
        extendedLayout.setViews(user);
        editProfile.setVisibility(View.VISIBLE);
        editProfile.setOnClickListener(new EditProfileOnClickListener());
        if (user.hasProfilePic())
            Picasso.with(getContext()).load(user.getPictureURL()).into(profilePic);
    }

    //@Override
    public void onActivityResult(int request, int result, Intent resultIntent) {
        if (result == Activity.RESULT_OK && request == EDIT_USER_REQUEST_CODE) {
            //user = (User) resultIntent.getExtras().getSerializable(EditProfileActivity.ExtrasKeys.USER);
            //setUserViews();
        }
    }


    /************************************** PRIVATE STUFF *****************************************/

    private void initialize() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_profile, this);
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
