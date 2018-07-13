package me.roseliu.instagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.roseliu.instagram.model.Post;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    GalleryAdapter galleryAdapter;
    ArrayList<Post> posts;
    RecyclerView rvGallery;

    ParseUser fUser;
    ImageView ivProfilePhoto;
    TextView tvUsername;
    ImageButton ibTakeProfilePhoto;
    View view;

    public String photoFileName = "profile.jpg";
    File photoFile;
    File resizedFile;
    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        tvUsername = view.findViewById(R.id.tvUsername);
        ibTakeProfilePhoto = view.findViewById(R.id.ibTakeProfilePhoto);
        ivProfilePhoto = view.findViewById(R.id.ivProfilePhoto);

        ibTakeProfilePhoto.setOnClickListener(this);

        rvGallery = (RecyclerView)view.findViewById(R.id.rvGallery);
        posts= new ArrayList<>();
        galleryAdapter = new GalleryAdapter(view,posts);
        rvGallery.setLayoutManager(new GridLayoutManager(view.getContext(),3));
        rvGallery.setAdapter(galleryAdapter);
        populateGallery();


        return view;
    }

    private void populateGallery(){
        final Post.Query postsQuery = new Post.Query();

        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, com.parse.ParseException e) {
                if (e == null){
                    for(int i=objects.size()-1; i>0;--i){
                        Post post= objects.get(i);
                        posts.add(post);
                        galleryAdapter.notifyItemInserted(objects.size() - 1);
                    }
                }
                else{
                    e.printStackTrace();
                }
            }
        });
    }

    //TODO when take photo horizontally, the user is null
    public void setCurrentUser(ParseUser user) {
        fUser = user;
        tvUsername.setText(fUser.getUsername());
        if (fUser != null && fUser.getParseFile("Profile") != null) {
            Glide.with(getContext()).load(fUser.getParseFile("Profile").getUrl()).into(ivProfilePhoto);
        }
    }

    public void onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }
    @Override
    public void onClick(View v) {
        onLaunchCamera();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getPath());
                // RESIZE BITMAP, see section below
                // See BitmapScaler.java: https://gist.github.com/nesquena/3885707fd3773c09f1bb
                Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(takenImage, 100);
                // Load the taken image into a preview
                ivProfilePhoto = (ImageView) view.findViewById(R.id.ivProfilePhoto);
                ivProfilePhoto.setImageBitmap(resizedBitmap);


                // Configure byte output stream
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                // Compress the image further
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
                // Create a new file for the resized bitmap (`getPhotoFileUri` defined above)
                Uri resizedUri = Uri.fromFile(getPhotoFileUri(photoFileName + "_resized"));
                resizedFile = new File(resizedUri.getPath());
                try {
                    resizedFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(resizedFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                // Write the bytes of the bitmap to file
                try {
                    fos.write(bytes.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
            ParseFile file = new ParseFile(resizedFile);
            fUser.put("Profile", file);
            fUser.saveInBackground();


        }
    }




}
