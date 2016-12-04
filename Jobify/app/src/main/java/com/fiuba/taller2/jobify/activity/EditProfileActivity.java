package com.fiuba.taller2.jobify.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.taller2.jobify.Experience;
import com.fiuba.taller2.jobify.JobPosition;
import com.fiuba.taller2.jobify.Skill;
import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.adapter.JobPositionsSpinnerAdapter;
import com.fiuba.taller2.jobify.adapter.SkillsSpinnerAdapter;
import com.fiuba.taller2.jobify.constant.JSONConstants;
import com.fiuba.taller2.jobify.utils.AppServerRequest;
import com.fiuba.taller2.jobify.utils.HttpCallback;
import com.fiuba.taller2.jobify.view.EditExperiencesLayout;
import com.fiuba.taller2.jobify.view.LoaderLayout;
import com.fiuba.taller2.jobify.view.SkillsLayout;
import com.squareup.picasso.Picasso;
import com.taller2.fiuba.jobify.R;

import org.apmem.tools.layouts.FlowLayout;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;


public class EditProfileActivity extends Activity {

    User user, oldUser;
    EditText firstName, lastName, about;
    ImageView profilePic, editProfile;
    Bitmap imageBitmap;
    LoaderLayout loader;
    Spinner skillsSpinner, jobPositions;
    FlowLayout skillsLayout;
    LinkedList<Skill> userSkills;
    TextView selectedJobPosition;
    SkillsSpinnerAdapter skillsAdapter;
    JobPositionsSpinnerAdapter jobPositionsAdapter;
    EditExperiencesLayout experiences;

    Boolean firstJPCall = true;
    Boolean skillsLoaded = false, jobPositionsLoaded = false;


    public int PICK_PHOTO_REQUEST_CODE = 1;

    public class ExtrasKeys {
        public static final String USER = "user";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        final ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Edit profile");
        }

        userSkills = new LinkedList<>();

        oldUser = (User) getIntent().getExtras().getSerializable(ExtrasKeys.USER);
        user = oldUser;
        firstName = (EditText) findViewById(R.id.first_name);
        lastName = (EditText) findViewById(R.id.last_name);
        about = (EditText) findViewById(R.id.about);
        loader = (LoaderLayout) findViewById(R.id.loader_layout);
        profilePic = (ImageView) findViewById(R.id.profile_pic);
        editProfile = (ImageView) findViewById(R.id.edit_profile_btn);
        skillsSpinner = (Spinner) findViewById(R.id.skills_spinner);
        skillsLayout = (FlowLayout) findViewById(R.id.skills_layout);
        selectedJobPosition = (TextView) findViewById(R.id.job_position);
        jobPositions = (Spinner) findViewById(R.id.job_positions_spinner);
        jobPositionsAdapter = new JobPositionsSpinnerAdapter(this);

        AppServerRequest.getSkills(new SkillsLoaderCallback());
        AppServerRequest.getJobPositions(new JobPositionsLoaderCallback());
        OnSkillSelectedListener spinnerListener = new OnSkillSelectedListener();
        skillsSpinner.setOnItemSelectedListener(spinnerListener);
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        about.setText(user.getAbout());
        if (user.hasPictureLoaded()) profilePic.setImageBitmap(user.getPictureBitmap());
        else if (user.hasPictureURL()) Picasso.with(this).load(user.getPictureURL()).into(profilePic);
        View.OnClickListener profileClickListener = new OnProfileClickListener();
        profilePic.setOnClickListener(profileClickListener);
        editProfile.setOnClickListener(profileClickListener);
        experiences = (EditExperiencesLayout) findViewById(R.id.edit_experiences_layout);
        experiences.setupView(user.getExperiences());
        experiences.setOnNewExperienceListener(new OnNewExperienceListener());
        experiences.setOnDeleteExperienceListener(new OnDeleteExperienceListener());
        jobPositions.setAdapter(jobPositionsAdapter);
        jobPositions.setOnItemSelectedListener(new OnJobPositionClickListener());
        selectedJobPosition.setOnClickListener(new OnJobPositionRemovedListener());
        for (Skill skill : user.getSkills())
            addSkill(skill);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                askConfirmation();
                return true;
            case R.id.save_edit:
                if (validExperiences()) {
                    editUser();
                    AppServerRequest.updateUser(user, new EditUserCallback(this));
                    loader.setVisible(true);
                } else {
                    Toast.makeText(this, "Invalid experiences", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int reqCode, int result, Intent imageIntent) {
        if (result == Activity.RESULT_OK && reqCode == PICK_PHOTO_REQUEST_CODE) {
            try {
                InputStream imgStream = getContentResolver().openInputStream(imageIntent.getData());
                imageBitmap = BitmapFactory.decodeStream(imgStream);
                profilePic.setImageBitmap(imageBitmap);
            } catch (FileNotFoundException e) {
                Log.e("Photo pick error", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        askConfirmation();
    }

    public static Intent createIntent(Context ctx, User user) {
        Intent intent = new Intent(ctx, EditProfileActivity.class);
        intent.putExtra(ExtrasKeys.USER, user);
        return intent;
    }


    /*************************************** PRIVATE STUFF ****************************************/

    private void addSkill(Skill skill) {
        userSkills.add(skill);
        SkillsLayout newSkillLayout = new SkillsLayout(EditProfileActivity.this);
        newSkillLayout.setOnClickListener(new OnSkillClickListener());
        newSkillLayout.setSkill(skill);
        skillsLayout.addView(newSkillLayout);
    }

    private void askConfirmation() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing user edit")
                .setMessage("Changes will be lost. Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    private void editUser() {
        user.setFirstName(firstName.getText().toString());
        user.setLastName(lastName.getText().toString());
        user.setAbout(about.getText().toString());
        user.setSkills(userSkills);
        user.setExperiences(generateExperiences());
        user.setPicture(imageBitmap);
        user.setJobPosition((JobPosition) selectedJobPosition.getTag());
    }

    private void finishForEdit() {
        Intent result = new Intent();
        result.putExtra(ExtrasKeys.USER, user);
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    private void setSkillsSpinner(List<Skill> list) {
        skillsAdapter = new SkillsSpinnerAdapter(this, list);
        skillsSpinner.setAdapter(skillsAdapter);
        skillsLoaded = true;
        endLoading();
    }

    private void setJobPositionsSpinner(List<JobPosition> list) {
        jobPositionsAdapter = new JobPositionsSpinnerAdapter(this, list);
        jobPositions.setAdapter(jobPositionsAdapter);
        if (user.hasJobPosition()) {
            firstJPCall = false;
            jobPositions.setSelection(jobPositionsAdapter.getPosition(user.getJobPosition()));
        }
        jobPositionsLoaded = true;
        endLoading();
    }

    private List<Experience> generateExperiences() {
        return experiences.generateModels();
    }

    private Boolean validExperiences() {
        return experiences.areValid();
    }

    private void endLoading() {
        if (skillsLoaded && jobPositionsLoaded) {
            findViewById(R.id.progress_bar).setVisibility(View.GONE);
            findViewById(R.id.edit_profile_container).setVisibility(View.VISIBLE);
        }
    }


    private class EditUserCallback extends HttpCallback {
        public EditUserCallback(Activity act) {
            super(act);
        }

        @Override
        public void onFailure(Call call, IOException e) {
            super.onFailure(call, e);
            cancelEdit();
        }

        @Override
        public void onResponse() {
            if (statusIs(200)) {
                finishForEdit();
            } else {
                cancelEdit();
            }
        }

        private void cancelEdit() {
            loader.setVisible(false);
            announceError("Edit not successful");
        }
    }

    private class SkillsLoaderCallback extends HttpCallback {
        @Override
        public void onResponse() {
            try {
                JSONArray jsonSkills = getJSONResponse().getJSONArray(JSONConstants.Arrays.SKILLS);
                List<Skill> skills = Skill.hydrate(jsonSkills);
                runOnUiThread(new SetSkillsSpinner(skills));
            } catch (JSONException e) {
                Log.e("Skills load", e.getMessage());
                e.printStackTrace();
            }
        }

        private class SetSkillsSpinner implements Runnable {
            List<Skill> skills;

            public SetSkillsSpinner(List<Skill> list) {
                skills = list;
            }

            @Override
            public void run() {
                setSkillsSpinner(skills);
            }
        }
    }

    private class JobPositionsLoaderCallback extends HttpCallback {
        @Override
        public void onResponse() {
            try {
                JSONArray jps = getJSONResponse().getJSONArray("job_positions");
                List<JobPosition> jobPositions = JobPosition.hydrate(jps);
                runOnUiThread(new SetJobPositionsSpinner(jobPositions));
            } catch (JSONException e) {
                Log.e("JobPositions load", e.getMessage());
                e.printStackTrace();
            }
        }

        private class SetJobPositionsSpinner implements Runnable {
            List<JobPosition> jps;

            SetJobPositionsSpinner(List<JobPosition> list) { jps = list; }

            @Override
            public void run() { setJobPositionsSpinner(jps); }
        }
    }

    private class OnProfileClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_PHOTO_REQUEST_CODE);
        }
    }

    private class OnSkillSelectedListener implements AdapterView.OnItemSelectedListener {
        Boolean firstSelect = true;

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (i < adapterView.getCount() && !firstSelect) {
                Skill skill = (Skill) adapterView.getItemAtPosition(i);
                addSkill(skill);
            } else {
                firstSelect = false;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    }

    private class OnSkillClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Skill skill = ((SkillsLayout) view).getSkill();
            userSkills.remove(skill);
            skillsLayout.removeView(view);
        }
    }

    private class OnDeleteExperienceListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            experiences.removeViewAt((Integer) view.getTag());
        }
    }

    private class OnNewExperienceListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (experiences.areValid()) {
                experiences.addNewExperience();
                experiences.setOnDeleteExperienceListener(new OnDeleteExperienceListener());
            } else {
                Toast.makeText(EditProfileActivity.this, "Complete all experiences", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class OnJobPositionClickListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (i < adapterView.getCount() && !firstJPCall) {
                JobPosition jp = (JobPosition) adapterView.getItemAtPosition(i);
                selectedJobPosition.setText(jp.toString());
                selectedJobPosition.setVisibility(View.VISIBLE);
                selectedJobPosition.setTag(jp);
                jobPositions.setVisibility(View.GONE);
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
            selectedJobPosition.setTag(null);
            view.setVisibility(View.GONE);
            jobPositions.setVisibility(View.VISIBLE);
        }
    }

}
