package com.fiuba.taller2.jobify.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.fiuba.taller2.jobify.Filter;
import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.view.SearchSection;
import com.fiuba.taller2.jobify.view.ContactsSection;
import com.fiuba.taller2.jobify.view.ChatsSection;
import com.fiuba.taller2.jobify.view.ProfileSection;
import com.taller2.fiuba.jobify.R;

import java.util.Locale;


public class SectionsPagerAdapter extends PagerAdapter
        implements PagerSlidingTabStrip.IconTabProvider {

    Context context;

    ProfileSection profileSection;
    ChatsSection chatsSection;
    ContactsSection contactsSection;
    SearchSection searchSection;


    public SectionsPagerAdapter(Activity activity, User user) {
        context = activity;
        profileSection = new ProfileSection(activity);
        chatsSection = new ChatsSection(activity);
        contactsSection = new ContactsSection(context);
        searchSection = new SearchSection(activity);
        profileSection.setViewsFrom(user);
        chatsSection.setViewsFrom(user);
        contactsSection.setViewsFrom(user);
    }


    @Override
    public Object instantiateItem(ViewGroup collection, int pos) {
        switch (pos) {
            case 0:
                collection.addView(chatsSection);
                return chatsSection;
            case 1:
                collection.addView(contactsSection);
                return contactsSection;
            case 2:
                collection.addView(profileSection);
                return profileSection;
            case 3:
                collection.addView(searchSection);
                return searchSection;
            default: return null;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup collection, int pos, Object view) {
        collection.removeView((View) view);
    }


    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public int getPageIconResId(int position) {
        switch (position) {
            case 0: return R.drawable.ic_chat_darkcyan;
            case 1: return R.drawable.ic_dossier_darkcyan;
            case 2: return R.drawable.ic_user_darkcyan;
            case 3: return R.drawable.ic_search_darkcyan;
            default: return 0;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0: return context.getString(R.string.chats_title).toUpperCase(l);
            case 1: return context.getString(R.string.contacts_title).toUpperCase(l);
            case 2: return context.getString(R.string.profile_title).toUpperCase(l);
            case 3: return context.getString(R.string.search_title).toUpperCase(l);
            default: return "";
        }
    }

    public void setProfileViewFrom(User u) {
        profileSection.setViewsFrom(u);
    }

    public void setFilterToSearch(Filter f) {
        searchSection.setFilter(f);
    }
}