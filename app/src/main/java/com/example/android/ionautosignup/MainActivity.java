package com.example.android.ionautosignup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private String username;
    private String password;
    private Toast noLogin;
    private Spinner blockSelector;
    private String curBlock;
    private TextView debug;
    private IonAPI ion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp=getSharedPreferences("IonSignup", Context.MODE_PRIVATE);
        debug=findViewById(R.id.debug);

        username=sp.getString("User","");
        password=sp.getString("Pass","");
        if(username.equals("") || password.equals(""))
        {
            noLogin=Toast.makeText(this,"No login information found, sign in again.",Toast.LENGTH_LONG);
            noLogin.show();
            Intent i = new Intent(this,SigninActivity.class);
            startActivity(i);

        }

        ion=new IonAPI(username,password);

        blockSelector=findViewById(R.id.block_selector);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.block_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        blockSelector.setAdapter(adapter);

        blockSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                curBlock=getResources().getStringArray(R.array.block_array)[position];
                System.out.println(curBlock);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ion.getAllSignups();

        //String str=ion.signUp(3556,250,109771);
        String str=ion.getAllActivities().toString();
        JSONObject json=ion.getAllActivitiesinBlock(3556);
        String activityNames="";
        HashMap<String, Integer> activityIdMap=ion.getActivitiesList(3556).toString();
        //debug.setText(str);

    }

}
