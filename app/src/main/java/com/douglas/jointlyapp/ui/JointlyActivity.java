package com.douglas.jointlyapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.douglas.jointlyapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class JointlyActivity extends AppCompatActivity  {

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment,
                R.id.initiativeFragment,
                R.id.favoriteFragment,
                R.id.profileFragment
        ).build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int id = destination.getId();
            
            switch (id)
            {
                case R.id.manageInitiativeFragment:
                    bottomNavigationView.setVisibility(View.GONE);
                    break;
                case R.id.showInitiativeFragment:
                    //TODO Arreglar posicionamiento scroll cuando abro una iniciativa
//                    (JointlyActivity.this).findViewById(R.id.nestedScrollView);
                    bottomNavigationView.setVisibility(View.GONE);
                    break;
                default:
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    break;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}