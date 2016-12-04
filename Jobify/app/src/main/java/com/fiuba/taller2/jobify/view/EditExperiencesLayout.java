package com.fiuba.taller2.jobify.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.fiuba.taller2.jobify.Experience;
import com.taller2.fiuba.jobify.R;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class EditExperiencesLayout extends LinearLayout {

    List<Experience> experiences;
    LinearLayout newExperience;


    public EditExperiencesLayout(Context context, List<Experience> xps) {
        super(context);
        experiences = xps;
        initialize();
    }

    public EditExperiencesLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        experiences = new LinkedList<>();
        initialize();
    }

    public EditExperiencesLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        experiences = new LinkedList<>();
        initialize();
    }

    public void setOnNewExperienceListener(OnClickListener onNewXPListener) {
        newExperience.setOnClickListener(onNewXPListener);
    }

    public void setOnDeleteExperienceListener(OnClickListener onDeleteXPListener) {
        for (int i = 0; i < getChildCount() - 1; ++i) {
            EditExperience editXP = (EditExperience) getChildAt(i);
            editXP.setOnDeleteListener(onDeleteXPListener, i);
        }
    }

    public EditExperience addNewExperience() {
        EditExperience view = new EditExperience(getContext());
        int xpCount = getChildCount() - 1;
        addView(view, xpCount);
        return view;
    }

    public LinkedList<Experience> generateModels() {
        LinkedList<Experience> xps = new LinkedList<>();
        for (int i = 0; i < getChildCount() - 1; ++i) {
            EditExperience view = (EditExperience) getChildAt(i);
            xps.add(view.generateModel());
        }
        return xps;
    }

    public void setupView(Collection<Experience> xps) {
        experiences.clear();
        experiences.addAll(xps);
        fillExperiences();
    }

    public Boolean areValid() {
        for (int i = 0; i < getChildCount() - 1; ++i)
            if (! ((EditExperience) getChildAt(i)).isValid())
                return false;
        return true;
    }


    private void initialize() {
        setOrientation(VERTICAL);
        fillExperiences();
        newExperience = new LinearLayout(getContext());
        LayoutInflater.from(getContext()).inflate(R.layout.view_new_experience, newExperience);
        addView(newExperience);
    }

    private void fillExperiences() {
        int xpIndex = 0;
        for (Experience xp : experiences) {
            EditExperience view = new EditExperience(getContext(), xp);
            addView(view, xpIndex++);
        }
    }


}
