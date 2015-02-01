package com.hackrice.kevinskim93.moneytrees;


public class User {

    private double money;
    private String phone;
    private String name;
    private boolean admin;


    public User(String n, String p){
        name = n;
        phone = p;
        money = 0;
        admin = false;
    }

    public void setAdmin(boolean b){
        //admins have the ability to "balance" the transactions
        //all other users can only submit their personal transactions for the group
        admin = b;
    }

    public void setNumber(String s){
        phone = s;
    }

    public void setMoney(double m){
        money = m;
    }

    public String getNumber(){
        return phone;
    }

    public double getMoney(){

        return money;
    }

    public void addMoney(double value) {
        money += value;

    }

    public String getName(){
        return name;

    }

    public void pay(User u){

        //empties this user's money, he does not owe any more money but user u still needs money
        if(u.getMoney() >= Math.abs(money)){
            double temp = money;

            u.addMoney(money);
            money = 0;

            System.out.println(name + " paid " + u.getName() + " $" + -temp);
        }

        //empties the other user's needMoney balance, while decreasing this user's debt
        else{
            double temp = u.getMoney();
            money += temp;
            u.addMoney(-temp);

            System.out.println(name + " paid " + u.getName() + " $" + temp);
        }
    }
}

