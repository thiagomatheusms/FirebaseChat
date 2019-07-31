package com.example.myslack.repository;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myslack.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

public class UsersRepository {

    private MutableLiveData<List<User>> items;

    public LiveData<List<User>> getUsers() {
        if (items == null) {
            items = new MutableLiveData<List<User>>();
            users();
        }

        return items;
    }

    private void users() {

        final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance().collection("users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<User> users = queryDocumentSnapshots.toObjects(User.class);
                            users.removeIf(new Predicate<User>() {
                                @Override
                                public boolean test(User user) {
                                    return user.getIdUser() == currentUserId;
                                }
                            });
                            items.setValue(users);
                        }
                    }
                });

    }
}
