package com.example.photo_community;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    NavController navController;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //if not loged in
        context = this.getApplicationContext();
        MyApp.current_user_id = Model.instance.getCurrentUserId();
        if (MyApp.current_user_id == null) {
            Intent intent = new Intent(this, LogInAndRegister.class);
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this, R.id.main_nav_host);
        NavigationUI.setupActionBarWithNavController(this, navController);

        BottomNavigationView bottomNav = findViewById(R.id.main_bottom_navigation);
        NavigationUI.setupWithNavController(bottomNav, navController);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            navController.navigateUp();
        else
            super.onOptionsItemSelected(item);
        return true;
    }
}
