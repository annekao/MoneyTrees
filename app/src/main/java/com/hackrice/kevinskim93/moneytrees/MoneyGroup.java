package com.hackrice.kevinskim93.moneytrees;

import java.util.*;

public class MoneyGroup {

    private String name;
    private double money;
    private ArrayList<User> users;
    private String password;

    public MoneyGroup(String n){
        name = n;
        money = 0;
        users = new ArrayList<User>();
        password = "";

    }

    public void addMoney(double value){
        money += value;
    }

    public double getMoney(){

        return money;
    }

    public void addUser(User u){

        users.add(u);
    }

    public ArrayList<User> getUsers(){
        return users;
    }

    public void setPassword(String pw){
        password = pw;
    }

    public String getPassword(){
        return password;
    }

}

