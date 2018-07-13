package me.roseliu.instagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseUser;

import java.util.List;

import me.roseliu.instagram.model.Post;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder>{

    private List<Post> mPosts;
    View mView;

    public GalleryAdapter(View view, List<Post> posts){
        mPosts=posts;
        mView = view;
    }

    @NonNull
    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View photoView = inflater.inflate(R.layout.item_photo, parent, false);
        GalleryAdapter.ViewHolder viewHolder = new GalleryAdapter.ViewHolder(photoView);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull GalleryAdapter.ViewHolder holder, int position) {
        //get the data according to position

        Post post = mPosts.get(position);

        //populate the views according to this data
        ParseUser user= post.getUser();
        try {
            String name=user.fetchIfNeeded().getUsername();
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }


        Glide.with(mView.getContext())
                .load(post.getImage().getUrl())
                .apply(
                        RequestOptions.centerCropTransform()
                )
                .into(holder.ivPostImage);

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public ImageView ivPostImage;


        public ViewHolder(View itemView) {

            super(itemView); //take in inflated layout
            View view= itemView;
            //perform findViewById lookups

            ivPostImage = (ImageView) itemView.findViewById(R.id.ivPostImage);


        }

//        @Override
//        public void onClick(View v) {
//            int position=getAdapterPosition();
//            if (position != RecyclerView.NO_POSITION) {
//                Post post = mPosts.get(position);
//                Intent intent = new Intent(mView.getContext(), DetailPostActivity.class);
//                intent.putExtra("objectId", post.getObjectId());
//                // show the activity
//                mView.getContext().startActivity(intent);
//            }
//
//        }
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
