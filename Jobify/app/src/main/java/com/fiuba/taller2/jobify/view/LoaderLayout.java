package com.fiuba.taller2.jobify.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.taller2.fiuba.jobify.R;

public class LoaderLayout extends RelativeLayout {

    private Boolean isVisible;


    public LoaderLayout(Context context) {
        super(context);
        initialize();
    }

    public LoaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public LoaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public void toggleVisibility() {
        isVisible = !isVisible;
        if (isVisible) setVisibility(View.VISIBLE);
        else setVisibility(View.GONE);
    }


    private void initialize() {
        isVisible = false;
        setVisibility(View.GONE);
        setBackgroundColor(getResources().getColor(R.color.loading_black));
        setGravity(Gravity.CENTER);
        addView(
                new ProgressBar(getContext(), null, android.R.attr.progressBarStyleLarge)
        );
    }
}
