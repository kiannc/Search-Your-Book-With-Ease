package com.kianncs.softwareengineering_libraryapp;


import com.kianncs.softwareengineering_libraryapp.Entity.Comment;
import com.kianncs.softwareengineering_libraryapp.Entity.CommentAdapter;
import com.kianncs.softwareengineering_libraryapp.Entity.Post;

/**
 * Created by Yq on 30/10/2016.
 */

public interface iDatabase {
    public void createNewPost(Post post);
    public void removePost(String postKey);
    public void getPostDetail(String postKey, PostListener postListener);
    public void getComments(String postKey, CommentsListener commentsListener, CommentAdapter commentAdapter);
    public void createNewComment(String postKey, Comment comment);
    public String getCurrentUserId();
    public void getUser(String userID, final UserListener userListener);
    public void createUserWithEmailandPassword(final String name, final String email, final String password, final String mobileNo,final RegisterListener registerListener);
    public void Login(final String email, final String password, final LoginListener loginListener);
    public void editProfile(String name, String email, String mobileNo, String UserID);
    public void changePassword(String oPassword, String newPassword, String cfmPassword, ChangePasswordListener changePasswordListener);
    public void resetPassword(String email, final ResetPasswordListener resetPasswordListener);
    public boolean checkIfAlreadyLogin();
    public void signOut();
    public void createNewChatRoom(final String userID,final String userName, ChatRoomListener chatRoomListener);
    public void createNewMessage(String roomKey, Comment comment);
    public void getMessages(String roomKey, final CommentsListener commentsListener, final CommentAdapter commentAdapter);
}
