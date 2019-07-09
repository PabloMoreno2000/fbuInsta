package com.example.fbuinsta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import models.Post;

public class HomeActivity extends AppCompatActivity {
    //constants
    private final static String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Get the top posts, then include on them the information of the user
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();

/*        //To get all the posts in a background threat
        ParseQuery.getQuery(Post.class).findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e == null) {
                    for(int i = 0; i < objects.size(); i++) {
                        Log.d(TAG, "Post " + (i + 1) + " = " + objects.get(i).getDescription());
                    }

                }

                else {
                    e.printStackTrace();
                }
            }
        });*/


        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e == null) {
                    for(int i = 0; i < objects.size(); i++) {
                        Log.d(TAG, "Post " + (i + 1) + " = " + objects.get(i).getDescription()
                        + "\n username = " + objects.get(i).getUser().getUsername());
                    }

                }

                else {
                    e.printStackTrace();
                }
            }
        });
    }
}
