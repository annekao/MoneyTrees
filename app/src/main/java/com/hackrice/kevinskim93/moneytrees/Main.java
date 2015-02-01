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
    String name, pNum, groupName;
    Map<String, MoneyGroup> groups = new HashMap<String, MoneyGroup>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://luminous-inferno-581.firebaseio.com/");
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
        EditText groupText = (EditText) findViewById(R.id.groupName);
        groupName = groupText.getText().toString().trim();
        System.out.println("joining group " + groupName);

        //if group doesn't exist, or wrong password, return error

        setContentView(R.layout.enter_password);
    }

    public void enterPassword(View v) {
        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                groups = (HashMap<String, MoneyGroup>) snapshot.getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        MoneyGroup moneyGroup = groups.get(groupName);
        if (moneyGroup == null){
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
        String actualPW = moneyGroup.getPassword();
        EditText userInput = (EditText) findViewById(R.id.password);
        String userPW = userInput.getText().toString();

        if (actualPW.equals(userPW)) {
            System.out.println(actualPW);
            // TODO: add user
        } else {
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
    }

    public void createPassword(View v) {
        EditText userInput = (EditText) findViewById(R.id.password);
        EditText confUserInput = (EditText) findViewById(R.id.confPassword);
        String groupPW = "";
        if (userInput.getText().toString().equals(confUserInput.getText().toString())) {
            groupPW = userInput.getText().toString();
            addToDB(groupPW);
        } else {
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
        MoneyGroup mg = new MoneyGroup(groupName);
        User u = new User(name, pNum);
        u.setAdmin(true);
        mg.addUser(u);
        mg.setPassword(pw);

        //adding to Firebase
        Firebase groupsRef = myFirebaseRef.child("groups");
        groupsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                groups = (Map<String, MoneyGroup>) snapshot.getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        groups.put(groupName, mg);
        groupsRef.setValue(groups);
    }

    public void back(View v) {
        setContentView(R.layout.homepage);
    }
}