package com.example.photo_community;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class updateFragment extends Fragment {
    String postKey;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    final static int RESAULT_SUCCESS = 0;
    private Bitmap imageBitmap = null;
    boolean pickedImage = false;
    String uri;
    EditText title;
    ImageView pic;
    Button saveBtn;

    public updateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_update, container, false);
        title = v.findViewById(R.id.updateTitle);
        pic = v.findViewById(R.id.updatePic);
        saveBtn = v.findViewById(R.id.updateBtnSave);


        postKey = updateFragmentArgs.fromBundle(getArguments()).getPostKey();
        uri = updateFragmentArgs.fromBundle(getArguments()).getPicUri();
        Glide.with(this).load(uri).into(pic);
        String sTitle = updateFragmentArgs.fromBundle(getArguments()).getTitle();
        title.setText(sTitle);

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enable_input(false);
                Post post = new Post();
                post.setPostKey(postKey);
                post.setTitle(title.getText().toString());
                post.setPicture(uri);
                post.setUserId(MyApp.getCurrentUserId());
                Model.instance.updatePost(post , imageBitmap, new Model.basiconCompleteListener() {
                    @Override
                    public void onComplete(boolean done) {
                        if (done == true){
                            Navigation.findNavController(view).navigateUp();
                        }
                    }


                });


            }
        });

        return v;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            pic.setImageBitmap(imageBitmap);
            pickedImage = true;
        }
    }

    public void enable_input(boolean flag)
    {
        saveBtn.setEnabled(flag);
        title.setEnabled(flag);
        pic.setClickable(flag);
    }
}
