package com.douglas.jointlyapp.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class JointlyActivity extends AppCompatActivity  {

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private NestedScrollView nestedScrollView;
    private FloatingActionButton floatingActionButton;
    private View viewBottomSheet;
    private BottomSheetBehavior<View> bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        toolbar = findViewById(R.id.toolbar);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        floatingActionButton = findViewById(R.id.faButton);
        viewBottomSheet = findViewById(R.id.bottomSheetJoinInitiative);

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
                case R.id.homeFragment:
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    floatingActionButton.setVisibility(View.GONE);
                    break;
                case R.id.initiativeFragment:
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    floatingActionButton.setVisibility(View.VISIBLE);
                    floatingActionButton.setImageResource(R.drawable.ic_add);
                    break;
                case R.id.manageInitiativeFragment:
                    bottomNavigationView.setVisibility(View.GONE);
                    floatingActionButton.setImageResource(R.drawable.ic_edit);
                    break;
                case R.id.favoriteFragment:
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    floatingActionButton.setVisibility(View.GONE);
                    break;
                case R.id.profileFragment:
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    floatingActionButton.setVisibility(View.GONE);
                    break;
                default:
                    bottomNavigationView.setVisibility(View.GONE);
                    floatingActionButton.setVisibility(View.GONE);
                    break;
            }
            nestedScrollView.scrollTo(0,0);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_settings:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.settingFragment);
                break;
            case R.id.action_logout:
                AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(JointlyActivity.this, "Sesion cerrada", Toast.LENGTH_SHORT).show();
                    }
                });
                JointlyPreferences.getInstance().putRemember(false);
                finish();
                break;
            case R.id.action_editAccount:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.accountFragment);
                break;
        }

        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}