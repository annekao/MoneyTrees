package com.hackrice.kevinskim93.moneytrees;


public class User {

    private double money;
    private String name;
    private boolean admin;
    private String phone;

    public User(String n){
        name = n;
        money = 0;

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

