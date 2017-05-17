package com.kianncs.softwareengineering_libraryapp.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kianncs.softwareengineering_libraryapp.ChangePasswordListener;
import com.kianncs.softwareengineering_libraryapp.R;
import com.kianncs.softwareengineering_libraryapp.UserAccountManager;



/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragments extends Fragment {

    EditText etOldPassword, etNewPassword, etCfmNewPassword;
    Button  bChange;
    FirebaseAuth auth;
    DatabaseReference ref;
    UserAccountManager userAccountManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment\
        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();
        userAccountManager = new UserAccountManager();
        View v = inflater.inflate(R.layout.fragment_change_password,container,false);
        etOldPassword = (EditText) v.findViewById(R.id.etOldPasswordChangePassword);
        etNewPassword = (EditText) v.findViewById(R.id.etNewPasswordChangePassword);
        etCfmNewPassword = (EditText) v.findViewById(R.id.etcfmNewPasswordChangePassword);
        bChange = (Button) v.findViewById(R.id.bChangeNewPassword);

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        etOldPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    etNewPassword.requestFocus();
                    handled = true;
                }
                return handled;
            }

        });

        etNewPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    etCfmNewPassword.requestFocus();
                    handled = true;
                }
                return handled;
            }

        });

        etCfmNewPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    closeKeyboard(etCfmNewPassword);
                    handled = true;
                }
                return handled;
            }

        });

        bChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String oPassword = etOldPassword.getText().toString();
                final String Password = etNewPassword.getText().toString();
                final String cfmPassword = etCfmNewPassword.getText().toString();
                if(oPassword.isEmpty() || Password.isEmpty() || cfmPassword.isEmpty()){
                    validateForm();
                }else{
                    userAccountManager.changePassword(oPassword, Password, cfmPassword, new ChangePasswordListener() {
                        @Override
                        public void onChangePassword(boolean complete) {
                            if(complete){
                                Toast.makeText(getActivity(),"Password successfully change", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getActivity(), "Old Password Wrong/Cfm Password Wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        return v;
    }

    private void closeKeyboard(EditText et){
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    private boolean validateForm(){
        boolean result = true;
        if (TextUtils.isEmpty(etOldPassword.getText().toString())) {
            etOldPassword.setError("Required");
            result = false;
        } else {
            etOldPassword.setError(null);
        }

        if (TextUtils.isEmpty(etNewPassword.getText().toString())) {
            etNewPassword.setError("Required");
            result = false;
        } else {
            etNewPassword.setError(null);
        }

        if (TextUtils.isEmpty(etCfmNewPassword.getText().toString())) {
            etCfmNewPassword.setError("Required");
            result = false;
        } else {
            etCfmNewPassword.setError(null);
        }

        return result;
    }

}



