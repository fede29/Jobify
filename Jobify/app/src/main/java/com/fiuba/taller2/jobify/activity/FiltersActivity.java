package com.fiuba.taller2.jobify.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.fiuba.taller2.jobify.Filter;
import com.fiuba.taller2.jobify.JobPosition;
import com.fiuba.taller2.jobify.Skill;
import com.fiuba.taller2.jobify.adapter.JobPositionsSpinnerAdapter;
import com.fiuba.taller2.jobify.adapter.SkillsSpinnerAdapter;
import com.fiuba.taller2.jobify.utils.AppServerRequest;
import com.fiuba.taller2.jobify.utils.HttpCallback;
import com.fiuba.taller2.jobify.view.JobPositionLayout;
import com.fiuba.taller2.jobify.view.SkillsLayout;
import com.taller2.fiuba.jobify.R;

import org.apmem.tools.layouts.FlowLayout;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;


public class FiltersActivity extends Activity {

    LinearLayout containerLayout;
    SeekBar rangeBar;
    TextView rangeText;
    Spinner jobPositions, skills;
    JobPositionsSpinnerAdapter jpAdapter;
    SkillsSpinnerAdapter skillsAdapter;
    FlowLayout skillsLayout;
    TextView jpTextView;
    Boolean jpLoaded, skillsLoaded;
    ProgressBar loader;
    Filter filter;

    Boolean firstJPCall = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Filters");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        filter = (Filter) getIntent().getExtras().getSerializable("filter");
        jpLoaded = skillsLoaded = false;

        containerLayout = (LinearLayout) findViewById(R.id.filters_layout);
        rangeBar = (SeekBar) findViewById(R.id.range_seekbar);
        rangeText = (TextView) findViewById(R.id.range_text);
        jobPositions = (Spinner) findViewById(R.id.job_positions_spinner);
        skills = (Spinner) findViewById(R.id.skills_spinner);
        jpTextView = (TextView) findViewById(R.id.job_position);
        skillsLayout = (FlowLayout) findViewById(R.id.skills_layout);
        loader = (ProgressBar) findViewById(R.id.progress_bar);

        // Initializing
        if (filter.hasRange()) rangeText.setText(String.format("%dkm", filter.getRange()));
        else rangeText.setText("No filter");
        AppServerRequest.getJobPositions(new JobPositionsCallback());
        AppServerRequest.getSkills(new SkillsCallback());
        rangeBar.setOnSeekBarChangeListener(new OnRangeChangeListener());
        jpAdapter = new JobPositionsSpinnerAdapter(this);
        jobPositions.setAdapter(jpAdapter);
        jobPositions.setOnItemSelectedListener(new OnJobPositionClickListener());
        jpTextView.setOnClickListener(new OnJobPositionRemovedListener());
        skillsAdapter = new SkillsSpinnerAdapter(this);
        skills.setOnItemSelectedListener(new OnSkillSelectedListener());
        skills.setAdapter(skillsAdapter);

        // Set views from current filter
        if (filter.hasRange()) rangeBar.setProgress(filter.getRange());
        else rangeBar.setProgress(100);
        for (Skill skill : filter.getSkills())
            addSkill(skill);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.save_filters:
                Intent filterIntent = new Intent();
                filterIntent.putExtra("filter", filter);
                setResult(Activity.RESULT_OK, filterIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public static Intent createIntent(Context ctx, Filter filter) {
        Intent intent = new Intent(ctx, FiltersActivity.class);
        intent.putExtra("filter", filter);
        return intent;
    }



    private void chooseJobPosition(JobPosition jp) {
        jpTextView.setText(jp.toString());
        jpTextView.setVisibility(View.VISIBLE);
        jobPositions.setVisibility(View.GONE);
    }

    private void addSkill(Skill skill) {
        filter.addSkill(skill);
        SkillsLayout newSkillLayout = new SkillsLayout(FiltersActivity.this);
        newSkillLayout.setOnClickListener(new OnSkillClickListener());
        newSkillLayout.setSkill(skill);
        skillsLayout.addView(newSkillLayout);
    }

    private void setJobPositionsSpinner(List<JobPosition> list) {
        jpAdapter = new JobPositionsSpinnerAdapter(this, list);
        jobPositions.setAdapter(jpAdapter);
        if (filter.hasJobPosition()) chooseJobPosition(filter.getJobPosition());
        jpLoaded = true;
        endLoading();
    }

    private void setSkillsSpinner(List<Skill> list) {
        skillsAdapter = new SkillsSpinnerAdapter(this, list);
        skills.setAdapter(skillsAdapter);
        skillsLoaded = true;
        endLoading();
    }

    private void endLoading() {
        if (jpLoaded && skillsLoaded) {
            containerLayout.setVisibility(View.VISIBLE);
            loader.setVisibility(View.GONE);
        }
    }

    private class OnRangeChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if (i == 100) {
                filter.removeRange();
                rangeText.setText("No filter");
            } else {
                filter.setRange(i+1);
                rangeText.setText(String.format("%dkm", i+1));
            }
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }

    private class JobPositionsCallback extends HttpCallback {
        @Override
        public void onResponse() {
            try {
                JSONArray jpsArray = getJSONResponse().getJSONArray("job_positions");
                runOnUiThread(new SetJPSpinner(JobPosition.hydrate(jpsArray)));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Job Positions load", e.getMessage());
            }
        }

        private class SetJPSpinner implements Runnable {
            private List<JobPosition> jps;

            public SetJPSpinner(List<JobPosition> l) { jps = l; }

            @Override
            public void run() {
                setJobPositionsSpinner(jps);
            }
        }
    }

    private class SkillsCallback extends HttpCallback {
        @Override
        public void onResponse() {
            try {
                JSONArray skillsArray = getJSONResponse().getJSONArray("skills");
                runOnUiThread(new SetSkillsSpinner(Skill.hydrate(skillsArray)));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Job Positions load", e.getMessage());
            }
        }

        private class SetSkillsSpinner implements Runnable {
            private List<Skill> list;

            public SetSkillsSpinner(List<Skill> l) { list = l; }

            @Override
            public void run() {
                setSkillsSpinner(list);
            }
        }
    }

    private class OnJobPositionClickListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (i < adapterView.getCount() && !firstJPCall) {
                JobPosition jp = (JobPosition) adapterView.getItemAtPosition(i);
                filter.setJobPosition(jp);
                chooseJobPosition(jp);
            } else {
                firstJPCall = false;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    }

    private class OnJobPositionRemovedListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            view.setVisibility(View.GONE);
            jobPositions.setVisibility(View.VISIBLE);
        }
    }

    private class OnSkillSelectedListener implements AdapterView.OnItemSelectedListener {
        Boolean firstCall = true;

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (i < adapterView.getCount() && !firstCall) {
                Skill skill = (Skill) adapterView.getItemAtPosition(i);
                if (! filter.getSkills().contains(skill)) addSkill(skill);
            } else {
                firstCall = false;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    }

    private class OnSkillClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Skill skill = ((SkillsLayout) view).getSkill();
            filter.remove(skill);
            skillsLayout.removeView(view);
        }
    }
}
