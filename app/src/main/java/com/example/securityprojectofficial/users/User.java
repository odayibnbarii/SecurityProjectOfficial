package com.example.securityprojectofficial.users;

import java.util.List;

public class User {
    protected String phone;
    protected String password;
    protected String fName;
    protected String lName;
    protected String userType;

    public User(){

    }

    public User(String phone, String password, String fName, String lName, String userType) {
        this.phone = phone;
        this.password = password;
        this.fName = fName;
        this.lName = lName;
        this.userType = userType;
    }

    public String getUsrType(){
        return userType;
    }
    public String getPassword(){
        return password;
    }
    public String getfName(){return fName;}
    public String getlName(){return lName;}
    public String getUserType(){return userType;}
    public String getPhone() {
        return phone;
    }
}
