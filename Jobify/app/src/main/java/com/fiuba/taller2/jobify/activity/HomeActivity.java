package com.fiuba.taller2.jobify.activity;

import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.fragment.ChatsFragment;
import com.fiuba.taller2.jobify.fragment.ContactsFragment;
import com.fiuba.taller2.jobify.fragment.ProfileFragment;
import com.fiuba.taller2.jobify.fragment.SearchFragment;
import com.taller2.fiuba.jobify.R;


public class HomeActivity extends Activity {

    private User user;

    private static class ExtrasKeys {
        public final static String USER = "user";
    }

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    public static Context getAppContext() {
        return getAppContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final ActionBar actionBar = getActionBar();
        actionBar.hide();

        user = (User) getIntent().getExtras().getSerializable(ExtrasKeys.USER);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);
        tabs.setIndicatorColor(getResources().getColor(R.color.darkcyan));
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

    public static Intent createIntent(Context ctx, User u) {
        Intent intent = new Intent(ctx, HomeActivity.class);
        intent.putExtra(ExtrasKeys.USER, u);
        return intent;
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter
            implements PagerSlidingTabStrip.IconTabProvider {

        private final int TABS_COUNT = 4;


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // FragmentPagerAdapter keeps things in memory, so there is no need to saved the instances
            switch (position) {
                case 0:
                    return ChatsFragment.newInstance();
                case 1:
                    return ContactsFragment.newInstance();
                case 2:
                    return ProfileFragment.newInstance(user);
                case 3:
                    return SearchFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return TABS_COUNT;
        }

        @Override
        public int getPageIconResId(int position) {
            switch (position) {
                case 0:
                    return R.drawable.ic_chat_darkblue;
                case 1:
                    return R.drawable.ic_dossier_darkblue;
                case 2:
                    return R.drawable.ic_user_darkblue;
                case 3:
                    return R.drawable.ic_search_darkblue;
            }
            return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.chats_title).toUpperCase(l);
                case 1:
                    return getString(R.string.contacts_title).toUpperCase(l);
                case 2:
                    return getString(R.string.profile_title).toUpperCase(l);
                case 3:
                    return getString(R.string.search_title).toUpperCase(l);
            }
            return null;
        }
    }

}
