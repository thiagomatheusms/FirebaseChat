package com.example.myslack.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{

    private String idUser;
    private String nome;
    private String email;
    private String photoUrl;

    public User() {
    }

    public User(String id, String nome, String email, String photoUrl) {
        this.idUser = id;
        this.nome = nome;
        this.email = email;
        this.photoUrl = photoUrl;
    }


    protected User(Parcel in) {
        idUser = in.readString();
        nome = in.readString();
        email = in.readString();
        photoUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String id) {
        this.idUser = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idUser);
        dest.writeString(nome);
        dest.writeString(email);
        dest.writeString(photoUrl);
    }
}
