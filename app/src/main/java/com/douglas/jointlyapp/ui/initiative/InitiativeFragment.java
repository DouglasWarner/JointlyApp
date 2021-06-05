package com.douglas.jointlyapp.ui.initiative;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    public static final String TYPE_INPROGRESS = "INPROGRESS";
    public static final String TYPE_HISTORY = "HISTORY";
    public static final String TYPE_CREATED = "CREATED";
    public static final String TYPE_JOINED = "JOINED";

    private LinearLayout llLoading;
    private LinearLayout llNoDataCreatedInProgress;
    private LinearLayout llNoDataCreatedHistory;
    private LinearLayout llNoDataJoinedInProgress;
    private LinearLayout llNoDataJoinedHistory;

//    ExpandableListView expandableListView;
//    HashMap<String, List<String>> listGroup;
//    HashMap<String, List<Initiative>>  listItem;
//    ExpandableListAdapter adapter;

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
//
//        expandableListView = view.findViewById(R.id.expListInitiativeCreated);
//        listGroup = new HashMap<>();
//        listItem = new HashMap<>();
//
//        listGroup.put(getString(R.string.created), Arrays.asList(getResources().getStringArray(R.array.item_type)));
//        listGroup.put(getString(R.string.joined), Arrays.asList(getResources().getStringArray(R.array.item_type)));
//        listItem.put(listGroup.get(listGroup.keySet().toArray()[0]).get(0), new ArrayList<>());
//        listItem.put(listGroup.get(listGroup.keySet().toArray()[0]).get(1), new ArrayList<>());
//        listItem.put(listGroup.get(listGroup.keySet().toArray()[1]).get(0), new ArrayList<>());
//        listItem.put(listGroup.get(listGroup.keySet().toArray()[1]).get(1), new ArrayList<>());
//
//        adapter = new ExpandableListAdapter(getContext(), listGroup, listItem);
//        expandableListView.setAdapter(adapter);

        presenter = new InitiativePresenter(this);

        setOnClickUI();
    }

    private void initUI(@NonNull View view) {
        llLoading = view.findViewById(R.id.llLoading);
        llNoDataCreatedInProgress = view.findViewById(R.id.llNoDataCreatedInProgress);
        llNoDataCreatedHistory = view.findViewById(R.id.llNoDataCreatedHistory);
        llNoDataJoinedInProgress = view.findViewById(R.id.llNoDataJoinedInProgress);
        llNoDataJoinedHistory = view.findViewById(R.id.llNoDataJoinedHistory);
        floatingActionButton = getActivity().findViewById(R.id.faButton);

        rvInitiativeCreatedInProgress = view.findViewById(R.id.rvInitiativeCreatedInProgress);
        rvInitiativeCreatedHistory = view.findViewById(R.id.rvInitiativeCreatedHistory);
        rvInitiativeJoindedInProgress = view.findViewById(R.id.rvInitiativeJoinedInProgress);
        rvInitiativeJoindedHistory = view.findViewById(R.id.rvInitiativeJoinedHistory);
        llInitiativeCreated = view.findViewById(R.id.llInitiativeCreated);
        llInitiativeJoined = view.findViewById(R.id.llInitiativeJoined);

        tvCreated = view.findViewById(R.id.tvCreated);
        tvJoined = view.findViewById(R.id.tvJoined);
    }

    private void initRecyclers() {
        adapterInitiativeCreatedInProgress = new InitiativeAdapter(getContext(), new ArrayList<>(), this, TYPE_CREATED + TYPE_INPROGRESS);
        adapterInitiativeCreatedHistory  = new InitiativeAdapter(getContext(), new ArrayList<>(), this, TYPE_CREATED + TYPE_HISTORY);
        adapterInitiativeJoinedInProgress = new InitiativeAdapter(getContext(), new ArrayList<>(), this, TYPE_JOINED + TYPE_INPROGRESS);
        adapterInitiativeJoinedHistory = new InitiativeAdapter(getContext(), new ArrayList<>(), this, TYPE_JOINED + TYPE_HISTORY);

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
        presenter.loadCreated(TYPE_INPROGRESS);
        presenter.loadCreated(TYPE_HISTORY);
        presenter.loadJoined(TYPE_INPROGRESS, 0);
        presenter.loadJoined(TYPE_HISTORY, 0);
    }

    private void hideFloatingButton()
    {
        floatingActionButton.setVisibility(View.GONE);
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
    public void onNoDataCreatedInProgress() {
        llNoDataCreatedInProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNoDataCreatedHistory() {
        llNoDataCreatedHistory.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNoDataJoinedInProgress() {
        llNoDataJoinedInProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNoDataJoinedHistory() {
        llNoDataJoinedHistory.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccessCreatedInProgress(List<Initiative> list) {
        if(llNoDataCreatedInProgress.getVisibility() == View.VISIBLE)
            llNoDataCreatedInProgress.setVisibility(View.GONE);

        adapterInitiativeCreatedInProgress.update(list);
    }

    @Override
    public void onSuccessCreatedHistory(List<Initiative> list) {
        if(llNoDataCreatedHistory.getVisibility() == View.VISIBLE)
            llNoDataCreatedHistory.setVisibility(View.GONE);

        adapterInitiativeCreatedHistory.update(list);
    }

    @Override
    public void onSuccessJoinedInProgress(List<Initiative> list) {
        if(llNoDataJoinedInProgress.getVisibility() == View.VISIBLE)
            llNoDataJoinedInProgress.setVisibility(View.GONE);

        adapterInitiativeJoinedInProgress.update(list);
    }

    @Override
    public void onSuccessJoinedHistory(List<Initiative> list) {
        if(llNoDataJoinedHistory.getVisibility() == View.VISIBLE)
            llNoDataJoinedHistory.setVisibility(View.GONE);

        adapterInitiativeJoinedHistory.update(list);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(getContext(), (message != null) ? message : "Algo salio mal", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View item, String type) {

        Initiative initiative;
        Bundle bundle;

        switch (type)
        {
            case TYPE_CREATED + TYPE_INPROGRESS:
                initiative = adapterInitiativeCreatedInProgress.getInitiativeItem(rvInitiativeCreatedInProgress.getChildAdapterPosition(item));

                bundle = new Bundle();
                bundle.putSerializable(Initiative.TAG, initiative);

                goToEditInitiative(bundle);
                break;
            case TYPE_CREATED + TYPE_HISTORY:
                initiative = adapterInitiativeCreatedHistory.getInitiativeItem(rvInitiativeCreatedHistory.getChildAdapterPosition(item));

                bundle = new Bundle();
                bundle.putSerializable(Initiative.TAG, initiative);
                bundle.putBoolean(TYPE_HISTORY,true);

                hideFloatingButton();
                goToShowInitiative(bundle);
                break;
            case TYPE_JOINED + TYPE_INPROGRESS:
                initiative = adapterInitiativeJoinedInProgress.getInitiativeItem(rvInitiativeJoindedInProgress.getChildAdapterPosition(item));

                bundle = new Bundle();
                bundle.putSerializable(Initiative.TAG, initiative);

                hideFloatingButton();
                goToShowInitiative(bundle);
                break;
            case TYPE_JOINED + TYPE_HISTORY:
                initiative = adapterInitiativeJoinedHistory.getInitiativeItem(rvInitiativeJoindedHistory.getChildAdapterPosition(item));

                bundle = new Bundle();
                bundle.putSerializable(Initiative.TAG, initiative);
                bundle.putBoolean(TYPE_HISTORY,true);

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