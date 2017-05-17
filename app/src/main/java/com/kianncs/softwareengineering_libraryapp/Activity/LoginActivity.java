package com.kianncs.softwareengineering_libraryapp.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kianncs.softwareengineering_libraryapp.LoginListener;
import com.kianncs.softwareengineering_libraryapp.R;
import com.kianncs.softwareengineering_libraryapp.UserAccountManager;

import static com.kianncs.softwareengineering_libraryapp.R.id.progressBar;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    EditText etUsername;
    EditText etPassword;
    Button bLogin;
    TextView tvRegLink, tvForgetPassword;
    FirebaseAuth auth;
    DatabaseReference ref;
    String getPassword;
    FirebaseAuth.AuthStateListener authStateListener;
    private static final String TAG = "CustomAuthActivity";
    private String mCustomToken;
    UserAccountManager userAccountManager;
    private ProgressDialog progressBar;

    public String userName = "", passWord = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();
        userAccountManager = new UserAccountManager();


        // Set up the login form.

        etUsername = (EditText) findViewById(R.id.etEmailLogin);
        etPassword = (EditText) findViewById(R.id.etpassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        tvRegLink = (TextView) findViewById(R.id.tvRegisterLink);
        tvForgetPassword = (TextView) findViewById(R.id.tvForgetPassword);
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Logging In ");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);

        if(userAccountManager.checkIfAlreadyLogin()) {
            startActivity(new Intent(LoginActivity.this, UserAreaActivity.class));
            finish();
        }

        bLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                if(etUsername.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()){
                    validateForm();
                }else{
                    progressBar.show();
                    userAccountManager.loginUserAccount(email, password, new LoginListener() {
                        @Override
                        public void onLogin(boolean complete) {
                            if(complete) {
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, UserAreaActivity.class));
                                progressBar.dismiss();
                                finish();
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Invalid Email/Password", Toast.LENGTH_SHORT).show();
                                progressBar.dismiss();
                            }
                        }
                    });
                }


            }
        });




        tvRegLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        tvForgetPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            }
        });



        etUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (etUsername.getText().toString().isEmpty()) {
                        validateForm();
                    } else {
                       userName = etUsername.getText().toString();
                    }
                    //close keyboard and
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(etUsername.getWindowToken(), 0);
                    handled = true;
                }

                return handled;
            }

        });

        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (etPassword.getText().toString().isEmpty()) {
                        validateForm();
                    } else {
                        passWord = etPassword.getText().toString();
                    }
                    //close keyboard and
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);
                    handled = true;
                }

                return handled;
            }

        });


    }

    private boolean validateForm(){
        boolean result = true;
        if (TextUtils.isEmpty(etUsername.getText().toString())) {
            etUsername.setError("Required");
            result = false;
        } else {
            etUsername.setError(null);
        }

        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            etPassword.setError("Required");
            result = false;
        } else {
            etPassword.setError(null);
        }

        return result;
    }

}


