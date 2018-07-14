package me.roseliu.instagram.model;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject{

    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";
    private static final String KEY_LIKEPOST="likePost";


    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image){
        put(KEY_IMAGE,image);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER,user);
    }

    public void unlikePost() {
        getRelation(KEY_LIKEPOST).remove(ParseUser.getCurrentUser());
    }

    public void likePost() {
        getRelation(KEY_LIKEPOST).add(ParseUser.getCurrentUser());
    }

    public boolean hasLiked() {
        try {
            int likeCount = getRelation(KEY_LIKEPOST)
                    .getQuery()
                    .whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId())
                    .count();
            return likeCount != 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getLikeCount() {
        try {
            return getRelation(KEY_LIKEPOST).getQuery().count();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static class Query extends ParseQuery<Post>{

        public Query() {
            super(Post.class);
        }

        public Query getTop(){
            setLimit(20);
            return this;
        }

        public Query withUser(){
            include("user");
            return this;
        }

    }

}
