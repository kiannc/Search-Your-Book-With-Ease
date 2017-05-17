package com.kianncs.softwareengineering_libraryapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.kianncs.softwareengineering_libraryapp.R;
import com.kianncs.softwareengineering_libraryapp.ResetPasswordListener;
import com.kianncs.softwareengineering_libraryapp.UserAccountManager;

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText resetPassEmail;
    Button bReset;
    FirebaseAuth auth;
    UserAccountManager userAccountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        auth = FirebaseAuth.getInstance();
        userAccountManager = new UserAccountManager();

        resetPassEmail = (EditText) findViewById(R.id.etEmailForgetPassword);
        bReset = (Button) findViewById(R.id.bChangePassword);

        bReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = resetPassEmail.getText().toString();
                userAccountManager.resetPassword(email, new ResetPasswordListener() {
                    @Override
                    public void onResetPass(boolean complete) {
                        if(complete){
                            Toast.makeText(ForgetPasswordActivity.this, "Reset Password Email has been sent", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                        }
                        else{
                            Toast.makeText(ForgetPasswordActivity.this,"Invalid Email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
