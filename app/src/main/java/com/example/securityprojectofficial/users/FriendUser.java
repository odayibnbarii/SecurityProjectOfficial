package com.example.securityprojectofficial.users;

public class FriendUser extends User {
    public FriendUser(){

    }

    public FriendUser(String phone, String password, String fName, String lName, String userType){
        super(phone, password, fName, lName, userType);
    }

    public FriendUser(User usr){
        FriendUser user = new FriendUser(usr.phone, usr.password, usr.fName, usr.lName, usr.userType);
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
