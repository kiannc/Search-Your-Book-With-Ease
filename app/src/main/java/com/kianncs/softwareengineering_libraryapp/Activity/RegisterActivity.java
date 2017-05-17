package com.kianncs.softwareengineering_libraryapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kianncs.softwareengineering_libraryapp.R;
import com.kianncs.softwareengineering_libraryapp.RegisterListener;
import com.kianncs.softwareengineering_libraryapp.UserAccountManager;

public class RegisterActivity extends AppCompatActivity {

    Button bRegister;
    EditText etName, etEmail, etPassword, etMobileNo;
    FirebaseAuth auth;
    DatabaseReference ref;
    UserAccountManager userAccountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();
        userAccountManager = new UserAccountManager();

        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmailRegister);
        etPassword = (EditText) findViewById(R.id.etPasswordRegister);
        etMobileNo = (EditText) findViewById(R.id.etMobileNoRegister);
        bRegister = (Button) findViewById(R.id.bReg);

        etName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    etEmail.requestFocus();
                    handled = true;
                }
                return handled;
            }
        });

        etEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    etPassword.requestFocus();
                    handled = true;
                }
                return handled;
            }
        });

        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    etMobileNo.requestFocus();
                    handled = true;
                }
                return handled;
            }
        });

        etMobileNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(etMobileNo.getWindowToken(), 0);
                    handled = true;
                }
                return handled;
            }
        });


        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = etName.getText().toString();
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();
                final String mobileNo = etMobileNo.getText().toString();

                if(name.isEmpty() || email.isEmpty() || password.isEmpty() || mobileNo.isEmpty()){
                    validateForm();
                }
                else{
                    userAccountManager.registerUserAccount(name, email, password, mobileNo, new RegisterListener() {
                        @Override
                        public void onCreateUser(boolean complete) {
                            if(complete) {
                                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(i);
                                Toast.makeText(RegisterActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this, "Invalid Email Format", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean validateForm(){
        boolean result = true;
        if (TextUtils.isEmpty(etName.getText().toString())) {
            etName.setError("Required");
            result = false;
        } else {
            etName.setError(null);
        }

        if (TextUtils.isEmpty(etEmail.getText().toString())) {
            etEmail.setError("Required");
            result = false;
        } else {
            etEmail.setError(null);
        }

        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            etPassword.setError("Required");
            result = false;
        } else {
            etPassword.setError(null);
        }

        if (TextUtils.isEmpty(etMobileNo.getText().toString())) {
            etMobileNo.setError("Required");
            result = false;
        } else {
            etMobileNo.setError(null);
        }
        return result;
    }

}
