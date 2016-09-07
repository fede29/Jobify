package com.fiuba.taller2.jobify.listener;

import android.view.View;
import android.view.animation.Animation;

/**
 * AnimationListener meant to set visible the given View.
 * This is useful for building layouts as they would end, but with the animated views on
 *  visibility gone (for example, entering the screen from outside)
 */
public class VisibilityAnimationListener implements Animation.AnimationListener {

    private View view;


    public VisibilityAnimationListener (View v) {
        view = v;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationEnd(Animation animation) {}

    @Override
    public void onAnimationRepeat(Animation animation) {}
}
