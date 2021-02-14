package com.douglas.jointlyapp.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.adapter.InitiativeAdapter;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

//TODO implementar MVP
public class ProfileFragment extends Fragment implements InitiativeAdapter.ManageInitiative {

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

    private RecyclerView rvInitiativeCreateds;
    private InitiativeAdapter adapter;

    private boolean isViewingUser;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        if(bundle != null) {
            user = (User) bundle.getSerializable("user");
            if(user == null)
                isViewingUser = false;
            else
                isViewingUser = true;
        }

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

        if(isViewingUser) {
            imgUser.setImageResource(R.mipmap.ic_app);
            tvUserName.setText(user.getName());
            tvUserLocation.setText(user.getLocation());
            tvUserEmail.setText(user.getEmail());
            tvUserPhome.setText(user.getPhone());
            tvUserInitiativeCreateds.setText("20");
            tvUserInitiativeJoineds.setText("20");
            tvUserFollows.setText(String.format("%d siguen a este usuario", user.getUserFollowed().size()));
            tvUserDescription.setText(user.getDescription());
            tvUserCreatedAt.setText(user.getCreatedAt());
            adapter = new InitiativeAdapter(new ArrayList<>(), this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
            rvInitiativeCreateds.setLayoutManager(layoutManager);
            rvInitiativeCreateds.setAdapter(adapter);
        }
        else
        {
            view.findViewById(R.id.layoutInitiativeCreateds).setVisibility(View.GONE);
            view.findViewById(R.id.divider4).setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().findViewById(R.id.faButton).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View initiative) {
        //TODO implementar ir a ver iniciativa
        Bundle b = new Bundle();
        Initiative i = (Initiative)adapter.getInitiativeItem(rvInitiativeCreateds.getChildAdapterPosition(initiative));
        b.putSerializable("initiative", i);

        NavHostFragment.findNavController(this).navigate(R.id.action_profileFragment_to_showInitiativeFragment, b);
    }
}