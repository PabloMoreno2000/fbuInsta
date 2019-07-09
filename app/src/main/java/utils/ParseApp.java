package utils;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import models.Post;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //To inform that this is a custom object
        ParseObject.registerSubclass(Post.class);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("pablofbuinstagram")
                .clientKey("keynotfoundsorryhuman404")
                .server("http://pablomoreno2000-fbu-instagram.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}
