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
import android.widget.EditText;
import android.widget.ImageView;

import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.utils.AppServerRequest;
import com.fiuba.taller2.jobify.utils.HttpCallback;
import com.fiuba.taller2.jobify.view.LoaderLayout;
import com.squareup.picasso.Picasso;
import com.taller2.fiuba.jobify.R;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class EditProfileActivity extends Activity {

    User user, oldUser;
    EditText firstName, lastName, about;
    ImageView profilePic, editProfile;
    Bitmap imageBitmap;
    LoaderLayout loader;

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

        oldUser = (User) getIntent().getExtras().getSerializable(ExtrasKeys.USER);
        user = oldUser;
        firstName = (EditText) findViewById(R.id.first_name);
        lastName = (EditText) findViewById(R.id.last_name);
        about = (EditText) findViewById(R.id.about);
        loader = (LoaderLayout) findViewById(R.id.loader_layout);
        profilePic = (ImageView) findViewById(R.id.profile_pic);
        editProfile = (ImageView) findViewById(R.id.edit_profile_btn);

        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        about.setText(user.getAbout());
        if (user.hasProfilePic())
            Picasso.with(this).load(user.getPictureURL()).into(profilePic);
        View.OnClickListener profileClickListener = new OnProfileClickListener();
        profilePic.setOnClickListener(profileClickListener);
        editProfile.setOnClickListener(profileClickListener);
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
                editUser();
                // TODO: upload image
                AppServerRequest.updateUser(user, new EditUserCallback());
                loader.setVisible(true);
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
    }

    private void finishForEdit() {
        Intent result = new Intent();
        result.putExtra(ExtrasKeys.USER, user);
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    private class EditUserCallback extends HttpCallback {
        @Override
        public void onResponse() {
            if (statusIs(200)) {
                finishForEdit();
            } else {
                loader.setVisible(false);
            }
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

}
