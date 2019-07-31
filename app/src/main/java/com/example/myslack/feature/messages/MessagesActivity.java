package com.example.myslack.feature.messages;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myslack.R;
import com.example.myslack.model.Channel;
import com.example.myslack.model.Message;
import com.example.myslack.model.User;
import com.example.myslack.repository.MessagesRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessagesActivity extends AppCompatActivity {

    //View
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private ImageView mImageToolbar;
    private TextView mTextToolbar;
    private ImageView mButtonSend;
    private EditText mEditTextMessage;

    //Adapter
    private AdapterMessages mAdapterMessages;

    //Repository
    private MessagesRepository mMessagesRepository;

    //Firebase
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    //Array
    private ArrayList<String> userIds;

    //UserRecipient
    private User userRecipient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        //Initialize Views
        this.mRecyclerView = findViewById(R.id.recyclerViewMessages);
        this.mToolbar = findViewById(R.id.toolbarChat);
        this.mImageToolbar = findViewById(R.id.imgToolbarChat);
        this.mTextToolbar = findViewById(R.id.textToolbarchat);
        this.mButtonSend = findViewById(R.id.btnEnviarMessagem);
        this.mEditTextMessage = findViewById(R.id.editTextMessage);


        //Toolbar
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle("");
        }
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //intent receiver
        userRecipient = new User();
        Intent intent = getIntent();

        if (intent != null) {
            if (intent.hasExtra("USER"))
                userRecipient = intent.getParcelableExtra("USER");
        }

        //FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //RecyclerView
        mAdapterMessages = new AdapterMessages(this, null, userRecipient);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapterMessages);

        userIds = new ArrayList<>();
        userIds.add(mUser.getUid());
        userIds.add(userRecipient.getIdUser());


        //Repository
        mMessagesRepository = new MessagesRepository();


        //ViewModel
        setupViewModel();


        initToolbar(userRecipient);

        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertMessage();
            }
        });


    }


    private void initToolbar(User user) {
        Glide.with(this)
                .load(user.getPhotoUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(mImageToolbar);

        mTextToolbar.setText(user.getNome());

    }

    private void insertMessage() {
        Message message = new Message();
        message.setMessage(mEditTextMessage.getText().toString());

        Date dataHoraAtual = new Date();
        String data = new SimpleDateFormat("dd/MM/yyyy").format(dataHoraAtual);
        String hora = new SimpleDateFormat("HH:mm").format(dataHoraAtual);

        message.setDateHour(hora);
        message.setIdUser(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ArrayList<String> userIds = new ArrayList<>();
        userIds.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userIds.add(userRecipient.getIdUser());

        ArrayList<User> users = new ArrayList<>();
        users.add(new User(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString()));
        users.add(userRecipient);

        Channel chat = new Channel(message.getMessage(), userIds, users);

//        firebaseFirestore.collection("channels").document().set(channel);

        mEditTextMessage.setText("");

        mMessagesRepository.insertMessages(chat, message);

    }

    private void setupViewModel() {
        MessagesViewModel viewModel = ViewModelProviders.of(this, new MessagesViewModelFactory(this.getApplication(), userIds)).get(MessagesViewModel.class);
        viewModel.observerMessages().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                Log.d("MENSAGENS", "Updating list of users from LiveData in ViewModel");
                mAdapterMessages.updateItems(messages);
            }
        });
    }


}
