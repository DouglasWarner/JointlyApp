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

/**
 * La ventana de mis iniciativas
 */
public class InitiativeFragment extends Fragment implements InitiativeContract.View, InitiativeAdapter.ManageInitiative {

    //region Variables
    private static final String TYPE_CREATED_INPROGRESS = "createdInProgress";
    private static final String TYPE_CREATED_HISTORY = "createdHistory";
    private static final String TYPE_JOINED_INPROGRESS = "joinedInProgress";
    private static final String TYPE_JOINED_HISTORY = "joinedHistory";

    private LinearLayout llLoading;
    private LinearLayout llNoData;
    private RecyclerView rvInitiativeCreatedInProgress;
    private RecyclerView rvInitiativeCreatedHistory;
    private RecyclerView rvInitiativeJoindedInProgress;
    private RecyclerView rvInitiativeJoindedHistory;
    private InitiativeAdapter adapterInitiativeCreatedInProgress;
    private InitiativeAdapter adapterInitiativeCreatedHistory;
    private InitiativeAdapter adapterInitiativeJoinedInProgress;
    private InitiativeAdapter adapterInitiativeJoinedHistory;

    private LinearLayout llInitiativeCreated;
    private LinearLayout llInitiativeJoined;
    private TextView tvCreated;
    private TextView tvJoined;

    private FloatingActionButton floatingActionButton;

    private InitiativeContract.Presenter presenter;
    //endregion

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_initiative, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);
        initRecyclers();

        presenter = new InitiativePresenter(this);

        setOnClickUI();
    }

    private void initUI(@NonNull View view) {
        llLoading = view.findViewById(R.id.llLoading);
        llNoData = view.findViewById(R.id.llNoDataInitiativeCreated);
        rvInitiativeCreatedInProgress = view.findViewById(R.id.rvInitiativeCreatedInProgress);
        floatingActionButton = getActivity().findViewById(R.id.faButton);

        rvInitiativeCreatedHistory = view.findViewById(R.id.rvInitiativeCreatedHistory);
        rvInitiativeJoindedInProgress = view.findViewById(R.id.rvInitiativeJoinedInProgress);
        rvInitiativeJoindedHistory = view.findViewById(R.id.rvInitiativeJoinedHistory);
        llInitiativeCreated = view.findViewById(R.id.llInitiativeCreated);
        llInitiativeJoined = view.findViewById(R.id.llInitiativeJoined);

        tvCreated = view.findViewById(R.id.tvCreated);
        tvJoined = view.findViewById(R.id.tvJoined);
    }

    private void initRecyclers() {
        adapterInitiativeCreatedInProgress = new InitiativeAdapter(new ArrayList<>(), this, TYPE_CREATED_INPROGRESS);
        adapterInitiativeCreatedHistory  = new InitiativeAdapter(new ArrayList<>(), this, TYPE_CREATED_HISTORY);
        adapterInitiativeJoinedInProgress = new InitiativeAdapter(new ArrayList<>(), this, TYPE_JOINED_INPROGRESS);
        adapterInitiativeJoinedHistory = new InitiativeAdapter(new ArrayList<>(), this, TYPE_JOINED_HISTORY);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rvInitiativeCreatedInProgress.setLayoutManager(layoutManager);
        rvInitiativeCreatedInProgress.setAdapter(adapterInitiativeCreatedInProgress);

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rvInitiativeCreatedHistory.setLayoutManager(layoutManager1);
        rvInitiativeCreatedHistory.setAdapter(adapterInitiativeCreatedHistory);

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rvInitiativeJoindedInProgress.setLayoutManager(layoutManager2);
        rvInitiativeJoindedInProgress.setAdapter(adapterInitiativeJoinedInProgress);

        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rvInitiativeJoindedHistory.setLayoutManager(layoutManager3);
        rvInitiativeJoindedHistory.setAdapter(adapterInitiativeJoinedHistory);
    }

    private void setOnClickUI() {
        tvCreated.setOnClickListener(v -> {
            showInitiativeCreated();
        });

        tvJoined.setOnClickListener(v -> {
            showInitiativeJoined();
        });

        floatingActionButton.setOnClickListener(v -> {
            goToAddInitiative();
        });
    }

    /**
     * Metodo que abre y cierra el desplegable de la lista de creados
     */
    public void showInitiativeCreated()
    {
        if ((llInitiativeCreated.getVisibility() == View.GONE)) {
            llInitiativeCreated.setVisibility(View.VISIBLE);
            tvCreated.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_arrow_up,0);
        } else {
            llInitiativeCreated.setVisibility(View.GONE);
            tvCreated.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_arrow_down,0);
        }
    }

    /**
     * Metodo que abre y cierra el desplegable de la lista de unidos
     */
    public void showInitiativeJoined()
    {
        if ((llInitiativeJoined.getVisibility() == View.GONE)) {
            llInitiativeJoined.setVisibility(View.VISIBLE);
            tvJoined.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_arrow_up,0);
        } else {
            llInitiativeJoined.setVisibility(View.GONE);
            tvJoined.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down,0);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.loadCreatedInProgress();
        presenter.loadCreatedHistory();
        presenter.loadJoinedInProgress();
        presenter.loadJoinedHistory();
    }

    private void hideFloatingButton()
    {
        floatingActionButton.setVisibility(View.GONE);
    }

    @Override
    public void setNoData() {
//        llNoData.setVisibility(View.VISIBLE);
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
    public void onSuccessCreatedInProgress(List<Initiative> list) {
        if(llNoData.getVisibility() == View.VISIBLE)
            llNoData.setVisibility(View.GONE);

        adapterInitiativeCreatedInProgress.update(list);
    }

    @Override
    public void onSuccessJoinedInProgress(List<Initiative> list) {
        if(llNoData.getVisibility() == View.VISIBLE)
            llNoData.setVisibility(View.GONE);

        adapterInitiativeJoinedInProgress.update(list);
    }

    @Override
    public void onSuccessCreatedHistory(List<Initiative> list) {
        if(llNoData.getVisibility() == View.VISIBLE)
            llNoData.setVisibility(View.GONE);

        adapterInitiativeCreatedHistory.update(list);
    }

    @Override
    public void onSuccessJoinedHistory(List<Initiative> list) {
        if(llNoData.getVisibility() == View.VISIBLE)
            llNoData.setVisibility(View.GONE);

        adapterInitiativeJoinedHistory.update(list);
    }

    @Override
    public void onClick(View item, String type) {

        Initiative initiative;
        Bundle bundle;

        switch (type)
        {
            case TYPE_CREATED_INPROGRESS:
                initiative = adapterInitiativeCreatedInProgress.getInitiativeItem(rvInitiativeCreatedInProgress.getChildAdapterPosition(item));

                bundle = new Bundle();
                bundle.putInt(Initiative.TAG, initiative.getId());

                goToEditInitiative(bundle);
                break;
            case TYPE_CREATED_HISTORY:
                initiative = adapterInitiativeCreatedHistory.getInitiativeItem(rvInitiativeCreatedHistory.getChildAdapterPosition(item));

                bundle = new Bundle();
                bundle.putInt(Initiative.TAG, initiative.getId());
                bundle.putBoolean("history",true);

                hideFloatingButton();
                goToShowInitiative(bundle);
                break;
            case TYPE_JOINED_INPROGRESS:
                initiative = adapterInitiativeJoinedInProgress.getInitiativeItem(rvInitiativeJoindedInProgress.getChildAdapterPosition(item));

                bundle = new Bundle();
                bundle.putInt(Initiative.TAG, initiative.getId());

                hideFloatingButton();
                goToShowInitiative(bundle);
                break;
            case TYPE_JOINED_HISTORY:
                initiative = adapterInitiativeJoinedHistory.getInitiativeItem(rvInitiativeJoindedHistory.getChildAdapterPosition(item));

                bundle = new Bundle();
                bundle.putInt(Initiative.TAG, initiative.getId());
                bundle.putBoolean("history",true);

                hideFloatingButton();
                goToShowInitiative(bundle);
                break;
            default:
                break;
        }

    }

    private void goToAddInitiative() {
        NavHostFragment.findNavController(this).navigate(R.id.action_initiativeFragment_to_manageInitiativeFragment, new Bundle());
    }

    private void goToEditInitiative(Bundle bundle) {
        NavHostFragment.findNavController(this).navigate(R.id.action_initiativeFragment_to_manageInitiativeFragment, bundle);
    }

    private void goToShowInitiative(Bundle bundle) {
        NavHostFragment.findNavController(this).navigate(R.id.action_initiativeFragment_to_showInitiativeFragment, bundle);
    }
}