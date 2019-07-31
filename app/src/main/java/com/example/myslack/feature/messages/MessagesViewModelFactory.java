package com.example.myslack.feature.messages;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class MessagesViewModelFactory implements ViewModelProvider.Factory {

    private Application mApplication;
    private ArrayList<String> userIds;

    public MessagesViewModelFactory(Application mApplication, ArrayList<String> userIds) {
        this.mApplication = mApplication;
        this.userIds = userIds;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MessagesViewModel(mApplication, userIds);
    }
}
