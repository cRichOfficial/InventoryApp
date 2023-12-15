package com.zybooks.inventoryapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {
    private MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();
    private MutableLiveData<UserDAO.User> user = new MutableLiveData<>();

    public UserViewModel(){

    }

    public LiveData<Boolean> getUserLoginStatus(){
        return isLoggedIn;
    }
    public LiveData<UserDAO.User> getUser(){return user;}

    public void setUserLoginStatus(boolean blnLoggedIn){
        isLoggedIn.setValue(blnLoggedIn);
    }
    public void setUser(UserDAO.User userObject){
        user.setValue(userObject);
    }
}
