package com.fiuba.taller2.jobify.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fiuba.taller2.jobify.Location;
import com.fiuba.taller2.jobify.Skill;
import com.fiuba.taller2.jobify.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.taller2.fiuba.jobify.R;

import org.apmem.tools.layouts.FlowLayout;

public class ProfileExtendedLayout extends RelativeLayout {

    FlowLayout skillsLayout;
    TextView about;
    ExperiencesLayout experiences;
    LinearLayout locationLayout;


    public ProfileExtendedLayout(Context context) {
        super(context);
        initialize();
    }

    public ProfileExtendedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public ProfileExtendedLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public void setViews(Activity act, User user, GoogleMap map) {
        setSkills(user);
        if (user.hasLastLocation()) setupMap(act, map, user);
        else locationLayout.setVisibility(GONE);
        about.setText(user.getAbout());
        experiences.setViews(user.getExperiences());
    }


    /*************************************** PRIVATE STUFF ****************************************/

    private void initialize() {
        inflate(getContext(), R.layout.view_profile_extended_info, this);
        skillsLayout = (FlowLayout) findViewById(R.id.skills_layout);
        about = (TextView) findViewById(R.id.about_text);
        experiences = (ExperiencesLayout) findViewById(R.id.experiences_layout);
        locationLayout = (LinearLayout) findViewById(R.id.location_layout);
    }

    private void setSkills(User user) {
        skillsLayout.removeAllViews();
        for (Skill skill : user.getSkills()) {
            View skillView =
                    LayoutInflater.from(getContext()).inflate(R.layout.view_skill, null);
            TextView skillText = (TextView) skillView.findViewById(R.id.skill_text);
            skillText.setText(skill.getName());
            skillsLayout.addView(skillView);
        }
    }

    private void setupMap(final Activity activity, GoogleMap map, final User user) {
        map.getUiSettings().setAllGesturesEnabled(false);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(user.getLastLocation().getLatLng(), 14.0f));
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Location lastLocation = user.getLastLocation();
                String uri = String.format("geo:%f,%f", lastLocation.getLat(), lastLocation.getLng());
                Uri gmmIntentUri = Uri.parse(uri);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                activity.startActivity(mapIntent);
            }
        });
    }

}
