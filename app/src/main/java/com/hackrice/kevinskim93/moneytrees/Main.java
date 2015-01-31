package com.hackrice.kevinskim93.moneytrees;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;


public class Main extends ActionBarActivity {

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
        EditText groupName = (EditText)findViewById(R.id.groupName);
        String str = groupName.getText().toString();

        //if group already exists, return error

        //create group
        Group

        System.out.println("creating group " + str);
    }

    public void joinGroup(View v){
        EditText groupName = (EditText)findViewById(R.id.groupName);
        String str = groupName.getText().toString().trim();
        System.out.println("joining group " + str);

        //if group doesn't exist, or wrong password, return error

        setContentView(R.layout.activity);
    }

}
