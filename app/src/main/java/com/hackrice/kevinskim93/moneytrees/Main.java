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

import com.firebase.client.Firebase;

import java.util.*;

public class Main extends Activity {

    Firebase myFirebaseRef;

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

    public void createGroup(View v){
        EditText nameText = (EditText)findViewById(R.id.name);
        String name = nameText.getText().toString();

        EditText pNumText = (EditText)findViewById(R.id.phoneNumber);
        String pNum = pNumText.getText().toString();

        EditText groupText = (EditText)findViewById(R.id.groupName);
        String groupName = groupText.getText().toString();

        setContentView(R.layout.create_password);

        //if group already exists, return error

        //create group
        MoneyGroup mg = new MoneyGroup(groupName);
        User u = new User(name, pNum);
        u.setAdmin(true);

        mg.addUser(u);

        //adding to Firebase
        Firebase groupsRef = myFirebaseRef.child("groups");
        Map<String, MoneyGroup> groups = new HashMap<String, MoneyGroup>();

        groups.put(groupName, mg);

        groupsRef.setValue(groups);
    }

    public void joinGroup(View v){
        EditText groupName = (EditText)findViewById(R.id.groupName);
        String str = groupName.getText().toString().trim();
        System.out.println("joining group " + str);

        //if group doesn't exist, or wrong password, return error

        setContentView(R.layout.enter_password);
    }

    public void enterPassword(View v){
        EditText userInput = (EditText)findViewById(R.id.password);

        if (userInput.getText().toString().equals("pw"))
        {
            System.out.println(userInput.getText().toString());
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
    }
/*
    public void createPassword() {
        final Activity context = this;
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.create_password, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.password);

        final EditText userConf = (EditText) promptsView
                .findViewById(R.id.confPassword);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setNegativeButton("Continue",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String user_text = (userInput.getText()).toString();

                                /** CHECK FOR USER'S INPUT
                                if (userConf.equals(userInput))
                                {
                                    Log.d(user_text, "Correct password");
                                }
                                else {
                                    Log.d(user_text, "passwords mismatch");
                                    String message = "The passwords do not match." + " \n \n" + "Please try again!";
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Error");
                                    builder.setMessage(message);
                                    builder.setPositiveButton("Cancel", null);
                                    builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            createPassword();
                                        }
                                    });
                                    builder.create().show();

                                }
                            }
                        })
                .setPositiveButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.dismiss();
                            }

                        }

                );

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

*/
}
