package com.aryan.virtualtrading.models;

import android.net.Uri;

public class UserModel {

    private String _id, fullName, username, admin, password;
    private long phone;

//    private Uri profile;
//    private String permissions;


    public UserModel() {
    }

    public UserModel(String _id, String fullName, String username, String admin, String password, long phone) {
        this._id = _id;
        this.fullName = fullName;
        this.username = username;
        this.admin = admin;
        this.password = password;
        this.phone = phone;
    }

    public UserModel(String fullName, String username, String admin, String password, long phone) {
        this.fullName = fullName;
        this.username = username;
        this.admin = admin;
        this.password = password;
        this.phone = phone;
    }

    public UserModel(long phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }
}
