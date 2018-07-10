package me.roseliu.instagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import me.roseliu.instagram.model.Post;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("fbu_instagram")
                .clientKey("yigexindeins")
                .server("http://instagram-rose.herokuapp.com/parse")
                .build();
        Parse.initialize(configuration);

    }


}
