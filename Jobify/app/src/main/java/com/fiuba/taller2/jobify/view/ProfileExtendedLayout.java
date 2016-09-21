package com.fiuba.taller2.jobify.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.fiuba.taller2.jobify.Skill;
import com.fiuba.taller2.jobify.User;
import com.taller2.fiuba.jobify.R;

import org.apmem.tools.layouts.FlowLayout;

public class ProfileExtendedLayout extends RelativeLayout {

    FlowLayout skillsLayout;


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

    public void setViews(User user) {
        for (Skill skill : user.getSkills()) {
            View skillView =
                    LayoutInflater.from(getContext()).inflate(R.layout.view_skill, null);
            TextView skillText = (TextView) skillView.findViewById(R.id.skill_text);
            skillText.setText(skill.getName());
            skillsLayout.addView(skillView);
        }
    }


    /*************************************** PRIVATE STUFF ****************************************/

    private void initialize() {
        inflate(getContext(), R.layout.view_profile_extended_info, this);
        skillsLayout = (FlowLayout) findViewById(R.id.skills_layout);
    }
}
