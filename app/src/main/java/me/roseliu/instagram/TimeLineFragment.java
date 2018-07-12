package me.roseliu.instagram;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;

import java.util.ArrayList;
import java.util.List;

import me.roseliu.instagram.model.Post;


public class TimeLineFragment extends Fragment {

    PostAdapter postAdapter;
    ArrayList<Post> posts;
    RecyclerView rvPosts;
    Context mContext;

    private SwipeRefreshLayout swipeContainer;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }


    public TimeLineFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_time_line, container, false);
        rvPosts = (RecyclerView)view.findViewById(R.id.rvPosts);
        posts= new ArrayList<>();
        postAdapter = new PostAdapter(view,posts); 
        rvPosts.setLayoutManager(new LinearLayoutManager(mContext));
        rvPosts.setAdapter(postAdapter);
        populateTimeLine();

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                postAdapter.clear();
                populateTimeLine();
                //postAdapter.addAll(posts);
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        return view;
    }

    private void populateTimeLine(){
        final Post.Query postsQuery = new Post.Query();

        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, com.parse.ParseException e) {
                if (e == null){
//                    for(int i=0; i<objects.size();++i){
//                        Post post= objects.get(i);
//                        posts.add(0,post);
//                        postAdapter.notifyItemInserted(0);
//                    }
                    for(int i=objects.size()-1; i>0;--i){
                        Post post= objects.get(i);
                        posts.add(post);
                        postAdapter.notifyItemInserted(objects.size() - 1);
                    }
                }
                else{
                    e.printStackTrace();
                }
            }
        });
    }
}

