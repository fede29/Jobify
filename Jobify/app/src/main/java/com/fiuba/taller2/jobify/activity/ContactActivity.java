package com.fiuba.taller2.jobify.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.fiuba.taller2.jobify.Contact;
import com.fiuba.taller2.jobify.User;
import com.taller2.fiuba.jobify.R;

public class ContactActivity extends Activity {

    private Contact contact;

    private static class ExtrasKeys {
        public final static String CONTACT = "contact";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        contact = (Contact) getIntent().getExtras().getSerializable(ExtrasKeys.CONTACT);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact, menu);
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

    public static Intent createIntent(Context ctx, Contact c) {
        Intent intent = new Intent(ctx, ContactActivity.class);
        intent.putExtra(ExtrasKeys.CONTACT, c);
        return intent;
    }
}
