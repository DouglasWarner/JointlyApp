package com.douglas.jointlyapp.ui.showuserprofile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.adapter.InitiativeAdapter;
import com.google.android.material.imageview.ShapeableImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * La ventana de otro usuario
 */
public class ShowUserProfileFragment extends Fragment implements InitiativeAdapter.ManageInitiative, ShowUserProfileContract.View {

//region Variables
    private static final String TYPE_JOINED_INPROGRESS = "joinedInProgress";

    private ShapeableImageView imgUser;
    private TextView tvUserName;
    private TextView tvUserLocation;
    private TextView tvUserEmail;
    private TextView tvUserPhome;
    private TextView tvUserInitiativeCreateds;
    private TextView tvUserInitiativeJoineds;
    private TextView tvUserFollows;
    private TextView tvUserDescription;
    private TextView tvUserCreatedAt;

    private TextView tvNoInitiativeUserData;
    private RecyclerView rvInitiativeCreateds;
    private InitiativeAdapter adapter;

    private String user;

    private ShowUserProfileContract.Presenter presenter;

    private Menu menu;
    private int iconFavorite;
//endregion

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        user = bundle.getString(User.TAG);

        iconFavorite = R.drawable.ic_favorite_border;

        initUI(view);

        initRecycler();

        presenter = new ShowUserProfilePresenter(this);
    }

    private void initUI(@NonNull View view) {
        imgUser = view.findViewById(R.id.imgUser);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserLocation = view.findViewById(R.id.tvUserLocation);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        tvUserPhome = view.findViewById(R.id.tvUserPhone);
        tvUserInitiativeCreateds = view.findViewById(R.id.tvInitiativeCreatedCount);
        tvUserInitiativeJoineds = view.findViewById(R.id.tvInitiativeJoinedCount);
        tvUserFollows = view.findViewById(R.id.tvUserFollows);
        tvUserDescription = view.findViewById(R.id.tvUserDescription);
        tvUserCreatedAt = view.findViewById(R.id.tvUserCreatedAt);
        rvInitiativeCreateds = view.findViewById(R.id.rvUserInitiativeCreated);
        tvNoInitiativeUserData = view.findViewById(R.id.tvNoInitiativeUserData);
    }

    private void initRecycler() {
        adapter = new InitiativeAdapter(new ArrayList<>(), this, TYPE_JOINED_INPROGRESS);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rvInitiativeCreateds.setLayoutManager(layoutManager);
        rvInitiativeCreateds.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.loadUser(user);
        presenter.loadListInitiativeInProgress(user);
        getActivity().findViewById(R.id.faButton).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View initiative, String type) {
        Bundle b = new Bundle();
        Initiative i = (Initiative)adapter.getInitiativeItem(rvInitiativeCreateds.getChildAdapterPosition(initiative));
        b.putInt(Initiative.TAG, i.getId());

        NavHostFragment.findNavController(this).navigate(R.id.action_userProfileFragment_to_showInitiativeFragment2, b);
    }

    @Override
    public void setLocationEmpty() {
        tvUserLocation.setText("Ubicacion");
    }

    @Override
    public void setPhoneEmpty() {
        tvUserPhome.setText("Telefono");
    }

    @Override
    public void setDescriptionEmpty() {
        tvUserDescription.setText("Descripcion");
    }

    @Override
    public void setUserFollowersEmpty() {
        tvUserFollows.setText("Seguidores");
    }

    @Override
    public void setInitiativeCreatedEmpty() {
        tvUserInitiativeCreateds.setText("0");
    }

    @Override
    public void setInitiativeJointedEmpty() {
        tvUserInitiativeJoineds.setText("0");
    }

    @Override
    public void setInitiativeInProgressEmptyError() {
        tvNoInitiativeUserData.setVisibility(View.GONE);
    }

    @Override
    public void setSuccessUnFollow() {
        menu.getItem(0).setIcon(R.drawable.ic_favorite_border);
    }

    @Override
    public void setSuccessFollow() {
        menu.getItem(0).setIcon(R.drawable.ic_favorite);
    }

    @Override
    public void setUserFollowed() {
        iconFavorite = R.drawable.ic_favorite;
    }

    @Override
    public void onSuccess(User user, int countUserFollowers, int initiativeCreated, int initiativeJoined) {
        imgUser.setImageBitmap(user.getImagen());
        tvUserName.setText(user.getName());
        tvUserEmail.setText(user.getEmail());
        tvUserLocation.setText(user.getLocation());
        tvUserPhome.setText(user.getPhone());
        tvUserDescription.setText(user.getDescription());
        tvUserFollows.setText(String.format(getString(R.string.tvUserProfileFollowsFormat), countUserFollowers));
        tvUserInitiativeJoineds.setText(String.valueOf(initiativeJoined));
        tvUserInitiativeCreateds.setText(String.valueOf(initiativeCreated));
        tvUserCreatedAt.setText(user.getCreatedAt());
    }

    @Override
    public void onSuccess(List<Initiative> list) {
        if(tvNoInitiativeUserData.getVisibility() == View.VISIBLE)
            tvNoInitiativeUserData.setVisibility(View.GONE);

        adapter.update(list);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        menu.add(0, 1, 0, "Favoritos").setIcon(iconFavorite).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                presenter.followUser(user);
                return true;
            }
        });

        this.menu = menu;
    }
}