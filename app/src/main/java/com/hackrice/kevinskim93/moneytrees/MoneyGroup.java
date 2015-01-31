package com.hackrice.kevinskim93.moneytrees;


public class MoneyGroup {

    private String name;
    private double money;

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

}

