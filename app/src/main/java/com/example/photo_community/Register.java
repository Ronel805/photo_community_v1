package com.example.photo_community;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Register extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    final static int RESAULT_SUCCESS = 0;


    EditText email, password;
    Button toLogIn, Register;
    ImageView profileImage;
    boolean isPicked = false;
    Bitmap imageBitmap;

    public Register() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        boolean hasPermission = (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        email = v.findViewById(R.id.registerEmail);
        password = v.findViewById(R.id.registerPassword);
        toLogIn = v.findViewById(R.id.RegisterToLogInBtn);
        profileImage = v.findViewById(R.id.registerProfileImage);
        Register = v.findViewById(R.id.registerBtn);

        toLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email != null && password != null && isPicked && password.length() > 5) {
                    String sEmail = email.getText().toString();
                    String sPassword = password.getText().toString();
                    Model.instance.register(sEmail, sPassword, new Model.basicListener() {

                        @Override
                        public void onSuccess(String id) {
                            MyApp.setCurrentUserId(id);
                            Model.instance.addUser(id, sEmail, sPassword, imageBitmap, new Model.basiconCompleteListener() {
                                @Override
                                public void onComplete(boolean done) {
                                    if (done == true) {
                                        getActivity().finish();
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });

                        }

                        @Override
                        public void onFailure(String e) {
                            showMessage(e);
                        }

                    });
                } else
                if ( password.length() <6){showMessage("please enter 6 digits password or longer");}
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();

            }
        });

        return v;
    }

    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
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
            profileImage.setImageBitmap(imageBitmap);
            isPicked = true;
        }
    }







}
