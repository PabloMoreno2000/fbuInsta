package com.example.fbuinsta;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.io.File;
import java.util.List;

import models.Post;

public class HomeActivity extends AppCompatActivity {
    //constants
    private final static String TAG = "HomeActivity";
    private static final int CAMERA_REQUEST = 81;
    private static final int MY_CAMERA_PERMISSION_CODE = 12;


    private Button bPublish;
    private EditText etDescription;
    private ImageView ivTest;

    private Uri imageUri;
    ParseUser user;

    //boolean to avoid uploading an image from other post when you do not put another image
    boolean bIsPosted;


    /////
    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Initializing elements of the view
        bPublish = (Button) findViewById(R.id.bPublish);
        etDescription = (EditText) findViewById(R.id.etDescription);

        //we do not have anything to post yet
        bIsPosted = true;

        //getting current loged user

        user = (ParseUser) Parcels.unwrap(getIntent().getParcelableExtra("user"));
        Toast.makeText(this, user.getUsername(), Toast.LENGTH_LONG).show();


        bPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Post post = new Post();

                String description = etDescription.getText().toString();


                post.setDescription(description);

                post.setUser(user);

                //if there is an image to upload
                if(!bIsPosted) {
                    File photoFile = getPhotoFileUri(photoFileName);
                    ParseFile parseFile = new ParseFile(photoFile);
                    post.setImage(parseFile);

                    bIsPosted = !bIsPosted;
                }

                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(HomeActivity.this, "Published!", Toast.LENGTH_LONG).show();

                        finish();
                        //clean reference

                    }
                });



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


        //If we do not have permission for the camera
        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //request permission from the user
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        }

        //If we have permission
        else {
            //Start an intent to the camera
/*                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);*/
            onLaunchCamera(getWindow().getDecorView().findViewById(android.R.id.content));

        }

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
                onLaunchCamera(getWindow().getDecorView().findViewById(android.R.id.content));
            }

            //if the permission was denied
            else {
                //Inform the user
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
                finish();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ImageView ivPreview = (ImageView) findViewById(R.id.ivTest);
                ivPreview.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
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

    public void onLaunchCamera(View view) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName);


        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(HomeActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        //Now we have something to post
        bIsPosted = false;

        return file;
    }

/*    //function that updates the recycler view of the posts
    public void updateListPosts() {


        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        //Execute the query asynchronously
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e == null) {
                    for(Post p : objects) {
                        Log.d("loop", p.getDescription());
                    }
                }

                else {
                    e.printStackTrace();
                }


            }
        });
    }*/
}
