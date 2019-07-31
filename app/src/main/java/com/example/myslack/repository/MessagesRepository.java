package com.example.myslack.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myslack.model.Channel;
import com.example.myslack.model.Message;
import com.example.myslack.model.User;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

public class MessagesRepository {

    private MutableLiveData<List<Message>> items;
    private String documentIdChannel;

    public LiveData<List<Message>> getMessages(ArrayList<String> userIds) {
        if (items == null) {
            items = new MutableLiveData<List<Message>>();
            getChannelForMessages(userIds);
        }

        return items;
    }


    public void insertMessages(final Channel channel, final Message message) {
        Log.d("USUARIO LOGADO", FirebaseAuth.getInstance().getCurrentUser().getUid());

        FirebaseFirestore.getInstance().collection("channels")
                .whereArrayContains("userIds", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<Channel> channels = queryDocumentSnapshots.toObjects(Channel.class);

                            Boolean hasChannel = false;

                            for (int i = 0; i < channels.size(); i++) {
                                for (int j = 0; j < 2; j++) {
                                    if (channels.get(i).getUsers().get(j).getIdUser().equals(channel.getUsers().get(1).getIdUser())) {
                                        documentIdChannel = queryDocumentSnapshots.getDocuments().get(i).getId();
                                        hasChannel = true;
                                    }
                                }
                            }

                            if (hasChannel == true) {
                                insert(channel, message, documentIdChannel);
                            } else if (hasChannel == false) {
                                firstInsert(channel, message);
                            }
                        }
                    }
                });
    }


    public void firstInsert(Channel channel, Message message) {
        DocumentReference setChannel = FirebaseFirestore.getInstance().collection("channels").
                document();

        setChannel.set(channel);
        setChannel.collection("messages").add(message);

    }

    private void insert(Channel channel, Message message, String documentIdChannel) {
        FirebaseFirestore.getInstance().collection("channels").document(documentIdChannel).collection("messages").document().set(message);
    }


    private void getChannelForMessages(final ArrayList<String> userIds) {
        FirebaseFirestore.getInstance().collection("channels")
                .whereArrayContains("userIds", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<Channel> channels = queryDocumentSnapshots.toObjects(Channel.class);

                            for (int i = 0; i < channels.size(); i++) {
                                for (int j = 0; j < 2; j++) {
                                    if (channels.get(i).getUserIds().get(j).equals(userIds.get(1))) {
                                        documentIdChannel = queryDocumentSnapshots.getDocuments().get(i).getId();
                                        messages(documentIdChannel);
                                    }
                                }
                            }
                        }
                    }
                });
    }

    //Após obter o Channel correto da conversa dos id's do array<string>, captura as mensagens.
    private void messages(String documentIdChannel) {
        FirebaseFirestore.getInstance().collection("channels")
                .document(documentIdChannel)
                .collection("messages")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<Message> messages = queryDocumentSnapshots.toObjects(Message.class);
                            items.setValue(messages);
                        }
                    }
                });
    }

}