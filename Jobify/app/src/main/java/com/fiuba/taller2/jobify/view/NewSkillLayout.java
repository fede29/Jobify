package com.fiuba.taller2.jobify.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fiuba.taller2.jobify.Skill;
import com.taller2.fiuba.jobify.R;


public class NewSkillLayout extends LinearLayout {

    Skill skill;
    TextView skillText;


    public NewSkillLayout(Context context) {
        super(context);
        initialize();
    }

    public NewSkillLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public NewSkillLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
        skillText.setText(skill.getName());
    }

    public Skill getSkill() {
        return skill;
    }


    private void initialize() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_new_skill, this);
        skillText = (TextView) findViewById(R.id.skill_text);
    }
}
