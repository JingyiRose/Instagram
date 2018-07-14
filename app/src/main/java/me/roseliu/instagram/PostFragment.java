package me.roseliu.instagram;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;


public class PostFragment extends Fragment implements View.OnClickListener {


    public void pbdisappear() {
        pbLoading.setVisibility(View.INVISIBLE);

    }

    interface Callback {

        void createPost(View view);

    }

    ProgressBar pbLoading;

    private Callback PostCallback;
    ImageView ivPreview;
    EditText etDescription;

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // `instanceof` here is how we check if the containing context (in our case the activity)
        // implements the required callback interface.
        //
        // If it does not implement the required callback, we want
        if (context instanceof Callback) {

            // If it is an instance of our Callback then we want to cast the context to a Callback
            // and store it as a reference so we can later update the callback when there has been
            // a text change event.
            PostCallback = (Callback) context;
        } else {
            // Throwing an error and making your application crash instead of just sweeping it under
            // the rug is called being an "offensive" programmer.
            //
            // The best defense is a strong offense.
            throw new IllegalStateException("Containing context must implement UserInputFragment.Callback.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        ivPreview = (ImageView) view.findViewById(R.id.ivPreview);
        etDescription = (EditText) view.findViewById(R.id.etDescription);
        final Button btShare = (Button) view.findViewById(R.id.btShare);
        btShare.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        pbLoading = (ProgressBar)getView().findViewById(R.id.pbLoading);
        pbLoading.setVisibility(View.VISIBLE);
        PostCallback.createPost(v);
        ivPreview.setImageResource(0);
        etDescription.setText("");



    }
}

