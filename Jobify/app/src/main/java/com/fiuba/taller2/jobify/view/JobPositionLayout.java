package com.fiuba.taller2.jobify.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fiuba.taller2.jobify.JobPosition;
import com.taller2.fiuba.jobify.R;

public class JobPositionLayout extends LinearLayout {

    JobPosition jobPosition;
    TextView textView;


    public JobPositionLayout(Context context) {
        super(context);
        initialize();
    }

    public JobPositionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public JobPositionLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public void setJobPosition(JobPosition jp) {
        jobPosition = jp;
        textView.setText(jp.toString());
    }

    public JobPosition getJobPosition() {
        return jobPosition;
    }


    private void initialize() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_deletable_text, this);
        textView = (TextView) findViewById(R.id.text);
    }

}
