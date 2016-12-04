package com.fiuba.taller2.jobify.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fiuba.taller2.jobify.Experience;
import com.taller2.fiuba.jobify.R;

import java.util.List;

public class ExperiencesLayout extends LinearLayout {

    public ExperiencesLayout(Context context) {
        super(context);
    }

    public ExperiencesLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExperiencesLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setViews(List<Experience> experiences) {
        removeAllViews();
        int xpNo = 0;
        for (Experience experience : experiences) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.view_experience, null);
            if (++xpNo % 2 == 1) view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_blue_200));
            else view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_blue_100));
            ((TextView) view.findViewById(R.id.where)).setText(experience.getPlace());
            ((TextView) view.findViewById(R.id.job_position)).setText(experience.getJobPosition());
            addView(view);
        }
    }

}
