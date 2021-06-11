package com.douglas.jointlyapp.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
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
import com.douglas.jointlyapp.ui.broadcast.CheckConnectionBroadCast;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.douglas.jointlyapp.ui.service.CheckConnectionService;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class JointlyActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private NestedScrollView nestedScrollView;
    private FloatingActionButton floatingActionButton;
    private View viewBottomSheetInitiative;
    private BottomSheetBehavior<View> bottomSheetInitiative;
    private TextView tvNoConnection;
    private Intent intentCheckConnection;
    private CheckConnectionBroadCast checkConnectionBroadCast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        toolbar = findViewById(R.id.toolbar);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        floatingActionButton = findViewById(R.id.faButton);
        viewBottomSheetInitiative = findViewById(R.id.bottomSheetJoinInitiative);
        bottomSheetInitiative = BottomSheetBehavior.from(viewBottomSheetInitiative);

        tvNoConnection = findViewById(R.id.tvNoConnection);

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
                case R.id.favoriteFragment:
                case R.id.profileFragment:
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    floatingActionButton.setVisibility(View.GONE);
                    break;
                case R.id.initiativeFragment:
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    floatingActionButton.setVisibility(View.VISIBLE);
                    floatingActionButton.setImageResource(R.drawable.ic_add);
                    break;
                case R.id.manageInitiativeFragment:
                case R.id.showUserProfileFragment:
                    bottomNavigationView.setVisibility(View.GONE);
                    floatingActionButton.setVisibility(View.GONE);
                    break;
                default:
                    bottomNavigationView.setVisibility(View.GONE);
                    floatingActionButton.setVisibility(View.GONE);
                    break;
            }
            nestedScrollView.scrollTo(0,0);
        });

        //TODO sync
        // set info connection
//        tvNoConnection.setVisibility(JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()? View.GONE : View.VISIBLE);

        createCheckConnectionService();
    }

    public void createCheckConnectionService() {
        checkConnectionBroadCast = new CheckConnectionBroadCast(tvNoConnection);
        registerReceiver(checkConnectionBroadCast, new IntentFilter(JointlyApplication.CHECK_CONNECTION_BROADCAST));
        intentCheckConnection = new Intent(JointlyApplication.getContext(), CheckConnectionService.class);
        startService(intentCheckConnection);
    }

    public void stopCheckConnectionService() {
        stopService(intentCheckConnection);
        checkConnectionBroadCast = null;
        intentCheckConnection = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopCheckConnectionService();
    }
}