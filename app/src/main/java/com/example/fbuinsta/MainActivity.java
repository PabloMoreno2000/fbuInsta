package com.example.fbuinsta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    //constants
    private static final String TAG = "MainActivity";
    private EditText etUsername;
    private EditText etPassword;
    private Button bLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the data of the user
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                //try to log the user with the given credentials
                login(username, password);
            }
        });
    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                //if there are no errors in the log in process
                if(e == null) {
                    Log.d(TAG, "Login successful");

                    //Lead the user to the HomeActivity
                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(i);

                    //Close this activity
                    finish();
                }

                else {
                    Log.d(TAG, "Login failure");
                    e.printStackTrace();
                }
            }
        });
    }
}
