/**
 * @Author: Christopher Richards
 * @File: MainActivity.java
 * @Date: 2023-12-09
 * @Email: christopher.richards4@snhu.edu
 * @Description: Java class that serves as the entry point
 * for the app. Contains the fragment container and navigation components
 */


package com.zybooks.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    private InventoryViewModel inventoryViewModel;
    private NavController navController;
    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the navView and navigate to the sign in fragment if the
        // user is not authenticated.
        navView = findViewById(R.id.nav_view);

        if(userViewModel == null){
            userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
            userViewModel.getUserLoginStatus().observe( this, blnLoggedIn -> {
                handleUserState(blnLoggedIn);
            });
            userViewModel.setUserLoginStatus(false);
        }

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);



        if(navHostFragment != null){
            navController = navHostFragment.getNavController();
            AppBarConfiguration appBarConfig = new AppBarConfiguration.Builder(R.id.navigation_user_profile, R.id.navigation_inventory, R.id.navigation_settings).build();

            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
            NavigationUI.setupWithNavController(navView, navController);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp() || super.onSupportNavigateUp();
    }

    private void handleUserState(boolean blnLoggedIn){
        if(blnLoggedIn){
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.navigation_sign_in, true)
                    .build();
            navController.navigate(R.id.navigation_inventory, null, navOptions);
            navView.setVisibility(View.VISIBLE);
        }
        else{
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.navigation_inventory, true)
                    .build();
            navController.navigate(R.id.navigation_sign_in, null, navOptions);
            navView.setVisibility(View.GONE);
        }
    }
}