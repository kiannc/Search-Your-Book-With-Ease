package com.kianncs.softwareengineering_libraryapp;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kianncs.softwareengineering_libraryapp.Entity.ChatRoom;
import com.kianncs.softwareengineering_libraryapp.Entity.Comment;
import com.kianncs.softwareengineering_libraryapp.Entity.CommentAdapter;
import com.kianncs.softwareengineering_libraryapp.Entity.Post;
import com.kianncs.softwareengineering_libraryapp.Entity.User;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yq on 30/10/2016.
 */

public class FirebaseDBImp implements iDatabase{
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference();
    FirebaseAuth auth = FirebaseAuth.getInstance();


    public void createNewPost(Post post){
        String key = dbRef.child("Post").push().getKey();
        Map<String, Object> postUpdates = new HashMap<>();
        postUpdates.put("/Posts/"+key,post);
        postUpdates.put("/User-Posts/"+post.getUserID()+"/"+key,post);
        dbRef.updateChildren(postUpdates);
    }

    public void removePost(String postID){
        dbRef.child("Posts").child(postID).removeValue();
        dbRef.child("User-Posts").child(getCurrentUserId()).child(postID).removeValue();
        dbRef.child("Post-Comments").child(postID).removeValue();
    }

    public void getPostDetail(String postKey, final PostListener postListener){
        DatabaseReference postDetailRef = dbRef.child("Posts").child(postKey);
        ValueEventListener postDetailListener = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                postListener.onPostDataChange(post);
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        };
        postDetailRef.addListenerForSingleValueEvent(postDetailListener);
    }

    public void getComments(String postKey, final CommentsListener commentsListener, final CommentAdapter commentAdapter){
        DatabaseReference commentsRef = dbRef.child("Post-Comments").child(postKey);
        final ArrayList<String> mCommentIds = new ArrayList<>();
        final ArrayList<Comment> mComments = new ArrayList<>();

        ChildEventListener commentsDataListener = new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                mCommentIds.add(dataSnapshot.getKey());
                mComments.add(comment);
                commentAdapter.notifyItemInserted(mComments.size() - 1);
                commentsListener.onCommentDataChange(mCommentIds,mComments);
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Comment newComment = dataSnapshot.getValue(Comment.class);
                String commentKey = dataSnapshot.getKey();

                int commentIndex = mCommentIds.indexOf(commentKey);
                if (commentIndex > -1) {
                    mComments.set(commentIndex, newComment);
                    commentAdapter.notifyItemChanged(commentIndex);
                    commentsListener.onCommentDataChange(mCommentIds,mComments);
                }
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String commentKey = dataSnapshot.getKey();

                int commentIndex = mCommentIds.indexOf(commentKey);
                if (commentIndex > -1) {
                    mCommentIds.remove(commentIndex);
                    mComments.remove(commentIndex);
                    commentAdapter.notifyItemRemoved(commentIndex);
                    commentsListener.onCommentDataChange(mCommentIds,mComments);
                }
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        };
        commentsRef.addChildEventListener(commentsDataListener);
    }

    public void createNewComment(String postKey, Comment comment){
        dbRef.child("Post-Comments").child(postKey).push().setValue(comment);
    }

    public void getUser(String userID, final UserListener userListener){
        ValueEventListener userDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userListener.onUserDataChange(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        dbRef.child("Users").child(userID).addValueEventListener(userDataListener);
    }

    public void createNewUser(String userId, String name, String email, String mobileNo){
        User acc = new User(name, email, mobileNo);
        HashMap<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Users/" + userId + "/" , acc);
        dbRef.updateChildren(childUpdates);
    }


    public void createUserWithEmailandPassword(final String name, final String email, final String password, final String mobileNo,final RegisterListener registerListener){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    createNewUser(task.getResult().getUser().getUid(), name, email, mobileNo);
                    registerListener.onCreateUser(true);
                    auth.signOut();
                }
                else{
                    registerListener.onCreateUser(false);
                }
            }
        });
    }

    public void Login(final String email, final String password, final LoginListener loginListener){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    loginListener.onLogin(true);
                }
                else{
                    loginListener.onLogin(false);
                }
            }
        });
    }

    public void editProfile(String name, String email, String mobileNo, String UserID){
        dbRef.child("Users").child(UserID).child("name").setValue(name);
        dbRef.child("Users").child(UserID).child("email").setValue(email);
        auth.getCurrentUser().updateEmail(email);
        dbRef.child("Users").child(UserID).child("mobileNo").setValue(mobileNo);
    }

    public void changePassword( String oPassword, final String newPassword, final String cfmPassword, final ChangePasswordListener changePasswordListener) {
        AuthCredential credential = EmailAuthProvider.getCredential(auth.getCurrentUser().getEmail(), oPassword);
        auth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    if(newPassword.equals(cfmPassword)){
                        auth.getCurrentUser().updatePassword(newPassword);
                        changePasswordListener.onChangePassword(true);
                    }
                    else{
                        changePasswordListener.onChangePassword(false);
                    }
                }
                else{
                    changePasswordListener.onChangePassword(false);
                }
            }
        });
    }

    public void resetPassword(String email, final ResetPasswordListener resetPasswordListener){
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    resetPasswordListener.onResetPass(true);
                }
                else{
                    resetPasswordListener.onResetPass(false);
                }
            }
        });

    }

    public String getCurrentUserId(){return auth.getCurrentUser().getUid();}

    public boolean checkIfAlreadyLogin(){
        if(auth.getCurrentUser()!=null)
            return true;
        return false;
    }

    public void signOut(){
        auth.signOut();
    }

    public void getMessages(String roomKey, final CommentsListener commentsListener, final CommentAdapter commentAdapter){
        DatabaseReference commentsRef = dbRef.child("Chatroom-Message").child(roomKey);
        final ArrayList<String> mCommentIds = new ArrayList<>();
        final ArrayList<Comment> mComments = new ArrayList<>();

        ChildEventListener commentsDataListener = new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                mCommentIds.add(dataSnapshot.getKey());
                mComments.add(comment);
                commentAdapter.notifyItemInserted(mComments.size() - 1);
                commentsListener.onCommentDataChange(mCommentIds,mComments);
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Comment newComment = dataSnapshot.getValue(Comment.class);
                String commentKey = dataSnapshot.getKey();

                int commentIndex = mCommentIds.indexOf(commentKey);
                if (commentIndex > -1) {
                    mComments.set(commentIndex, newComment);
                    commentAdapter.notifyItemChanged(commentIndex);
                    commentsListener.onCommentDataChange(mCommentIds,mComments);
                }
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String commentKey = dataSnapshot.getKey();

                int commentIndex = mCommentIds.indexOf(commentKey);
                if (commentIndex > -1) {
                    mCommentIds.remove(commentIndex);
                    mComments.remove(commentIndex);
                    commentAdapter.notifyItemRemoved(commentIndex);
                    commentsListener.onCommentDataChange(mCommentIds,mComments);
                }
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        };
        commentsRef.addChildEventListener(commentsDataListener);
    }

    public void createNewMessage(final String roomKey, final Comment comment){
        dbRef.child("Chatroom-Message").child(roomKey).push().setValue(comment);
        ValueEventListener addNewMessage = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ChatRoom chatRoom = dataSnapshot.getValue(ChatRoom.class);
                ChatRoom newChatRoom = new ChatRoom(getCurrentUserId(),comment.getUserName());
                dbRef.child("User-Chatrooms").child(getCurrentUserId()).child(roomKey).setValue(chatRoom);
                dbRef.child("User-Chatrooms").child(chatRoom.getUserID()).child(roomKey).setValue(newChatRoom);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        dbRef.child("User-Chatrooms").child(getCurrentUserId()).child(roomKey).addListenerForSingleValueEvent(addNewMessage);
    }

    public void createNewChatRoom(final String userID, final String userName, final ChatRoomListener chatRoomListener)
    {
        ChatRoom newChatRoom = new ChatRoom(userID, userName);
        String roomKey = dbRef.child("chatRoom").push().getKey();
        dbRef.child("User-Chatrooms").child(getCurrentUserId()).child(roomKey).setValue(newChatRoom);
        chatRoomListener.onChatRoomDataChange(roomKey, userName);
    }
}
