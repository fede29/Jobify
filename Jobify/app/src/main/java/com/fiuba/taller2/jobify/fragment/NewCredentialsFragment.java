package com.fiuba.taller2.jobify.fragment;

import android.app.Activity;
import android.net.Uri;
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

import com.fiuba.taller2.jobify.HttpCallback;
import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.constant.JSONConstants;
import com.fiuba.taller2.jobify.utils.AppServerRequest;
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
 * {@link NewCredentialsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewCredentialsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewCredentialsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private EditText emailEntry, passwordEntry;
    LoaderLayout loaderLayout;

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
        loaderLayout = (LoaderLayout) rootView.findViewById(R.id.loader_layout);
        viewPass.setOnClickListener(new OnSeePasswordClickListener());
        nextBtn.setOnClickListener(new OnNextClickListener());

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
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
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(User u);
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
            loaderLayout.toggleVisibility();
            AppServerRequest.register(
                    emailEntry.getText().toString(),
                    passwordEntry.getText().toString(),
                    new RegisterCallback()
            );
        }
    }

    private class RegisterCallback implements Callback {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.e("Registration", e.getMessage());
            e.printStackTrace();
            toggleLoader();
        }

        @Override
        public void onResponse(Call call, Response httpResponse) throws IOException {
            try {
                JSONObject jsonResponse = new JSONObject(httpResponse.body().string());
                if (httpResponse.code() == 200) {
                    AppServerRequest.updateToken(jsonResponse.getString(JSONConstants.TOKEN));
                    mListener.onFragmentInteraction(
                            User.hydrate(jsonResponse.getJSONObject(JSONConstants.User.USER))
                    );
                }
            } catch (JSONException e) {
                Log.e("JSON Load", e.getMessage());
                e.printStackTrace();
            }
            toggleLoader();
        }

        private void toggleLoader() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loaderLayout.toggleVisibility();
                }
            });
        }
    }

}
