package com.zybooks.inventoryapp;

import android.content.Context;

public class UserDAO {
    private InventoryAppDatabase mDb;
    public UserDAO(){}
    public UserDAO(Context context){
        mDb = new InventoryAppDatabase(context);
    }

    public boolean authenticateUser(String userId, String userPass){
        return mDb.authenticateUser(userId, userPass);
    }

    public User getUser(String userId){
        return mDb.getUser(userId);
    }

    public boolean registerUser(User user){
        return mDb.createUser(user);
    }

    public static class User{

        private String mUserId, mUserPass, mFirstName, mLastName, mUserEmail;

        public User(){}
        public User(String userId, String userPass, String firstName, String lastName, String userEmail){
            mUserId = userId;
            mUserPass = userPass;
            mFirstName = firstName;
            mLastName = lastName;
            mUserEmail = userEmail;
        }

        public String getmUserId() {
            return mUserId;
        }

        public void setmUserId(String mUserId) {
            this.mUserId = mUserId;
        }

        public String getmUserPass() {
            return mUserPass;
        }

        public void setmUserPass(String mUserPass) {
            this.mUserPass = mUserPass;
        }

        public String getmFirstName() {
            return mFirstName;
        }

        public void setmFirstName(String mFirstName) {
            this.mFirstName = mFirstName;
        }

        public String getmLastName() {
            return mLastName;
        }

        public void setmLastName(String mLastName) {
            this.mLastName = mLastName;
        }

        public String getmUserEmail() {
            return mUserEmail;
        }

        public void setmUserEmail(String mUserEmail) {
            this.mUserEmail = mUserEmail;
        }
    }

}
