package com.douglas.jointlyapp.ui.infouser;

import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static com.douglas.jointlyapp.ui.initiative.InitiativeFragment.TYPE_CREATED;
import static com.douglas.jointlyapp.ui.initiative.InitiativeFragment.TYPE_JOINED;

/**
 * Fragment that shows the current info on ViewPager2 from UserProfile
 */
public class InfoUserFragment extends Fragment implements InfoUserContract.View, InitiativeAdapter.ManageInitiative {

    //region Variables

    private User user;

    private TextView tvUserFollows;
    private TextView tvUserDescription;
    private TextView tvUserCreatedAt;
    private TextView tvUserInitiativeCreateds;
    private TextView tvUserInitiativeJoineds;

    private TextView tvNoInitiativeCreatedUserData;
    private TextView tvNoInitiativeJoinedUserData;
    private RecyclerView rvInitiativeCreateds;
    private InitiativeAdapter adapterCreateds;
    private RecyclerView rvInitiativeJoineds;
    private InitiativeAdapter adapterJoineds;

    private InfoUserContract.Presenter presenter;

    //endregion

    /**
     * Create a new InfoUserFragment
     * @param user
     */
    public InfoUserFragment(User user) {
        this.user = user;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);
        initRecycler();
        setUser(user);

        // set visibility
        if(JointlyPreferences.getInstance().getUser().equals(user.getEmail())){
            view.findViewById(R.id.linearLayoutInitiatives).setVisibility(View.GONE);
            view.findViewById(R.id.divider4).setVisibility(View.GONE);
        }

        presenter = new InfoUserPresenter(this);
    }

    /**
     *
     * @param view
     */
    private void initUI(@NonNull View view) {
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
     * initRecycler
     */
    private void initRecycler() {
        adapterCreateds = new InitiativeAdapter(getContext(), new ArrayList<>(), this, TYPE_CREATED);
        adapterJoineds = new InitiativeAdapter(getContext(), new ArrayList<>(), this, TYPE_JOINED);

        RecyclerView.LayoutManager lmInitiativeCreateds = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        RecyclerView.LayoutManager lmInitiativeJoineds = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);

        rvInitiativeCreateds.setLayoutManager(lmInitiativeCreateds);
        rvInitiativeCreateds.setAdapter(adapterCreateds);
        rvInitiativeJoineds.setLayoutManager(lmInitiativeJoineds);
        rvInitiativeJoineds.setAdapter(adapterJoineds);
    }

    private void initListener() {

    }

    //TODO probar

    /**
     * load data
     */
    public void load() {
        presenter.loadCountParticipate(user);
        presenter.loadCountUserFollow(user);
        presenter.loadListInitiative(user);
    }

    /**
     * setUser
     * @param user
     */
    private void setUser(User user) {
        tvUserDescription.setText(user.getDescription());
        tvUserCreatedAt.setText(user.getCreated_at());
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.loadCountUserFollow(user);
        presenter.loadListInitiative(user);
        presenter.loadCountParticipate(user);
        getActivity().findViewById(R.id.faButton).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view, String type) {
        Bundle bundle;
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
                initiative = (Initiative) adapterJoineds.getInitiativeItem(rvInitiativeJoineds.getChildAdapterPosition(view));

                bundle = new Bundle();
                bundle.putSerializable(Initiative.TAG, initiative);
                bundle.putBoolean(TYPE_JOINED, true);

                NavHostFragment.findNavController(this).navigate(R.id.action_userProfileFragment_to_showInitiativeFragment2, bundle);
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
    public void setCountUserFollow(long count) {
        tvUserFollows.setText(String.format(getString(R.string.tvUserProfileFollowsFormat), count));
    }

    @Override
    public void setCountUserParticipate(long count) {
        tvUserInitiativeJoineds.setText(String.valueOf(count));
    }

    @Override
    public void onSuccessListCreated(List<Initiative> listInitiativesCreated) {
        if(tvNoInitiativeCreatedUserData.getVisibility() == View.VISIBLE)
            tvNoInitiativeCreatedUserData.setVisibility(View.GONE);

        tvUserInitiativeCreateds.setText(String.valueOf(listInitiativesCreated.size()));

        adapterCreateds.update(listInitiativesCreated);
    }

    @Override
    public void onSuccessListJoined(List<Initiative> listInitiativesJoined) {
        if(tvNoInitiativeJoinedUserData.getVisibility() == View.VISIBLE)
            tvNoInitiativeJoinedUserData.setVisibility(View.GONE);

        adapterJoineds.update(listInitiativesJoined);
    }

    @Override
    public void onError(String message) {
        Snackbar.make(getView(), message != null ? message : getString(R.string.default_error_action), Snackbar.LENGTH_SHORT).show();
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