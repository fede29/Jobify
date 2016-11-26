package com.fiuba.taller2.jobify.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fiuba.taller2.jobify.Skill;
import com.taller2.fiuba.jobify.R;

import java.util.List;

public class SkillsSpinnerAdapter extends ArrayAdapter<Skill> {

    public SkillsSpinnerAdapter(Context context, List<Skill> skills) {
        super(context, R.layout.text_skill, skills);
        add(new Skill());
    }

    @Override
    public int getCount() {
        return super.getCount() > 1 ? super.getCount() - 1 : 1;
    }

    public void insert(Skill skill) {
        insert(skill, 0);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        if (position == super.getCount() - 1) {
            ((TextView) v.findViewById(R.id.skill_text)).setHint("Select a new skill...");
        }
        return v;
    }
}
