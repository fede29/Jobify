package com.fiuba.taller2.jobify.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.constant.JSONConstants;
import com.fiuba.taller2.jobify.utils.AppServerRequest;
import com.fiuba.taller2.jobify.utils.HttpCallback;
import com.fiuba.taller2.jobify.view.LoaderLayout;
import com.taller2.fiuba.jobify.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.fiuba.taller2.jobify.fragment.NewCredentialsFragment.OnRegistrationListener} interface
 * to handle interaction events.
 * Use the {@link NewCredentialsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewCredentialsFragment extends Fragment {

    private OnRegistrationListener mListener;
    private EditText emailEntry, passwordEntry;

    public static NewCredentialsFragment newInstance() {
        return new NewCredentialsFragment();
    }

    public NewCredentialsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_new_credentials, container, false);

        ImageButton viewPass = (ImageButton) rootView.findViewById(R.id.password_visibility);
        Button nextBtn = (Button) rootView.findViewById(R.id.next_btn);
        emailEntry = (EditText) rootView.findViewById(R.id.email_entry);
        passwordEntry = (EditText) rootView.findViewById(R.id.password_entry);
        viewPass.setOnClickListener(new OnSeePasswordClickListener());
        nextBtn.setOnClickListener(new OnNextClickListener());

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnRegistrationListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that activity.
     */
    public interface OnRegistrationListener {
        public void onRegistration(User u, String e, String pass);
        public void toggleLoader();
    }


    /*********************************** PRIVATE STUFF ********************************************/

    private class OnSeePasswordClickListener implements View.OnClickListener {

        Boolean isVisible;

        public OnSeePasswordClickListener() {
            isVisible = false;
        }

        @Override
        public void onClick(View view) {
            isVisible = ! isVisible;
            if (isVisible) {
                passwordEntry.setInputType(
                        InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                );
                ((ImageButton) view).setImageResource(R.drawable.ic_visibility);
            } else {
                passwordEntry.setInputType(
                        InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD
                );
                ((ImageButton) view).setImageResource(R.drawable.ic_visibility_off);
            }
        }
    }

    private class OnNextClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            mListener.toggleLoader();
            AppServerRequest.register(
                    emailEntry.getText().toString(),
                    passwordEntry.getText().toString(),
                    new RegisterCallback()
            );
        }
    }

    private class RegisterCallback extends HttpCallback {

        public RegisterCallback() { super(getActivity()); }

        @Override
        public void onFailure(Call call, IOException e) {
            super.onFailure(call, e);
            toggleLoader();
        }

        @Override
        public void onResponse() {
            try {
                if (statusIs(200)) {
                    AppServerRequest.updateToken(getToken());
                    onRegistrationAccepted();
                } else {
                    showLongToast(getErrorMessage());
                }
            } catch (JSONException e) {
                Log.e("JSON Load", e.getMessage());
                e.printStackTrace();
            }
            toggleLoader();
        }

        private void onRegistrationAccepted() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mListener.onRegistration(
                                User.hydrate(getJSONObject(JSONConstants.User.USER)),
                                emailEntry.getText().toString(),
                                passwordEntry.getText().toString()
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        private void toggleLoader() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mListener.toggleLoader();
                }
            });
        }
    }

}
