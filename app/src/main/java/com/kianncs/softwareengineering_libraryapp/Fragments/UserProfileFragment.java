package com.kianncs.softwareengineering_libraryapp.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kianncs.softwareengineering_libraryapp.Activity.LoadingScreenActivity;
import com.kianncs.softwareengineering_libraryapp.Activity.LoginActivity;
import com.kianncs.softwareengineering_libraryapp.Entity.User;
import com.kianncs.softwareengineering_libraryapp.R;
import com.kianncs.softwareengineering_libraryapp.UserAccountManager;
import com.kianncs.softwareengineering_libraryapp.UserListener;

import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    EditText etFullName, etEmail, etMobileNo;
    Button bUpdate;
    boolean editTextEnabled = false;
    FirebaseAuth auth;
    DatabaseReference ref;
    UserAccountManager userAccountManager;
    ProgressBar pbFullName,pBEmail,pbMobileNo;
    String userID;
    private static final long SPLASH_TIME=4000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userAccountManager = new UserAccountManager();
        View v = inflater.inflate(R.layout.fragment_user_profile, container, false);
        etFullName = (EditText) v.findViewById(R.id.etFullNameUserProfile);
        etEmail = (EditText) v.findViewById(R.id.etEmailUserProfile);
        etMobileNo = (EditText) v.findViewById(R.id.etMobileNoUserProfile);
        pBEmail = (ProgressBar) v.findViewById(R.id.progressBarUserProfileEmail);
        pbFullName = (ProgressBar) v.findViewById(R.id.progressBarUserProfileFullName);
        pbMobileNo = (ProgressBar) v.findViewById(R.id.progressBarUserProfileMobieNo);
        bUpdate = (Button) v.findViewById(R.id.buttonUserProfile);
        disabledEnabledAllEditText(false);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        auth= FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();
        userID = userAccountManager.getCurrentUserID();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {

            }
        };
        final Timer timer = new Timer();
        timer.schedule(task, SPLASH_TIME);

        userAccountManager.getUser(userID, new UserListener() {
            @Override
            public void onUserDataChange(User user) {
                etFullName.setText(user.getName());
                etEmail.setText(user.getEmail());
                etMobileNo.setText(user.getMobileNo());
                pbFullName.setVisibility(View.GONE);
                pBEmail.setVisibility(View.GONE);
                pbMobileNo.setVisibility(View.GONE);
                bUpdate.setVisibility(View.VISIBLE);
            }
        });




        bUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = etFullName.getText().toString();
                final String email = etEmail.getText().toString();
                final String mobileNo = etMobileNo.getText().toString();
                if(name.isEmpty() || email.isEmpty() || mobileNo.isEmpty()){
                    validateForm();
                }else{
                    if(editTextEnabled ){
                        editTextEnabled = false;
                        disabledEnabledAllEditText(editTextEnabled);
                        userAccountManager.editProfile(name,email,mobileNo,userID);
                        Toast.makeText(getActivity(), "All Fields disabled !", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "Profile Updated !", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        editTextEnabled = true;
                        disabledEnabledAllEditText(editTextEnabled);
                        Toast.makeText(getActivity(), "All Fields enabled !", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        return v;
    }


    private boolean validateForm(){
        boolean result = true;
        if (TextUtils.isEmpty(etFullName.getText().toString())) {
            etFullName.setError("Required");
            result = false;
        } else {
            etFullName.setError(null);
        }

        if (TextUtils.isEmpty(etEmail.getText().toString())) {
            etEmail.setError("Required");
            result = false;
        } else {
            etEmail.setError(null);
        }

        if (TextUtils.isEmpty(etMobileNo.getText().toString())) {
            etMobileNo.setError("Required");
            result = false;
        } else {
            etMobileNo.setError(null);
        }

        return result;
    }



    public void disabledEnabledAllEditText(boolean i){
        etFullName.setEnabled(i);
        etEmail.setEnabled(i);
        etMobileNo.setEnabled(i);
    }



}
