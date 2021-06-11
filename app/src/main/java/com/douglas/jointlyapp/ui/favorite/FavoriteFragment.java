package com.douglas.jointlyapp.ui.favorite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.adapter.UserFavoriteAdapter;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that represents the list of users followed
 */
public class FavoriteFragment extends Fragment implements FavoriteContract.View, UserFavoriteAdapter.ManageUserFavorite {

    //region Variables

    private LinearLayout llLoading;
    private LinearLayout llNoData;
    private RecyclerView rvUserFavorite;
    private UserFavoriteAdapter adapter;

    private FavoritePresenter presenter;

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
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);

        initRecycler();

        presenter = new FavoritePresenter(this);
    }

    private void initUI(@NonNull View view) {
        llLoading = view.findViewById(R.id.llLoading);
        llNoData = view.findViewById(R.id.llNoDataCreatedInProgress);

        rvUserFavorite = view.findViewById(R.id.rvUserFavorite);
    }

    private void initRecycler() {
        adapter = new UserFavoriteAdapter(new ArrayList<>(), this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvUserFavorite.setLayoutManager(layoutManager);
        rvUserFavorite.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.load();
        getActivity().findViewById(R.id.faButton).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        Bundle b = new Bundle();
        b.putSerializable(User.TAG, adapter.getUserItem(rvUserFavorite.getChildAdapterPosition(v)));

        NavHostFragment.findNavController(this).navigate(R.id.action_favoriteFragment_to_userProfileFragment, b);
    }

    @Override
    public void onClickBtnFollow(View user) {
        presenter.followUser((User)adapter.getUserItem(rvUserFavorite.getChildAdapterPosition(user)));
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
    public void setSuccessUnFollow() {
        adapter.updateUnFollow(true);
    }

    @Override
    public void setSuccessFollow() {
        adapter.updateUnFollow(false);
    }

    @Override
    public void onSuccess(List<User> list) {
        if(llNoData.getVisibility() == View.VISIBLE)
            llNoData.setVisibility(View.GONE);

        adapter.update(list);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.setGroupVisible(R.id.group_action_order_favorite, true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_order_favorite_by_location:
                adapter.sortByLocation();
                break;
            case R.id.action_order_favorite_by_name:
                adapter.sortByName();
                break;
            case R.id.action_order_favorite_by_users_follows:
                adapter.sortByUsersFollows();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}