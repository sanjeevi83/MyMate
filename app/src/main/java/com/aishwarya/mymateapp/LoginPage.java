package com.aishwarya.mymateapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginPage extends AppCompatActivity {
    EditText username, password;
    Button btnlogin;
    DBHelper DB;
    TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        username = (EditText)findViewById(R.id.username1);
        password = (EditText)findViewById(R.id.password1);
        btnlogin = (Button) findViewById(R.id.btnsignin1);
        signup = findViewById(R.id.textsignup1);
        DB = new DBHelper(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, SignupPage.class);
                startActivity(intent);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (user.equals("")||pass.equals(""))
                    Toast.makeText(LoginPage.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else {
                    Boolean checkuserpass = DB.checkusernamepassword(user, pass);
                    if (checkuserpass==true){
                        Toast.makeText(LoginPage.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), HomePage.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(LoginPage.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}






















