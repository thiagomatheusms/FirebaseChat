package com.example.myslack.feature.messages;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myslack.model.Channel;
import com.example.myslack.model.Message;
import com.example.myslack.repository.MessagesRepository;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

public class MessagesViewModel extends ViewModel {

    private LiveData<List<Message>> messageData;
    private MessagesRepository messagesRepository;

    public MessagesViewModel(Application application, ArrayList<String> userIds) {
        messagesRepository = new MessagesRepository();
        messageData = messagesRepository.getMessages(userIds);
    }


    public LiveData<List<Message>> observerMessages(){
        return messageData;
    }

}
