package com.fiuba.taller2.jobify.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fiuba.taller2.jobify.User;
import com.taller2.fiuba.jobify.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistrationCompleteFragment.OnRegistrationCompleteListener} interface
 * to handle interaction events.
 * Use the {@link RegistrationCompleteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationCompleteFragment extends Fragment {

    private static final String CONGRATULATIONS_FORMAT =
            "Congratulations %s!\n" +
                    "You are now part of our community\n" +
                    "Connect with your contacts now!";
    private User newUser;
    private TextView congratsText;
    private Button startBtn;
    private OnRegistrationCompleteListener mListener;


    public static RegistrationCompleteFragment newInstance() {
        return new RegistrationCompleteFragment();
    }

    public RegistrationCompleteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_registration_complete, container, false);
        congratsText = (TextView) rootView.findViewById(R.id.congrats_text);
        startBtn = (Button) rootView.findViewById(R.id.start_btn);
        startBtn.setOnClickListener(new OnStartClickListener());
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnRegistrationCompleteListener) activity;
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
        updateViews();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that activity.
     */
    public interface OnRegistrationCompleteListener {
        public void onRegistrationComplete();
    }


    /************************************* PRIVATE STUFF ******************************************/

    private void updateViews() {
        String congratsMsg = String.format(CONGRATULATIONS_FORMAT, newUser.getFirstName());
        congratsText.setText(congratsMsg);
    }

    private class OnStartClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            mListener.onRegistrationComplete();
        }
    }

}
