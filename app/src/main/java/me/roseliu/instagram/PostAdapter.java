package me.roseliu.instagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import me.roseliu.instagram.model.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    private List<Post> mPosts;
    View mView;

    public PostAdapter(View view, List<Post> posts){
        mPosts=posts;
        mView = view;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get the data according to position

        Post post = mPosts.get(position);

        //populate the views according to this data
        ParseUser user= post.getUser();
        try {
            String name=user.fetchIfNeeded().getUsername();
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }

        holder.tvDescription.setText(post.getDescription());
        holder.tvUsername.setText(user.getUsername());
        Glide.with(mView.getContext()).load(post.getImage().getUrl()).into(holder.ivPostImage);


    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivProfilePhoto;
        public TextView tvUsername;
        public ImageView ivPostImage;
        public ImageView ivNavigation;
        public ImageButton ibSend;
        public TextView tvDescription;


        public ViewHolder(View itemView) {

            super(itemView); //take in inflated layout
            View view= itemView;
            //perform findViewById lookups

            ivProfilePhoto = (ImageView) itemView.findViewById(R.id.ivProfilePhoto);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            ivPostImage = (ImageView) itemView.findViewById(R.id.ivPostImage);
            ivNavigation = (ImageView)itemView.findViewById(R.id.ivNavigation);
            ibSend = (ImageButton)itemView.findViewById(R.id.ibSend);
            tvDescription=(TextView)itemView.findViewById(R.id.tvDescription);


        }

    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String InstagramFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(InstagramFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    // Clean all elements of the recycler
    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }
}




