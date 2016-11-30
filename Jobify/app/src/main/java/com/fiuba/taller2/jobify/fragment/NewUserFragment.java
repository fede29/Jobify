package com.fiuba.taller2.jobify.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.utils.AppServerRequest;
import com.fiuba.taller2.jobify.utils.HttpCallback;
import com.taller2.fiuba.jobify.R;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.fiuba.taller2.jobify.fragment.NewUserFragment.OnUserEditListener} interface
 * to handle interaction events.
 * Use the {@link NewUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewUserFragment extends Fragment {

    private OnUserEditListener mListener;
    private User newUser;
    private EditText firstName, lastName;


    public static NewUserFragment newInstance() {
        return new NewUserFragment();
    }

    public NewUserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_new_user, container, false);

        firstName = (EditText) rootView.findViewById(R.id.first_name_entry);
        lastName = (EditText) rootView.findViewById(R.id.last_name_entry);
        Button nextBtn = (Button) rootView.findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(new OnNextClickListener());

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnUserEditListener) activity;
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

    public void useUser(User u) {
        newUser = u;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that activity.
     */
    public interface OnUserEditListener {
        void onUserEdit(User user);
        void toggleLoader();
    }


    /*********************************** PRIVATE STUFF ********************************************/

    private class OnNextClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String fname = firstName.getText().toString();
            String lname = lastName.getText().toString();
            if (fname.isEmpty() || lname.isEmpty()) {
                Toast.makeText(getActivity(), "Fill the fields", Toast.LENGTH_LONG).show();
            } else {
                newUser.setFirstName(firstName.getText().toString());
                newUser.setLastName(lastName.getText().toString());
                AppServerRequest.updateUser(newUser, new EditUserCallback());
                mListener.toggleLoader();
            }
        }
    }

    private class EditUserCallback extends HttpCallback {
        @Override
        public void onResponse() {
            try {
                if (statusIs(200)) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListener.onUserEdit(newUser);
                        }
                    });
                } else {
                    showLongToast(getErrorMessage());
                    Log.w(
                            String.format("Update user, code = %d", getStatusCode()),
                            getErrorMessage()
                    );
                }
            } catch (JSONException e) {
                Log.e("User edit registration", e.getMessage());
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mListener.toggleLoader();
                }
            });
        }
    }

}
