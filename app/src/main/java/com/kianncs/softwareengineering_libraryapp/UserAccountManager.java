package com.kianncs.softwareengineering_libraryapp;

/**
 * Created by kiann on 1/11/2016.
 */

public class UserAccountManager {

    iDatabase database = APIAndDatabaseFactory.getDatabase("firebase");


    public void registerUserAccount(final String name, final String email, final String password, final String mobileNo,final RegisterListener registerListener){
        database.createUserWithEmailandPassword(name,email,password,mobileNo,registerListener);
    }

    public void loginUserAccount(final String email, final String password, final LoginListener loginListener){
        database.Login(email, password, loginListener);
    }

    public String getCurrentUserID(){
        return database.getCurrentUserId();
    }


    public void editProfile(String name, String email, String mobileNo, String UserID){
        database.editProfile(name,email,mobileNo,UserID);
    }

    public void getUser(String userID, UserListener userListener){
        database.getUser(userID, userListener);
    }

    public void changePassword(String oPassword, String newPassword, String cfmPassword, ChangePasswordListener changePasswordListener){
        database.changePassword(oPassword, newPassword, cfmPassword, changePasswordListener);
    }

    public void resetPassword(String email, final ResetPasswordListener resetPasswordListener){
        database.resetPassword(email, resetPasswordListener);
    }

    public boolean checkIfAlreadyLogin(){
        return database.checkIfAlreadyLogin();
    }

    public void signOut(){
        database.signOut();
    }
}
