package com.example.photo_community;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


/**
 * A simple {@link Fragment} subclass.
 */
public class LogIn extends Fragment {

    EditText email, password;
    Button logIn, toRegister;

    public LogIn() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_log_in, container, false);
        email = v.findViewById(R.id.logIn_email);
        password = v.findViewById(R.id.loginPassword);
        logIn = v.findViewById(R.id.logInBtn);
        toRegister = v.findViewById(R.id.logInToRegisterBtn);

        toRegister.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_logIn_to_register));
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sEmail = email.getText().toString();
                String sPassword = password.getText().toString();
                if (email != null && password != null && sEmail.length() != 0 && sPassword.length() != 0 ) {

                    Model.instance.login(sEmail, sPassword, new Model.basicListener(){

                        @Override
                        public void onSuccess(String id) {
                            MyApp.setCurrentUserId(id);
                            getActivity().finish();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(String e) {
                            showMessage(e);
                        }

                    });
                }
                else
                    showMessage("please enter all parameters");
            }
        });

        return v;
    }
    private void showMessage(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

}
