package com.example.fbuinsta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.parceler.Parcels;

public class MainActivity extends AppCompatActivity {

    //constants
    private static final String TAG = "MainActivity";
    private static final String PREF_NAME = "userInfo";
    private EditText etUsername;
    private EditText etPassword;
    private Button bLogin;
    private Button bSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        bSignUp = (Button) findViewById(R.id.bSingUp);
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

        bSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                //Getting the text
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();


                if(etUsername.equals("") && etPassword.equals("")) {
                    //display toast
                    Toast.makeText(MainActivity.this, "Please create user and password", Toast.LENGTH_LONG).show();
                }

                else if(etPassword.equals("")) {
                    //display toast
                    Toast.makeText(MainActivity.this, "Please create a password", Toast.LENGTH_LONG).show();
                }

                else if(etUsername.equals("")) {
                    //display toast
                    Toast.makeText(MainActivity.this, "Please create a username", Toast.LENGTH_LONG).show();

                }

                //else, everything is fine
                else {
                    //Create new ParsUser
                    ParseUser user = new ParseUser();

                    //Setting information of the user
                    user.setUsername(username);
                    user.setPassword(password);

                    //putting the user in the database
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            //If there is no error
                            if(e == null) {
                                //display toast
                                Toast.makeText(MainActivity.this, "You can now login!", Toast.LENGTH_LONG).show();
                            }

                            else {
                                e.printStackTrace();

                                Toast.makeText(MainActivity.this, "Account not registered", Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                }



            }
        });

        //Check if the user had a previous session
        if(userHadSession()) {
            SharedPreferences settings = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            String username = settings.getString("username", "");
            String password = settings.getString("password", "");

            login(username, password);
        }
    }

    private void login(final String username, final String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                //if there are no errors in the log in process
                if(e == null) {
                    Log.d(TAG, "Login successful");

                    saveLoginStatus(username, password);

                    //Lead the user to the HomeActivity
                    Intent i = new Intent(MainActivity.this, Feed.class);
                    i.putExtra("user", Parcels.wrap(user));
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

    //function to allow the current signed in user to be persisted accross app restarts
    private void saveLoginStatus(String username, String password) {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        //This means that the user has loged in
        editor.putBoolean("isLogedIn", true);
        editor.putString("username", username);
        editor.putString("password", password);

        //Apply the edits
        editor.apply();

    }

    private boolean userHadSession() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        return settings.getBoolean("isLogedIn", false);
    }
}
