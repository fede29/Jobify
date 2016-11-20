package com.fiuba.taller2.jobify.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v13.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.fiuba.taller2.jobify.PositionManager;
import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.adapter.SectionsPagerAdapter;
import com.fiuba.taller2.jobify.view.ProfileSection;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.taller2.fiuba.jobify.R;


public class HomeActivity extends Activity {

    User user;
    PositionManager positionManager;
    SectionsPagerAdapter sectionsPagerAdapter;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    public final static int LOCATION_PERMISSION_CODE = 1;

    private static class ExtrasKeys {
        public final static String USER = "user";

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final ActionBar actionBar = getActionBar();
        if (actionBar != null) actionBar.hide();

        user = (User) getIntent().getExtras().getSerializable(ExtrasKeys.USER);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        sectionsPagerAdapter = new SectionsPagerAdapter(this, user);
        viewPager.setAdapter(sectionsPagerAdapter);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);
        tabs.setIndicatorColor(ContextCompat.getColor(this, R.color.light_blue_700));

        positionManager = new PositionManager
                ((LocationManager) getSystemService(Context.LOCATION_SERVICE), user);
        askLocationPermission();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int request, String permissions[], int[] results) {
        if (request == LOCATION_PERMISSION_CODE) {
            if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
                positionManager.initiate();
            } else {
                Toast.makeText(this, "Location permission denied, wont be saved", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int request, int result, Intent resultIntent) {
        if (result == Activity.RESULT_OK && request == ProfileSection.EDIT_USER_REQUEST_CODE) {
            user = (User) resultIntent.getExtras().getSerializable(EditProfileActivity.ExtrasKeys.USER);
            sectionsPagerAdapter.setProfileViewFrom(user);
        }
    }

    public static Intent createIntent(Context ctx, User u) {
        Intent intent = new Intent(ctx, HomeActivity.class);
        intent.putExtra(ExtrasKeys.USER, u);
        return intent;
    }


    /**********************************************************************************************/

    private void askLocationPermission() {
        int permission = ActivityCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(
                    this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_CODE
            );
    }

}
