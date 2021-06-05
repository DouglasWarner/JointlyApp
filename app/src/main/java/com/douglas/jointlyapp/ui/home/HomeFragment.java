package com.douglas.jointlyapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.adapter.HomeAdapter;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

//TODO Quitar los iniciativas creadas por ti

/**
 * La ventana home con todas la iniciativas
 */
public class HomeFragment extends Fragment implements HomeContract.View, HomeAdapter.ManageInitiative {

    private LinearLayout llLoading;
    private LinearLayout llNoData;
    private RecyclerView rvInitiative;
    private HomeAdapter adapter;
    private HomePresenter presenter;
    private Initiative initiative;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvNoConnection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);

        initRecycler();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Thread sync = new Thread(() -> {
                    presenter.syncData();

                    getActivity().runOnUiThread(() -> {
                        presenter.load();
                        swipeRefreshLayout.setRefreshing(false);
                    });
                });
                sync.start();
            }
        });

        presenter = new HomePresenter(this);
    }

    private void initUI(@NonNull View view) {
        llLoading = view.findViewById(R.id.llLoading);
        llNoData = view.findViewById(R.id.llNoDataCreatedInProgress);
        rvInitiative = view.findViewById(R.id.rvInitiativeCreated);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        tvNoConnection = getActivity().findViewById(R.id.tvNoConnection);
    }

    private void initRecycler() {
        adapter = new HomeAdapter(new ArrayList<>(), new ArrayList<>(), this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvInitiative.setLayoutManager(layoutManager);
        rvInitiative.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.load();
    }

    @Override
    public void setNoData() {
        llNoData.setVisibility(View.VISIBLE);
        tvNoConnection.setVisibility(JointlyApplication.getConnection() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showProgress() {
        llLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        llLoading.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(List<Initiative> list, List<User> userOwners) {
        if(llNoData.getVisibility() == View.VISIBLE)
            llNoData.setVisibility(View.GONE);

        tvNoConnection.setVisibility(JointlyApplication.getConnection() ? View.GONE : View.VISIBLE);

        adapter.update(list, userOwners);
        updateListOrderByDefault();
    }

    @Override
    public void showOnError(String message) {
        Snackbar.make(getView(), message != null ? message : getString(R.string.default_error), Snackbar.LENGTH_SHORT).show();
    }

    private void updateListOrderByDefault() {
        switch (JointlyPreferences.getInstance().getOrderByFavorite())
        {
            case "date":
                adapter.sortByDate();
                break;
            case "location":
                adapter.sortByLocation();
                break;
            case "users":
                adapter.sortByUsersJoineds();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }

    @Override
    public void onClick(View initiative) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Initiative.TAG, adapter.getInitiativeItem(rvInitiative.getChildAdapterPosition(initiative)));
        NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_showInitiativeFragment, bundle);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.setGroupVisible(R.id.group_action_order_initiative, true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_order_initiative_by_date:
                adapter.sortByDate();
                break;
            case R.id.action_order_initiative_by_location:
                adapter.sortByLocation();
                break;
            case R.id.action_order_initiative_by_users_joined:
                adapter.sortByUsersJoineds();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}