package com.fiuba.taller2.jobify.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fiuba.taller2.jobify.JobPosition;
import com.taller2.fiuba.jobify.R;

import java.util.LinkedList;
import java.util.List;


public class JobPositionsSpinnerAdapter extends ArrayAdapter<JobPosition> {

    public JobPositionsSpinnerAdapter(Context ctx) {
        this(ctx, new LinkedList<JobPosition>());
    }

    public JobPositionsSpinnerAdapter(Context ctx, List<JobPosition> jps) {
        super(ctx, R.layout.view_text, jps);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View view = super.getView(pos, convertView, parent);
        ((TextView) view).setText("Select a job position...");
        return view;
    }

}
