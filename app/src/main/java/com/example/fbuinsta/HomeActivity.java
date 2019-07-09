package com.example.fbuinsta;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import models.Post;

public class HomeActivity extends AppCompatActivity {
    //constants
    private final static String TAG = "HomeActivity";
    private static final int CAMERA_REQUEST = 81;
    private static final int MY_CAMERA_PERMISSION_CODE = 12;

    //Elements of the view
    private Button bCreate;
    private ImageView ivTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Initializing elements of the view
        bCreate = (Button) findViewById(R.id.bCreate);

        //Listeners
        bCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If we do not have permission for the camera
                if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //request permission from the user
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }

                //If we have permission
                else {
                    //Start an intent to the camera
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });




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

        loadTopPosts();


    }


    //While requesting permissions

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //If the permission for the camera was asked
        if(requestCode == MY_CAMERA_PERMISSION_CODE) {
            //if the permission was granted
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Inform the user
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();

                //start the intent
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }

            //if the permission was denied
            else {
                //Inform the user
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ivTest.setImageBitmap(photo);
        }
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

                }

                else {
                    e.printStackTrace();
                }
            }
        });
    }
}
