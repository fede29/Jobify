package com.fiuba.taller2.jobify.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.activity.EditProfileActivity;
import com.fiuba.taller2.jobify.view.ProfileBasicLayout;
import com.fiuba.taller2.jobify.view.ProfileExtendedLayout;
import com.squareup.picasso.Picasso;
import com.taller2.fiuba.jobify.R;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public final static int EDIT_USER_REQUEST_CODE = 1;

    private User user;
    private View parentView;

    private class ExtrasKeys {
        public final static String USER = "user";
    }


    public static ProfileFragment newInstance(User user) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(ExtrasKeys.USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = (User) getArguments().getSerializable(ExtrasKeys.USER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentView = inflater.inflate(R.layout.layout_profile, container, false);
        setUserViews();
        return parentView;
    }

    public void setUserViews() {
        CircleImageView profilePic = (CircleImageView) parentView.findViewById(R.id.profile_pic);
        ImageButton editProfile = (ImageButton) parentView.findViewById(R.id.edit_profile_btn);
        ProfileBasicLayout basicLayout = (ProfileBasicLayout) parentView.findViewById(R.id.basic_layout);
        ProfileExtendedLayout extendedLayout = (ProfileExtendedLayout) parentView.findViewById(R.id.extended_layout);

        basicLayout.setViews(user);
        extendedLayout.setViews(user);
        editProfile.setVisibility(View.VISIBLE);
        editProfile.setOnClickListener(new EditProfileOnClickListener());
        if (user.hasProfilePic())
            Picasso.with(getActivity()).load(user.getPictureURL()).into(profilePic);
    }

    @Override
    public void onActivityResult(int request, int result, Intent resultIntent) {
        if (result == Activity.RESULT_OK && request == EDIT_USER_REQUEST_CODE) {
            user = (User) resultIntent.getExtras().getSerializable(EditProfileActivity.ExtrasKeys.USER);
            setUserViews();
        }
    }


    /************************************** PRIVATE STUFF *****************************************/

    private class EditProfileOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            startActivityForResult(
                    EditProfileActivity.createIntent(getContext(), user),
                    EDIT_USER_REQUEST_CODE
            );
        }
    }

}
