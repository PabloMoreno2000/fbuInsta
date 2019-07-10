package com.example.fbuinsta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import models.Post;
import utils.PostAdapter;

public class Feed extends AppCompatActivity {

    private static final String TAG = "feed";

    private ArrayList<Post> posts;

    private PostAdapter adapter;

    private Button bCreate;
    private RecyclerView rvPosts;

    ParseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        user = (ParseUser) Parcels.unwrap(getIntent().getParcelableExtra("user"));

        bCreate = (Button) findViewById(R.id.bCreate);
        rvPosts = (RecyclerView) findViewById(R.id.rvPosts);

        posts = new ArrayList<>();

        adapter = new PostAdapter(posts);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        rvPosts.setLayoutManager(linearLayoutManager);

        rvPosts.setAdapter(adapter);

        bCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(Feed.this, HomeActivity.class);
                takePicture.putExtra("user", Parcels.wrap(user));
                startActivity(takePicture);
            }
        });


        loadTopPosts();


    }

    private void loadTopPosts() {

        //Get the top posts, then include on them the information of the user
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e == null) {
                    for(int i = 0; i < objects.size(); i++) {
                        Log.d(TAG, "Post " + (i + 1) + " = " + objects.get(i).getDescription()
                                + "\n username = " + objects.get(i).getUser().getUsername());
                    }


                    posts.clear();
                    adapter.notifyDataSetChanged();
                    posts.addAll(objects);
                    adapter.notifyDataSetChanged();

                }

                else {
                    e.printStackTrace();
                }
            }
        });
    }
}
