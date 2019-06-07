package com.example.securityprojectofficial.users;

import com.example.securityprojectofficial.Security.CipherDatabase;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FriendUser extends User {

    public FriendUser(){

    }

    public FriendUser(String phone, String password, String fName, String lName, String userType){
        super(phone, password, fName, lName, userType);
    }

    public FriendUser(User usr){
       // FriendUser user = new FriendUser(usr.phone, usr.password, usr.fName, usr.lName, usr.userType);
        super(usr.phone, usr.password, usr.fName, usr.lName, usr.userType);
    }


    public String getUsrType(){
        return userType;
    }
    public String getPassword(){
        return password;
    }

    public String getPhone() {
        return phone;
    }
}
