package com.fiuba.taller2.jobify.view;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fiuba.taller2.jobify.Contact;
import com.fiuba.taller2.jobify.adapter.QueryResultsAdapter;
import com.fiuba.taller2.jobify.constant.JSONConstants;
import com.fiuba.taller2.jobify.utils.AppServerRequest;
import com.fiuba.taller2.jobify.utils.HttpCallback;
import com.taller2.fiuba.jobify.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;


public class SearchSection extends RelativeLayout {

    Activity activity;
    EditText queryText;
    ImageView searchButton;
    RecyclerView resultsList;
    QueryResultsAdapter resultsAdapter;
    ProgressBar resultsLoader;
    TextView noResultsText;

    public SearchSection(Activity act) {
        super(act);
        activity = act;
        initialize();
    }

    public SearchSection(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public SearchSection(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }


    /**************************************** PRIVATE STUFF ***************************************/

    private void initialize() {
        LayoutInflater.from(getContext()).inflate(R.layout.section_search, this);
        queryText = (EditText) findViewById(R.id.query);
        searchButton = (ImageView) findViewById(R.id.search_btn);
        resultsList = (RecyclerView) findViewById(R.id.query_results);
        resultsLoader = (ProgressBar) findViewById(R.id.results_loader);
        noResultsText = (TextView) findViewById(R.id.no_results_text);

        queryText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int action, KeyEvent keyEvent) {
                return action == EditorInfo.IME_ACTION_SEARCH && searchButton.callOnClick();
            }
        });

        resultsAdapter = new QueryResultsAdapter(getContext());
        resultsList.setAdapter(resultsAdapter);

        resultsList.setLayoutManager(new LinearLayoutManager(getContext()));
        searchButton.setOnClickListener(new OnSearchClickListener());
    }

    private void setResults(List<Contact> contacts) {
        resultsLoader.setVisibility(View.GONE);
        resultsAdapter.setResults(contacts);
        if (contacts.size() > 0) noResultsText.setVisibility(View.GONE);
        else noResultsText.setVisibility(View.VISIBLE);
    }


    private class OnSearchClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            resultsAdapter.clear();
            resultsLoader.setVisibility(View.VISIBLE);
            noResultsText.setVisibility(View.GONE);
            /* TODO: Generalize search query
            AppServerRequest.searchUsers(queryText.getText().toString(), new QueryResultsCallback());
            */
            AppServerRequest.getUser(queryText.getText().toString(), new SingleUserResultCallback());
        }
    }

    /**
     * Class to be deleted when search generalizes
     */
    private class SingleUserResultCallback extends HttpCallback {
        @Override
        public void onResponse() {
            try {
                if (statusIs(200)) {
                    LinkedList<Contact> contactList = new LinkedList<>();
                    JSONObject jsonUser = getJSONResponse().getJSONObject(JSONConstants.User.USER);
                    Contact c = Contact.hydrate(jsonUser);
                    c.setId(queryText.getText().toString());
                    contactList.add(c);
                    activity.runOnUiThread(new SetResults(contactList));
                } else {
                    activity.runOnUiThread(new SetResults(new LinkedList<Contact>()));
                }
            } catch (JSONException e) {
                Log.e("Single user query", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private class QueryResultsCallback extends HttpCallback {
        @Override
        public void onResponse() {
            try {
                JSONArray usersArray = getJSONResponse().getJSONArray(JSONConstants.Arrays.CONTANCTS);
                List<Contact> contacts = Contact.hydrate(usersArray);
                activity.runOnUiThread(new SetResults(contacts));
            } catch (Exception e) {
                Log.e("Users query", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public class SetResults implements Runnable {
        private List<Contact> contacts;

        public SetResults(List<Contact> c) {
            contacts = c;
        }

        @Override
        public void run() {
            setResults(contacts);
        }
    }
}
