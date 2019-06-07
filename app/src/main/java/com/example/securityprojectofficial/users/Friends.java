package com.example.securityprojectofficial.users;

import java.util.ArrayList;

public class Friends {

    public ArrayList<String> friends;
    public Friends(){
        friends=new ArrayList<String>();
    }
    public void addFriend(String phone){
        friends.add(phone);
    }

}
