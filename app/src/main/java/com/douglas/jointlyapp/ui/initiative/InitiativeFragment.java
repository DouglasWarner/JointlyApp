package com.douglas.jointlyapp.ui.initiative;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.ui.adapter.InitiativeAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class InitiativeFragment extends Fragment implements InitiativeContract.View, InitiativeAdapter.ManageInitiative {

    private LinearLayout llLoading;
    private LinearLayout llNoData;
    private RecyclerView rvInitiativeCreatedInProgress;
    private RecyclerView rvInitiativeCreatedHistory;
    private RecyclerView rvInitiativeJoindedInProgress;
    private RecyclerView rvInitiativeJoindedHistory;
    private InitiativeAdapter adapter;
    private InitiativePresenter presenter;
    private Initiative initiative;
    private LinearLayout llInitiativeCreated;
    private LinearLayout llInitiativeJoined;
    private TextView tvCreated;
    private TextView tvJoined;
    private FloatingActionButton floatingActionButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_initiative, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        llLoading = view.findViewById(R.id.llLoading);
        llNoData = view.findViewById(R.id.llNoDataInitiativeCreated);
        rvInitiativeCreatedInProgress = view.findViewById(R.id.rvInitiativeCreatedInProgress);
        floatingActionButton = getActivity().findViewById(R.id.faButton);

//        rvInitiativeCreatedHistory = view.findViewById(R.id.rvInitiativeCreatedHistory);
//        rvInitiativeJoindedInProgress = view.findViewById(R.id.rvInitiativeJoinedInProgress);
//        rvInitiativeJoindedHistory = view.findViewById(R.id.rvInitiativeJoinedHistory);
//        llInitiativeCreated = view.findViewById(R.id.llInitiativeCreated);
//        llInitiativeJoined = view.findViewById(R.id.llInitiativeJoined);

//        tvCreated = view.findViewById(R.id.tvCreated);
//        tvJoined = view.findViewById(R.id.tvJoined);

        adapter = new InitiativeAdapter(new ArrayList<>(), this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rvInitiativeCreatedInProgress.setLayoutManager(layoutManager);
        rvInitiativeCreatedInProgress.setAdapter(adapter);

//        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
//        rvInitiativeCreatedHistory.setLayoutManager(layoutManager1);
//        rvInitiativeCreatedHistory.setAdapter(adapter);
//
//        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
//        rvInitiativeJoindedInProgress.setLayoutManager(layoutManager2);
//        rvInitiativeJoindedInProgress.setAdapter(adapter);
//
//        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
//        rvInitiativeJoindedHistory.setLayoutManager(layoutManager3);
//        rvInitiativeJoindedHistory.setAdapter(adapter);

        presenter = new InitiativePresenter(this);

//        tvCreated.setOnClickListener(v -> {
//            showInitiativeCreated();
//        });
//
//        tvJoined.setOnClickListener(v -> {
//            showInitiativeJoined();
//        });

        floatingActionButton.setOnClickListener(v -> {
            goToAddInitiative();
        });

    }

    private void goToAddInitiative() {
        NavHostFragment.findNavController(this).navigate(R.id.action_initiativeFragment_to_manageInitiativeFragment, new Bundle());
    }

//    public void showInitiativeCreated()
//    {
//        if ((llInitiativeCreated.getVisibility() == View.GONE)) {
//            llInitiativeCreated.setVisibility(View.VISIBLE);
//        } else {
//            llInitiativeCreated.setVisibility(View.GONE);
//        }
//    }
//
//    public void showInitiativeJoined()
//    {
//        if ((llInitiativeJoined.getVisibility() == View.GONE)) {
//            llInitiativeJoined.setVisibility(View.VISIBLE);
//        } else {
//            llInitiativeJoined.setVisibility(View.GONE);
//        }
//    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.load();
        manageFloatingButton();
    }

    private void manageFloatingButton() {
        floatingActionButton.setImageResource(R.drawable.ic_add);
        floatingActionButton.setVisibility(View.VISIBLE);
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
    public void onSuccess(List<Initiative> list) {
        if(llNoData.getVisibility() == View.VISIBLE)
            llNoData.setVisibility(View.GONE);

        adapter.update(list);
    }

    @Override
    public void onClick(View initiative) {
        //TODO navegar a ver/editar mi iniciativa
        Bundle bundle = new Bundle();
        bundle.putSerializable("initiative", (Initiative)adapter.getInitiativeItem(rvInitiativeCreatedInProgress.getChildAdapterPosition(initiative)));

        goToEditInitiative(bundle);
    }

    private void goToEditInitiative(Bundle bundle) {
        NavHostFragment.findNavController(this).navigate(R.id.action_initiativeFragment_to_manageInitiativeFragment, bundle);
    }
}