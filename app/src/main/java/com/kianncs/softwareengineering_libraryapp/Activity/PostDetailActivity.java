package com.kianncs.softwareengineering_libraryapp.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kianncs.softwareengineering_libraryapp.ChatRoomListener;
import com.kianncs.softwareengineering_libraryapp.ChatRoomManager;
import com.kianncs.softwareengineering_libraryapp.Entity.CommentAdapter;
import com.kianncs.softwareengineering_libraryapp.Entity.Post;
import com.kianncs.softwareengineering_libraryapp.PostListener;
import com.kianncs.softwareengineering_libraryapp.PostManager;
import com.kianncs.softwareengineering_libraryapp.R;
import com.kianncs.softwareengineering_libraryapp.Entity.User;
import com.kianncs.softwareengineering_libraryapp.UserAccountManager;
import com.kianncs.softwareengineering_libraryapp.UserListener;

public class PostDetailActivity extends OfflineDealingActivity implements View.OnClickListener {

    private static final String TAG = "PostDetailActivity";

    public static final String EXTRA_POST_KEY = "post_key";
    public static final String EXTRA_USERID ="userID";

    private String mPostKey;
    private CommentAdapter mAdapter;
    private TextView mUserNameView;
    private TextView mTitleView;
    private TextView mAuthorView;
    private TextView mPriceView;
    private TextView mConditionView;
    private EditText mCommentField;
    private Button mCommentButton;
    private RecyclerView mCommentsRecycler;
    private Button bDelete;
    private Button chat;

    private PostManager postManager;
    private UserAccountManager accountManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        mUserNameView = (TextView) findViewById(R.id.post_username);
        mTitleView = (TextView) findViewById(R.id.book_title);
        mAuthorView = (TextView) findViewById(R.id.book_author);
        mPriceView = (TextView) findViewById(R.id.book_price);
        mConditionView = (TextView) findViewById(R.id.book_condition);
        mCommentField = (EditText) findViewById(R.id.field_comment_text);
        mCommentButton = (Button) findViewById(R.id.button_post_comment);
        mCommentsRecycler = (RecyclerView) findViewById(R.id.recycler_comments);
        bDelete = (Button) findViewById(R.id.bDelete);
        chat = (Button) findViewById(R.id.chatRoom);
        mCommentButton.setOnClickListener(this);
        mCommentsRecycler.setLayoutManager(new LinearLayoutManager(this));

        postManager = new PostManager();
        accountManager = new UserAccountManager();
        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePost();
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                postManager.getPostDetail(mPostKey, new PostListener() {
                    @Override
                    public void onPostDataChange(Post post) {
                        new ChatRoomManager().createNewChatRoom(post.getUserID(), post.getUserName(), new ChatRoomListener() {
                            @Override
                            public void onChatRoomDataChange(String roomKey, String userName) {
                                Intent intent = new Intent(PostDetailActivity.this,ChatMessage.class);
                                intent.putExtra("room_name", roomKey);
                                intent.putExtra("user_name", userName);
                                startActivity(intent);
                                finish();
                            }
                        });

                    }
                });
            }
        });
    }

    public void onStart() {
        super.onStart();



        final UserListener userNameListener = new UserListener() {
            public void onUserDataChange(User user) {
                mUserNameView.setText(user.getName());
            }
        };

        postManager.getPostDetail(mPostKey, new PostListener() {
            @Override
            public void onPostDataChange(Post post) {
                mTitleView.setText(post.getBookTitle());
                mAuthorView.setText(post.getBookAuthor());
                mPriceView.setText(post.getBookPrice());
                mConditionView.setText(post.getBookCondition());
                accountManager.getUser(post.getUserID(),userNameListener);
                if(post.getUserID().equals(accountManager.getCurrentUserID())){
                    bDelete.setVisibility(View.VISIBLE);
                }
                else{
                    chat.setVisibility(View.VISIBLE);
                }

            }
        });

        mAdapter = new CommentAdapter(this, mPostKey,false);
        mCommentsRecycler.setAdapter(mAdapter);
    }

    public void onStop() {
        super.onStop();
        mAdapter.cleanupListener(mPostKey);
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_post_comment) {
            postComment();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(mCommentField.getWindowToken(), 0);
        }
    }

    private void postComment() {
        final String userID = accountManager.getCurrentUserID();
        if (!TextUtils.isEmpty(mCommentField.getText().toString()))
            accountManager.getUser(userID, new UserListener() {
                @Override
                public void onUserDataChange(User user) {
                    String commentText = mCommentField.getText().toString();
                    postManager.createNewComment(mPostKey, userID, user.getName(), commentText);
                    mCommentField.setText(null);
                }
            });
    }

    public void deletePost() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PostDetailActivity.this);
        builder1.setMessage("Are you sure you want to delete this Post?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        postManager.removePost(mPostKey);
                        finish();

                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
