package com.example.android.ionautosignup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.SigningInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SigninActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button login;
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
        sp=getSharedPreferences("IonSignup",Context.MODE_PRIVATE);
        edit=sp.edit();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(username.getText().toString().equals("") && password.getText().toString().equals("")))
                {
                    edit.putString("User", username.getText().toString());
                    edit.putString("Pass",password.getText().toString());
                    edit.commit();
                }
                Intent i=new Intent(SigninActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
    }
}
