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
import android.widget.Toast;

import com.fiuba.taller2.jobify.Contact;
import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.adapter.QueryResultsAdapter;
import com.fiuba.taller2.jobify.constant.JSONConstants;
import com.fiuba.taller2.jobify.utils.AppServerRequest;
import com.fiuba.taller2.jobify.utils.HttpCallback;
import com.taller2.fiuba.jobify.R;

import org.json.JSONArray;

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

    private void setResults(List<User> users) {
        resultsLoader.setVisibility(View.GONE);
        resultsAdapter.setResults(users);
        if (users.size() > 0) noResultsText.setVisibility(View.GONE);
        else noResultsText.setVisibility(View.VISIBLE);
    }


    private class OnSearchClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            resultsAdapter.clear();
            resultsLoader.setVisibility(View.VISIBLE);
            noResultsText.setVisibility(View.GONE);
            AppServerRequest.searchUsers(queryText.getText().toString(), new QueryResultsCallback());
        }
    }

    private class QueryResultsCallback extends HttpCallback {
        @Override
        public void onResponse() {
            try {
                JSONArray usersArray = getJSONResponse().getJSONArray(JSONConstants.Arrays.USERS);
                List<User> users = User.hydrate(usersArray);
                activity.runOnUiThread(new SetResults(users));
            } catch (Exception e) {
                Log.e("Users query", e.getMessage());
                resultsLoader.setVisibility(View.GONE);
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    public class SetResults implements Runnable {
        private List<User> users;

        public SetResults(List<User> c) {
            users = c;
        }

        @Override
        public void run() {
            setResults(users);
        }
    }
}
