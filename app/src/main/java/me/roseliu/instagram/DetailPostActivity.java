package me.roseliu.instagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.Date;

import me.roseliu.instagram.model.Post;

public class DetailPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        final TextView tvUsername = findViewById(R.id.tvUsername);
        final TextView tvDescription = findViewById(R.id.tvDescription);
        final TextView tvTimestamp = findViewById(R.id.tvTimestamp);
        final ImageView ivPostImage = findViewById(R.id.ivPostImage);

        String objectId = getIntent().getStringExtra("objectId");

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // Specify the object id
        query.getInBackground(objectId, new GetCallback<Post>() {
            public void done(Post post, ParseException e) {
                if (e == null) {
                    // Access data using the `get` methods for the object
                    String description = post.getDescription();
                    tvDescription.setText(description);
                    // Access special values that are built-in to each object

                    Date createdAt = post.getCreatedAt();

                    tvTimestamp.setText(createdAt.toString());

                    try {
                        String username= post.getUser().fetchIfNeeded().getUsername();
                        tvUsername.setText(username);
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                    String url = post.getImage().getUrl();

                    Glide.with(DetailPostActivity.this).load(post.getImage().getUrl()).into(ivPostImage);

                } else {
                    e.printStackTrace();
                }
            }
        });



    }
}
