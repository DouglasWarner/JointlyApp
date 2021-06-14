package com.douglas.jointlyapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.HomeListAdapter;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.ui.adapter.HomeAdapter;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

//TODO Quitar los iniciativas creadas por ti

/**
 * Fragment Home that represents the main view of the app
 */
public class HomeFragment extends Fragment implements HomeContract.View, HomeAdapter.ManageInitiative, SearchView.OnQueryTextListener {

    //region Variables

    private LinearLayout llLoading;
    private LinearLayout llNoData;
    private RecyclerView rvInitiative;
    private HomeAdapter adapter;
    private HomePresenter presenter;
    private Initiative initiative;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvNoConnection;

    //endregion

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

        swipeRefreshLayout.setOnRefreshListener(() -> {
            Thread sync = new Thread(() -> {
                presenter.syncData();

                getActivity().runOnUiThread(() -> {
                    presenter.load();
                    swipeRefreshLayout.setRefreshing(false);
                });
            });
            sync.start();
        });

        presenter = new HomePresenter(this);
    }

    /**
     *
     * @param view
     */
    private void initUI(@NonNull View view) {
        llLoading = view.findViewById(R.id.llLoading);
        llNoData = view.findViewById(R.id.llNoDataCreatedInProgress);
        rvInitiative = view.findViewById(R.id.rvInitiativeCreated);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        tvNoConnection = getActivity().findViewById(R.id.tvNoConnection);
    }

    /**
     * initRecycler
     */
    private void initRecycler() {
        adapter = new HomeAdapter(new ArrayList<>(),this);
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
    public void onSuccess(List<HomeListAdapter> homeListAdapters) {
        if(llNoData.getVisibility() == View.VISIBLE)
            llNoData.setVisibility(View.GONE);

//        tvNoConnection.setVisibility(JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized() ? View.GONE : View.VISIBLE);

        updateListOrderByDefault(homeListAdapters);
    }

    @Override
    public void showOnError(String message) {
        Snackbar.make(getActivity().findViewById(R.id.coordinator_main), message != null ? message : getString(R.string.default_error_action), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSync() {
        swipeRefreshLayout.setRefreshing(!swipeRefreshLayout.isRefreshing());
    }

    /**
     * update the recycler with default order
     * @param homeListAdapters
     */
    private void updateListOrderByDefault(List<HomeListAdapter> homeListAdapters) {
        switch (JointlyPreferences.getInstance().getOrderByInitiative()) {
            case "date":
                adapter.sortByDate();
                break;
            case "location":
                adapter.sortByLocation();
                break;
            case "users":
                adapter.sortByUsersJoineds();
                break;
            default:
                adapter.update(homeListAdapters);
                break;
        }
    }

    @Override
    public void onClick(View initiative) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Initiative.TAG, adapter.getInitiativeItem(rvInitiative.getChildAdapterPosition(initiative)).getInitiative());
        NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_showInitiativeFragment, bundle);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.setGroupVisible(R.id.group_action_order_initiative, true);
        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        searchViewItem.setVisible(true);
        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                menu.setGroupVisible(R.id.group_action_order_initiative, false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                menu.setGroupVisible(R.id.group_action_order_initiative, true);
                return true;
            }
        };

        searchViewItem.setOnActionExpandListener(onActionExpandListener);
        SearchView searchView = (SearchView) searchViewItem.getActionView();
        //TODO comprobar el ancho y el porque de este listener
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.e("TAG", searchViewItem.isVisible() +"");
                return true;
            }
        });
        searchView.setQueryHint(getString(R.string.action_search_initiative));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
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

    @Override
    public boolean onQueryTextSubmit(String query) {
//        searchView.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}