package com.aishwarya.mymateapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupPage extends AppCompatActivity {

    ImageView Back;

    EditText username, password, repassword;
    Button signup, signin;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        username = (EditText) findViewById(R.id.username);
        password =(EditText) findViewById(R.id.password);
        repassword =(EditText) findViewById(R.id.repassword);
        signup =(Button) findViewById(R.id.btnsignup);
        Back = (ImageView) findViewById(R.id.imageBack);
        DB  = new DBHelper(this);


        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginPage.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();

                if (user.equals("") || pass.equals("") || repass.equals(""))
                    Toast.makeText(SignupPage.this, "please enter all the fields", Toast.LENGTH_SHORT).show();

                else {
                    if (pass.equals(repass)) {
                        Boolean checkuser = DB.checkusername(user);
                        if (checkuser == false) {
                            Boolean insert = DB.insertUserData(user, pass);
                            if (insert == true) {
                                Toast.makeText(SignupPage.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignupPage.this, "Registration called", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignupPage.this, "user already exists' please sign in", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignupPage.this, "Password not matching", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}