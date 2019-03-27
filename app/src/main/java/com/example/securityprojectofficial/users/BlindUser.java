package com.example.securityprojectofficial.users;

public class BlindUser extends User{
    public BlindUser(){

    }
    public BlindUser(String phone, String password, String fName, String lName, String userType){
        super(phone, password, fName, lName, userType);
    }

    public BlindUser(User usr){
        BlindUser blindUser = new BlindUser(usr.phone, usr.password, usr.fName, usr.lName, usr.userType);

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
