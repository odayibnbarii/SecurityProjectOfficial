package com.example.securityprojectofficial.users;

public abstract class User {
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
    public static BlindUser blind(User usr){
        BlindUser blindUser = new BlindUser(usr.phone, usr.password, usr.fName, usr.lName, usr.userType);
        return blindUser;

    }
    public static FriendUser friend(User usr){
        FriendUser user = new FriendUser(usr.phone, usr.password, usr.fName, usr.lName, usr.userType);
        return user;

    }

}
