package com.example.virtualbookshelf.viewmodel;

import android.app.Application;
import android.database.Cursor;

import androidx.lifecycle.MutableLiveData;

import com.example.virtualbookshelf.model.User;

public class UserViewModel extends BaseViewModel {

    private final MutableLiveData<User> userLiveData;

    public UserViewModel(Application application) {
        super(application);
        this.userLiveData = new MutableLiveData<>();
        loadUserData();
    }

    private void loadUserData() {
        Cursor cursor_user = dbManager.getUserById(1);

        User user = null;
        if (cursor_user.moveToFirst()) {
            user = new User(cursor_user.getInt(0), cursor_user.getString(1), cursor_user.getBlob(2), cursor_user.getInt(3), cursor_user.getInt(4), cursor_user.getInt(5), cursor_user.getInt(6), cursor_user.getInt(7));
        }
        cursor_user.close();
        userLiveData.setValue(user);
    }

    public MutableLiveData<User> getUser() {
        return userLiveData;
    }

    public void refreshUserData() {
        loadUserData();
    }
}