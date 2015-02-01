package com.hackrice.kevinskim93.moneytrees;

import java.util.*;

public class MoneyGroup {

    private String name;
    private double money;
    private ArrayList<User> users;

    public MoneyGroup(String n){
        name = n;
        money = 0;

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

}

