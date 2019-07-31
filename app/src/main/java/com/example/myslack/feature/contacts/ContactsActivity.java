package com.example.myslack.feature.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.myslack.R;
import com.example.myslack.feature.messages.MessagesActivity;
import com.example.myslack.feature.myprofile.MyProfileActivity;
import com.example.myslack.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import javax.annotation.Nullable;

public class ContactsActivity extends AppCompatActivity implements AdapterContact.ContactAdapterOnClickListener {

    private static final String TAG = "CONTACTS_LOG";

    //View
    private RecyclerView mRecyclerView;
    private AdapterContact mAdapterContact;
    private Toolbar mToolbar;
    private TextView textToolbarTitle;
    private ImageView imgToolbarAvatar;

    //Firebase
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        this.mRecyclerView = findViewById(R.id.recyclerView_contacts);
        this.mToolbar = findViewById(R.id.toolbarInclude);
        this.imgToolbarAvatar = findViewById(R.id.imgToolbarAvatar);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        initToolbar();

        imgToolbarAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myProfileIntent = new Intent(getBaseContext(), MyProfileActivity.class);
                startActivity(myProfileIntent);
            }
        });

        mAdapterContact = new AdapterContact(this, null, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapterContact);

        setupViewModel();
    }

    private void initToolbar() {
        //Set default action bar
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);

            RequestOptions requestOptions = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).circleCrop();

            this.textToolbarTitle = findViewById(R.id.textTitleToolbar);
            textToolbarTitle.setText(getString(R.string.contacts));

            Glide.with(this)
                    .asBitmap()
                    .load(mUser.getPhotoUrl())
                    .apply(requestOptions)
                    .into(imgToolbarAvatar);
        }
    }

    private void setupViewModel() {
        ContactsViewModel viewModel = ViewModelProviders.of(this).get(ContactsViewModel.class);
        viewModel.observerUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                Log.d(TAG, "Updating list of users from LiveData in ViewModel");
                mAdapterContact.updateItems(users);
            }
        });
    }

    @Override
    public void onMyClickListener(User user) {
        Intent messagesIntent = new Intent(this, MessagesActivity.class);
        messagesIntent.putExtra("USER", user);
        startActivity(messagesIntent);

    }
}
