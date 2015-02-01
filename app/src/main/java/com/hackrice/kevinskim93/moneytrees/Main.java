package com.hackrice.kevinskim93.moneytrees;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.*;

public class Main extends Activity {
    Firebase myFirebaseRef;
    Firebase groupsRef;
    String name, pNum, groupName;
    Double billDouble;
    Object groups;
    User u;
    boolean isNewUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://luminous-inferno-581.firebaseio.com/");
        final Firebase groupsRef = myFirebaseRef.child("groups");
        groups = new HashMap<String,Object>();

        groupsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                groups = (Map<String, Object>) snapshot.getValue();
                System.out.println("initial read");
                if(groups == null){
                    groupsRef.setValue("groups", null);
                }
                //System.out.println(snapshot.getValue().getClass() + "A");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        Map<String, String> temp = new HashMap<String, String>();
        Firebase tempRef = myFirebaseRef.child("temp");
        tempRef.setValue(temp);

        setContentView(R.layout.homepage);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createGroup(View v) {
        EditText nameText = (EditText) findViewById(R.id.name);
        name = nameText.getText().toString();

        EditText pNumText = (EditText) findViewById(R.id.phoneNumber);
        pNum = pNumText.getText().toString();

        EditText groupText = (EditText) findViewById(R.id.groupName);
        groupName = groupText.getText().toString();



        setContentView(R.layout.create_password);

        //if group already exists, return error


    }

    public void joinGroup(View v) {
        EditText nameText = (EditText) findViewById(R.id.name);
        name = nameText.getText().toString();

        EditText pNumText = (EditText) findViewById(R.id.phoneNumber);
        pNum = pNumText.getText().toString();

        EditText groupText = (EditText) findViewById(R.id.groupName);
        groupName = groupText.getText().toString().trim();
        System.out.println("joining group " + groupName);

        //if group doesn't exist, or wrong password, return error
        Firebase groupsRef = myFirebaseRef.child("groups");
        groupsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                groups = (Map<String, Object>) snapshot.getValue();
                System.out.println(snapshot.getValue().getClass() + "B");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        Map<String, String> temp = new HashMap<String, String>();
        Firebase tempRef = myFirebaseRef.child("temp");
        tempRef.setValue(temp);

        if (!((HashMap<String, Object>) groups).containsKey(groupName)){
            String message = "The group you have entered does not exist." + " \n \n" + "Please try again!";
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage(message);
            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    setContentView(R.layout.homepage);
                }
            });
            builder.create().show();
        }
        setContentView(R.layout.enter_password);
    }

    public void enterPassword(View v) {


        Map<String, Object> tempMap = (Map<String, Object>) ((HashMap<String, Object>) groups).get(groupName);

        System.out.println(tempMap);
        System.out.println(tempMap.getClass());
        System.out.println(tempMap.get("password").getClass());

        String actualPW = ((Map<String, Object>) tempMap).get("password").toString();
        EditText userInput = (EditText) findViewById(R.id.password);
        String userPW = userInput.getText().toString();

        if (actualPW.equals(userPW)) {
            u = new User(name, pNum);

            System.out.println("JOINED");

            final ArrayList<Object> userList = (ArrayList<Object>) tempMap.get("users");

            userList.add(u);


            groupsRef = myFirebaseRef.child("groups");
            groupsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    ((Map<String, Object>) groups).put("users", ((Object) userList));
                    groupsRef.setValue(groups);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The read failed: " + firebaseError.getMessage());
                }
            });



        }
        else {
            System.out.println(userInput.getText().toString());
            String message = "The password you have entered is incorrect." + " \n \n" + "Please try again!";
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage(message);
            builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    setContentView(R.layout.homepage);
                }
            });
            builder.setNegativeButton("Retry", null);
            builder.create().show();
        }
        setTransactionList();
        setContentView(R.layout.activity);
        /*groups.put(groupName, moneyGroup);
        Firebase groupsRef = myFirebaseRef.child("groups");
        groupsRef.setValue(groups);*/
    }

    public void createPassword(View v) {
        EditText userInput = (EditText) findViewById(R.id.password);
        EditText confUserInput = (EditText) findViewById(R.id.confPassword);
        String groupPW = "";
        if (userInput.getText().toString().equals(confUserInput.getText().toString())) {
            groupPW = userInput.getText().toString();
            addToDB(groupPW);
        }
        else {
            System.out.println(userInput.getText().toString());
            String message = "The password you've entered do not match." + " \n \n" + "Please try again!";
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage(message);
            builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    setContentView(R.layout.homepage);
                }
            });
            builder.setNegativeButton("Retry", null);
            builder.create().show();
        }
    }

    public void addToDB(String pw) {
        //create group
        final MoneyGroup mg = new MoneyGroup(groupName);
        u = new User(name, pNum);

        u.setNumber(pNum);
        u.setAdmin(true);
        mg.addUser(u);
        mg.setPassword(pw);

        //adding to Firebase
        groupsRef = myFirebaseRef.child("groups");
        groupsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                groups = (Map<String, Object>) snapshot.getValue();
                ((Map<String, Object>) groups).put(groupName, mg);
                groupsRef.setValue(groups);

                System.out.println(snapshot.getValue().getClass() + "C");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        Map<String, String> temp = new HashMap<String, String>();
        Firebase tempRef = myFirebaseRef.child("temp");
        tempRef.setValue(temp);
        setTransactionList();
        setContentView(R.layout.admin_activity);


    }

    public void back(View v) {
        setContentView(R.layout.homepage);
    }

    public void setTransactionList(){
        Map<String, Object> tempMap = (Map<String,Object>)((Map<String, Object>) groups);
        Object moneyGroupMap = tempMap.get(groupName);

        if(moneyGroupMap instanceof MoneyGroup){


        }

        //not money group
        else {

            ListView list = (ListView) findViewById(R.id.listView);
            List<String> transactionStrings = new ArrayList<String>();
            List<Object> usersListMap = (ArrayList<Object>) ((Map<String, Object>) moneyGroupMap).get("users");
            for (Object o : usersListMap) {
                //User user2 = new User((String)((Map<String, Object>)u).get("name"),
                //        (String)((Map<String, Object>)u).get("number") );
                //User user2 = (User) user;


                System.out.println(o.getClass());
                System.out.println(o);

                String s1 = "";
                Double s2 = 0.0;

                if(!(o instanceof User)){

                    s1 = (String) ((Map<String, Object>) o).get("name");
                     s2 = (Double) ((Map<String, Object>) o).get("money");

                }

                else{
                     s1 = ((User) o).getName();
                     s2 = ((User) o).getMoney();

                }

                transactionStrings.add(s1 + " contributed " +
                        s2);

            }
            ArrayAdapter adapter = new ArrayAdapter<String>(this,
                    R.layout.activity,
                    transactionStrings);

            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    public void addPayment(View v){
        //add user's transaction into the database
        //refresh list
        EditText amntText = (EditText) findViewById(R.id.amountPaid);
        Double amount = Double.parseDouble(amntText.getText().toString());

        u.addMoney(amount);


        Map<String, Object> tempMap = (Map<String,Object>)((Map<String, Object>) groups);

        System.out.println(tempMap);

        Object mg = (Object) tempMap.get(groupName);

        if(mg.getClass().equals(tempMap.getClass())){

            Map<String, Object> temporaryMap = (Map<String,Object>)((Map<String, Object>) tempMap.get(groupName));
            String billString = ((Map<String, Object>) temporaryMap).get("money").toString();
            System.out.println("temp map : " + temporaryMap);

            isNewUser = true;

            billDouble = amount;

        }
        else{

            mg = (MoneyGroup) tempMap.get(groupName);
            System.out.println("mg: " + mg);
            System.out.println(((MoneyGroup) mg).getMoney());

            isNewUser = false;

            billDouble = amount;
        }

        System.out.println(mg);

        //billDouble = mg.getMoney() + amount;



        final ArrayList<Object> userList = (ArrayList<Object>) tempMap.get("users");

        groupsRef = myFirebaseRef.child("groups");
        groupsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {



                //((Map<String, Object>) groups).put("money", ((Object) billDouble).toString());
                MoneyGroup mg = null;

                if(!isNewUser) {
                    Map<String, Object> tempMap = (Map<String,Object>)((Map<String, Object>) groups);
                    //Map<String, Object> temporaryMap = (Map<String,Object>)((Map<String, Object>) tempMap.get(groupName));
                    mg = (MoneyGroup) ((Map<String, Object>) groups).get(groupName);

                    List<User> usersListMap = (List<User>)mg.getUsers();

                    System.out.println(usersListMap.size());

                    ((Map<String, Object>) groups).put("users", usersListMap);

                    mg = (MoneyGroup) ((Map<String, Object>) groups).get(groupName);
                    mg.addMoney( billDouble );
                }
                else{

                    ((Map<String, Object>) groups).put("users", ((Object) userList));
                    Object tempMoneyMap = ((Map<String, Object>)groups).get(groupName);
                    Double money = (Double) ((Map<String, Object>) tempMoneyMap).get("money");
                    ((Map<String, Object>) tempMoneyMap).put("money", (Object) (money + billDouble));
                }


                groupsRef.setValue(groups);

                isNewUser = false;
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        setTransactionList();
    }

    public void balance(View v){
        //compiles list of who to pay with option to venmo
        ArrayList<String> iPay = new ArrayList<String>();
        ArrayList<String> iGet = new ArrayList<String>();

        System.out.println(groupName);

        groupsRef = myFirebaseRef.child("groups");
        groupsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                groups = (Map<String, Object>) snapshot.getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        Map<String, String> temp = new HashMap<String, String>();
        Firebase tempRef = myFirebaseRef.child("temp");
        tempRef.setValue(temp);


        Map<String, Object> tempMap = (Map<String,Object>)((Map<String, Object>) groups);

        Object moneyGroupMap = tempMap.get(groupName);

        //money group
        if(moneyGroupMap instanceof MoneyGroup){


        }

        //not money group
        else{

            ArrayList<Object> usersListMap = (ArrayList<Object>)((Map<String, Object>) moneyGroupMap).get("users");

            double disbursement = 0;
            for(Object o : usersListMap){
                //o is a map, use its data

                Double money = (Double)((Map<String, Object>)o).get("money");

                disbursement += money;
            }

            disbursement = disbursement / usersListMap.size();

            System.out.println(disbursement);

            Stack<User> oweMoneyUsers = new Stack<User>();
            Stack<User> needMoneyUsers = new Stack<User>();

            for(Object o : usersListMap){

                User user = new User((String)((Map<String, Object>)o).get("name"),
                                     (String)((Map<String, Object>)o).get("number") );
                user.setMoney((Double)((Map<String, Object>)o).get("money"));

                user.addMoney(-disbursement);

                if(user.getMoney() < 0){

                   oweMoneyUsers.push(user);

                }
                else if (user.getMoney() > 0){

                   needMoneyUsers.push(user);


                }
            }


            while(!oweMoneyUsers.isEmpty()){

                User owe = oweMoneyUsers.peek();

                System.out.println("Owes money is " + owe.getMoney());

                User need = needMoneyUsers.peek();

                System.out.println("Needs money is " + need.getMoney());

                while(owe.getMoney() < 0){

                    if(Math.abs(owe.getMoney()) > need.getMoney()){
                        //owe pays all of need's balance, but still has some over

                        owe.addMoney(need.getMoney());
                        need.setMoney(0);
                        needMoneyUsers.pop();
                        need = needMoneyUsers.peek();
                        System.out.println("Needs money is " + need.getMoney());

                    }
                    else{
                        //owe is fully out of debt, go to the next need money user

                        need.addMoney(owe.getMoney());
                        owe.setMoney(0);
                    }
                }

                oweMoneyUsers.pop();
            }
        }
    }
}
