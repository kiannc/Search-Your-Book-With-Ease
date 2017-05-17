package com.kianncs.softwareengineering_libraryapp.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kianncs.softwareengineering_libraryapp.Entity.Post;
import com.kianncs.softwareengineering_libraryapp.PostManager;
import com.kianncs.softwareengineering_libraryapp.R;
import com.kianncs.softwareengineering_libraryapp.Entity.User;
import com.kianncs.softwareengineering_libraryapp.UserAccountManager;
import com.kianncs.softwareengineering_libraryapp.UserListener;


import java.util.HashMap;
import java.util.Map;

public class NewPostActivity extends OfflineDealingActivity {

    private static final String TAG = "NewPostActivity";
    private static final String REQUIRED = "Required";


    private EditText mTitleField;
    private EditText mPriceField;
    private EditText mAuthorField;
    private EditText mConditionField;
    private FloatingActionButton mSubmitButton;
    private PostManager postManager;
    private UserAccountManager userAccountManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post_activty);

        mTitleField = (EditText) findViewById(R.id.field_title);
        mAuthorField = (EditText) findViewById(R.id.field_author);
        mPriceField = (EditText) findViewById(R.id.field_price);
        mConditionField = (EditText) findViewById(R.id.field_condition);
        mSubmitButton = (FloatingActionButton) findViewById(R.id.fab_submit_post);
        postManager = new PostManager();
        userAccountManager = new UserAccountManager();

        mTitleField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    mAuthorField.requestFocus();
                    handled = true;
                }
                return handled;
            }

        });

        mAuthorField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    mPriceField.requestFocus();
                    handled = true;
                }
                return handled;
            }

        });

        mPriceField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    mConditionField.requestFocus();
                    handled = true;
                }
                return handled;
            }

        });

        mConditionField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    closeKeyboard(mConditionField);
                    handled = true;
                }
                return handled;
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
    }

    private void submitPost() {
        final String title = mTitleField.getText().toString();
        final String price = mPriceField.getText().toString();
        final String author = mAuthorField.getText().toString();
        final String condition = mConditionField.getText().toString();

        if (TextUtils.isEmpty(title)) {
            mTitleField.setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(price)) {
            mPriceField.setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(author)) {
            mAuthorField.setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(condition)) {
            mConditionField.setError(REQUIRED);
            return;
        }

        setEditingEnabled(false);
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        final String userID = new UserAccountManager().getCurrentUserID();
        userAccountManager.getUser(userID, new UserListener(){
            public void onUserDataChange(User user){
                postManager.createNewPost(userID,user.getName(),title,author,price,condition);

            }
        });
        setEditingEnabled(true);
        finish();
    }

    private void setEditingEnabled(boolean enabled) {
        mTitleField.setEnabled(enabled);
        mPriceField.setEnabled(enabled);
        mAuthorField.setEnabled(enabled);
        mConditionField.setEnabled(enabled);
        if (enabled) {
            mSubmitButton.setVisibility(View.VISIBLE);
        } else {
            mSubmitButton.setVisibility(View.GONE);
        }
    }

    private void closeKeyboard(EditText et){
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

}
