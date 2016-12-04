package com.fiuba.taller2.jobify.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fiuba.taller2.jobify.Skill;
import com.taller2.fiuba.jobify.R;


public class SkillsLayout extends LinearLayout {

    Skill skill;
    TextView skillText;


    public SkillsLayout(Context context) {
        super(context);
        initialize();
    }

    public SkillsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public SkillsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
        LayoutInflater.from(getContext()).inflate(R.layout.layout_deletable_text, this);
        skillText = (TextView) findViewById(R.id.text);
    }
}
