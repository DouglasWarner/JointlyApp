package com.douglas.jointlyapp.ui.home;

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

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.ui.adapter.HomeAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeContract.View, HomeAdapter.ManageInitiative{

    private LinearLayout llLoading;
    private LinearLayout llNoData;
    private RecyclerView rvInitiative;
    private HomeAdapter adapter;
    private HomePresenter presenter;
    private Initiative initiative;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        llLoading = view.findViewById(R.id.llLoading);
        llNoData = view.findViewById(R.id.llNoDataInitiativeCreated);
        rvInitiative = view.findViewById(R.id.rvInitiativeCreated);

        adapter = new HomeAdapter(new ArrayList<>(), this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvInitiative.setLayoutManager(layoutManager);
        rvInitiative.setAdapter(adapter);

        presenter = new HomePresenter(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.load();
        getActivity().findViewById(R.id.faButton).setVisibility(View.GONE);
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
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }

    @Override
    public void onClick(View initiative) {
        //TODO navegar a ver iniciativa donde puedo unirme y acceder al chat
        Bundle bundle = new Bundle();
        bundle.putSerializable("initiative", adapter.getInitiativeItem(rvInitiative.getChildAdapterPosition(initiative)));
        NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_showInitiativeFragment, bundle);
    }
}