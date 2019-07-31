package com.example.myslack.feature.myprofile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.example.myslack.R;
import com.example.myslack.feature.login.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyProfileActivity extends AppCompatActivity {

    //View
    private ImageView imgProfile, imgToolbarAvatar;
    private TextView textUsernameProfile, textEmailProfile;
    private Button btnEditProfile, btnLogoutProfile;
    private Toolbar mToolbar;
    private TextView textToolbarTitle;

    //Firebase
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        this.imgProfile = findViewById(R.id.imgProfile);
        this.textUsernameProfile = findViewById(R.id.textUsernameProfile);
        this.textEmailProfile = findViewById(R.id.textEmailProfile);
        this.btnEditProfile = findViewById(R.id.btnEditProfile);
        this.btnLogoutProfile = findViewById(R.id.btnLogoutProfile);
        this.mToolbar = findViewById(R.id.toolbarInclude);

        this.imgToolbarAvatar = findViewById(R.id.imgToolbarAvatar);

        //Set default action bar
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);

            this.textToolbarTitle = findViewById(R.id.textTitleToolbar);
            textToolbarTitle.setText(getString(R.string.my_profile));
        }

        mAuth = FirebaseAuth.getInstance();

        btnLogoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        initUI();
    }

    private void initUI() {
        if (mAuth != null) {
            mUser = mAuth.getCurrentUser();

            this.textUsernameProfile.setText(mUser.getDisplayName());
            this.textEmailProfile.setText(mUser.getEmail());

            RequestOptions requestOptions = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).circleCrop();

            Glide.with(this)
                    .asBitmap()
                    .load(mUser.getPhotoUrl())
                    .apply(requestOptions)
                    .into(imgProfile);

            Glide.with(this)
                    .asBitmap()
                    .load(mUser.getPhotoUrl())
                    .apply(requestOptions)
                    .into(imgToolbarAvatar);
        }
    }

    private void signOut() {
        mAuth.signOut();
        //solicitar os dados do usuário exigidos pelo seu aplicativo.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getBaseContext(), gso);
        mGoogleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent loginIntent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }
}
