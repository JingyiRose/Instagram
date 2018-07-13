package me.roseliu.instagram;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseUser;

import java.util.List;

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

        View postView = inflater.inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView);
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
        if(post.getUser().getParseFile("Profile")!=null){
            Glide.with(mView.getContext()).load(post.getUser().getParseFile("Profile").getUrl()).into(holder.ivProfilePhoto);
        }
        holder.tvTimestamp.setText(post.getCreatedAt().toString());


    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView ivProfilePhoto;
        public TextView tvUsername;
        public ImageView ivPostImage;
        public ImageView ivNavigation;
        public ImageButton ibSend;
        public TextView tvDescription;
        public TextView tvTimestamp;


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
            tvTimestamp = (TextView)itemView.findViewById(R.id.tvTimestamp);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Post post = mPosts.get(position);
                Intent intent = new Intent(mView.getContext(), DetailPostActivity.class);
                intent.putExtra("objectId", post.getObjectId());
                // show the activity
                mView.getContext().startActivity(intent);
            }

        }
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




