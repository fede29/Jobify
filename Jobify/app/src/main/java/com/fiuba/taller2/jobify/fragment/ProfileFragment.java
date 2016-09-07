package com.fiuba.taller2.jobify.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.activity.EditProfileActivity;
import com.taller2.fiuba.jobify.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private User user;
    private View parentView;
    private ImageButton profilePic;

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
        parentView = inflater.inflate(R.layout.fragment_profile, container, false);
        setUserViews();
        return parentView;
    }

    public void setUserViews() {
        TextView name = (TextView) parentView.findViewById(R.id.name),
                jobPosition = (TextView) parentView.findViewById(R.id.job_position),
                about = (TextView) parentView.findViewById(R.id.about_text);
        profilePic = (ImageButton) parentView.findViewById(R.id.profile_pic);

        name.setText(user.getFullname());
        ImageButton editProfile = (ImageButton) parentView.findViewById(R.id.edit_profile_btn);
        editProfile.setOnClickListener(new EditProfileOnClickListener());
        user.loadProfilePic(profilePic);
    }


    /************************************** PRIVATE STUFF *****************************************/

    private class EditProfileOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            startActivity(EditProfileActivity.createIntent(getContext(), user));
        }
    }

}
