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
import android.widget.EditText;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.*;

public class Main extends Activity {
    Firebase myFirebaseRef;
    Firebase groupsRef;
    String name, pNum, groupName;
    Object groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://luminous-inferno-581.firebaseio.com/");
        Firebase groupsRef = myFirebaseRef.child("groups");
        groups = new HashMap<String,MoneyGroup>();
        groupsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                groups = (Map<String, MoneyGroup>) snapshot.getValue();
                System.out.println("initial read");
                System.out.println(snapshot.getValue().getClass() + "A");
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
                groups = (Map<String, MoneyGroup>) snapshot.getValue();
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

        if (!((HashMap<String, MoneyGroup>) groups).containsKey(groupName)){
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
            User u = new User(name, pNum);
            System.out.println("JOINED");
            //moneyGroup.addUser(u);
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
        User u = new User(name, pNum);
        u.setAdmin(true);
        mg.addUser(u);
        mg.setPassword(pw);

        //adding to Firebase
        groupsRef = myFirebaseRef.child("groups");
        groupsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                groups = (Map<String, MoneyGroup>) snapshot.getValue();
                ((Map<String, MoneyGroup>) groups).put(groupName, mg);
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


    }

    public void back(View v) {
        setContentView(R.layout.homepage);
    }
}