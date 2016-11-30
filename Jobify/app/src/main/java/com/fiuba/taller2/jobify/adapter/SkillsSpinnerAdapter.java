package com.fiuba.taller2.jobify.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fiuba.taller2.jobify.Skill;
import com.taller2.fiuba.jobify.R;

import java.util.LinkedList;
import java.util.List;


public class SkillsSpinnerAdapter extends ArrayAdapter<Skill> {

    public SkillsSpinnerAdapter(Context ctx) {
        this(ctx, new LinkedList<Skill>());
    }

    public SkillsSpinnerAdapter(Context ctx, List<Skill> list) {
        super(ctx, R.layout.view_text, list);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View view = super.getView(pos, convertView, parent);
        ((TextView) view).setText("Select a skill...");
        return view;
    }
}
