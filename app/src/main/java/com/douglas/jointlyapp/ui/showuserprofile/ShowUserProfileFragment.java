package com.douglas.jointlyapp.ui.showuserprofile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static com.douglas.jointlyapp.ui.initiative.InitiativeFragment.TYPE_CREATED;
import static com.douglas.jointlyapp.ui.initiative.InitiativeFragment.TYPE_JOINED;

/**
 * La ventana de otro usuario
 */
public class ShowUserProfileFragment extends Fragment implements InitiativeAdapter.ManageInitiative, ShowUserProfileContract.View {

    //region Variables

    private ShapeableImageView imgUser;
    private ImageView ivEditImagenUser;
    private TextView tvUserName;
    private TextView tvUserLocation;
    private TextView tvUserEmail;
    private TextView tvUserPhome;
    private TextView tvUserInitiativeCreateds;
    private TextView tvUserInitiativeJoineds;
    private TextView tvUserFollows;
    private TextView tvUserDescription;
    private TextView tvUserCreatedAt;

    private TextView tvNoInitiativeCreatedUserData;
    private TextView tvNoInitiativeJoinedUserData;
    private RecyclerView rvInitiativeCreateds;
    private InitiativeAdapter adapterCreateds;
    private RecyclerView rvInitiativeJoineds;
    private InitiativeAdapter adapterJoineds;

    private User user;
    private Menu menu;

    private ShowUserProfileContract.Presenter presenter;

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
        user = (bundle != null) ? (User) bundle.getSerializable(User.TAG) : null;

        initUI(view);
        initRecycler();

        presenter = new ShowUserProfilePresenter(this);
    }

    /**
     *
     * @param view
     */
    private void initUI(@NonNull View view) {
        imgUser = view.findViewById(R.id.imgUser);
        ivEditImagenUser = view.findViewById(R.id.ivEditImageUser);
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
        rvInitiativeJoineds = view.findViewById(R.id.rvUserInitiativeJoined);
        tvNoInitiativeCreatedUserData = view.findViewById(R.id.tvNoInitiativeCreatedUserData);
        tvNoInitiativeJoinedUserData = view.findViewById(R.id.tvNoInitiativeJoinedUserData);
    }

    /**
     *
     */
    private void initRecycler() {
        adapterCreateds = new InitiativeAdapter(getContext(), new ArrayList<>(), this, TYPE_CREATED);
        adapterJoineds = new InitiativeAdapter(getContext(), new ArrayList<>(), this, TYPE_JOINED);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);

        rvInitiativeCreateds.setLayoutManager(layoutManager);
        rvInitiativeCreateds.setAdapter(adapterCreateds);
        rvInitiativeJoineds.setLayoutManager(layoutManager);
        rvInitiativeJoineds.setAdapter(adapterJoineds);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.loadCountUserFollow(user);
        presenter.loadListInitiative(user);
        presenter.loadUserStateFollow(user);
        getActivity().findViewById(R.id.faButton).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view, String type) {
        Bundle bundle = new Bundle();
        Initiative initiative;

        //TODO mirar
        switch (type) {
            case TYPE_CREATED:
                initiative = (Initiative) adapterCreateds.getInitiativeItem(rvInitiativeCreateds.getChildAdapterPosition(view));

                bundle = new Bundle();
                bundle.putSerializable(Initiative.TAG, initiative);
                bundle.putBoolean(TYPE_CREATED, true);

                NavHostFragment.findNavController(this).navigate(R.id.action_userProfileFragment_to_showInitiativeFragment2, bundle);
                break;
            case TYPE_JOINED:
                initiative = (Initiative) adapterCreateds.getInitiativeItem(rvInitiativeJoineds.getChildAdapterPosition(view));

                bundle = new Bundle();
                bundle.putSerializable(Initiative.TAG, initiative);
                bundle.putBoolean(TYPE_JOINED, true);

                NavHostFragment.findNavController(this).navigate(R.id.action_userProfileFragment_to_showInitiativeFragment2, bundle);
                break;
            default:
                break;
        }
    }

    @Override
    public void setInitiativeCreatedEmpty() {
        tvNoInitiativeCreatedUserData.setVisibility(View.VISIBLE);
    }

    @Override
    public void setInitiativeJointedEmpty() {
        tvNoInitiativeJoinedUserData.setVisibility(View.VISIBLE);
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
    public void setUserStateFollow(boolean follow) {
        menu.getItem(0).setIcon(follow ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
    }

    @Override
    public void setCountUserFollow(long count) {
        tvUserFollows.setText(String.format(getString(R.string.tvUserProfileFollowsFormat), count));
    }

    @Override
    public void setCountUserParticipate(long count) {
        tvUserInitiativeJoineds.setText(String.valueOf(count));
    }

    @Override
    public void onSuccess(List<Initiative> listInitiativesCreated, List<Initiative> listInitiativesJoined) {
        if(tvNoInitiativeCreatedUserData.getVisibility() == View.VISIBLE)
            tvNoInitiativeCreatedUserData.setVisibility(View.GONE);
        if(tvNoInitiativeJoinedUserData.getVisibility() == View.VISIBLE)
            tvNoInitiativeJoinedUserData.setVisibility(View.GONE);

        tvUserInitiativeCreateds.setText(String.valueOf(listInitiativesCreated.size()));

        adapterCreateds.update(listInitiativesCreated);
        adapterJoineds.update(listInitiativesJoined);
    }

    @Override
    public void onError(String message) {
        Snackbar.make(getActivity().findViewById(R.id.coordinator_main), message != null ? message : getString(R.string.default_error_action), Snackbar.LENGTH_SHORT).show();
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
        menu.add(0, 1, 0, "Favoritos").setIcon(R.drawable.ic_favorite_border).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                presenter.manageFollowUser(user);
                return true;
            }
        });

        this.menu = menu;
    }
}