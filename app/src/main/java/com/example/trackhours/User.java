package com.example.trackhours;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String name;
    private String email;
    private String username;
    private String pass;

    public User() {

    }

    public User(String name, String email, String username, String pass) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.pass = pass;
    }

    protected User(Parcel in) {
        name = in.readString();
        email = in.readString();
        username = in.readString();
        pass = in.readString();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(username);
        parcel.writeString(pass);
    }
}