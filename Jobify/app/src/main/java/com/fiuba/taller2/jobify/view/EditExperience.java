package com.fiuba.taller2.jobify.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.fiuba.taller2.jobify.Experience;
import com.fiuba.taller2.jobify.JobPosition;
import com.taller2.fiuba.jobify.R;

public class EditExperience extends LinearLayout {

    EditText where;
    EditText jobPosition;


    public EditExperience(Context ctx) {
        super(ctx);
        initialize(null);
    }

    public EditExperience(Context context, Experience xp) {
        super(context);
        initialize(xp);
    }

    public EditExperience(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(null);
    }

    public EditExperience(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(null);
    }

    public void setOnDeleteListener(OnClickListener listener, int pos) {
        View deleteImg = findViewById(R.id.delete_experience);
        deleteImg.setTag(pos);
        deleteImg.setOnClickListener(listener);
    }

    public Experience generateModel() {
        Experience xp = new Experience();
        xp.setPlace(where.getText().toString());
        xp.setJobPosition(jobPosition.getText().toString());
        return xp;
    }

    public Boolean isValid() {
        return !where.getText().toString().isEmpty() && !jobPosition.getText().toString().isEmpty();
    }


    private void initialize(Experience model) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_edit_experience, this);
        where = (EditText) findViewById(R.id.where);
        jobPosition = (EditText) findViewById(R.id.job_position);
        if (model != null) {
            where.setText(model.getPlace());
            jobPosition.setText(model.getJobPosition().toString());
        }
    }

}
