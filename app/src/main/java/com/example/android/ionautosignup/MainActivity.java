package com.example.android.ionautosignup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private String username;
    private String password;
    private Toast noLogin;
    private Spinner blockSelector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp=getSharedPreferences("IonSignup", Context.MODE_PRIVATE);
        username=sp.getString("User","");
        password=sp.getString("Pass","");
        if(username.equals("") || password.equals(""))
        {
            noLogin=Toast.makeText(this,"No login information found, sign in again.",Toast.LENGTH_LONG);
            noLogin.show();
            Intent i = new Intent(this,SigninActivity.class);
            startActivity(i);

        }
        blockSelector=findViewById(R.id.block_selector);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.block_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        blockSelector.setAdapter(adapter);


    }

}
