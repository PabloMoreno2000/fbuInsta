package models;


import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {

    //names of the columns in the database
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";

    public String getDescription() {
        //Coming from the ParseObject class
        return getString(KEY_DESCRIPTION);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    //a query just of the post class
    public static class Query extends ParseQuery<Post> {
        public Query() {
            super(Post.class);
        }

        public Query getTop() {
            //get the first 20 posts
            setLimit(20);
            return this;
        }

        //returns the posts with the information of the user
        public Query withUser() {
            include(KEY_USER);
            return this;
        }
    }


}
