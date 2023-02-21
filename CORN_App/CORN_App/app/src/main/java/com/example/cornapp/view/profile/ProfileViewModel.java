package com.example.cornapp.view.profile;

import android.text.Editable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<UserBo> user;

    public void updateUser(Editable text, Editable text1, Editable text2, Editable text3) {

    }
    public LiveData<UserBo> getUser(){
        return user;
    }

}