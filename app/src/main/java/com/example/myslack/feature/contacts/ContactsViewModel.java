package com.example.myslack.feature.contacts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myslack.model.User;
import com.example.myslack.repository.UsersRepository;

import java.util.List;

public class ContactsViewModel extends ViewModel {

    private LiveData<List<User>> usersData;
    private UsersRepository usersRepository;

    public ContactsViewModel() {
        usersRepository = new UsersRepository();
        usersData = usersRepository.getUsers();
    }

    public LiveData<List<User>> observerUsers(){
        return usersData;
    }
}
