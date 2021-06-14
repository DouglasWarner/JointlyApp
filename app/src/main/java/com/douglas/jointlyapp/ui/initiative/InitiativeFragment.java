package com.douglas.jointlyapp.ui.initiative;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.adapter.InitiativeAdapter;
import com.douglas.jointlyapp.ui.utils.QRCaptureActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that represents the list of initiative created
 * and joined by user
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

    private View coordinatorLayout;
    private FloatingActionButton floatingActionButton;

    private TextView qrEscaneado;
    private InitiativeContract.Presenter presenter;

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
        return inflater.inflate(R.layout.fragment_initiative, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);
        initRecyclers();
        setOnClickUI();

        qrEscaneado = view.findViewById(R.id.qrEscaneado);

        presenter = new InitiativePresenter(this);
    }

    /**
     *
     * @param view
     */
    private void initUI(@NonNull View view) {
        llLoading = view.findViewById(R.id.llLoading);
        llNoDataCreatedInProgress = view.findViewById(R.id.llNoDataCreatedInProgress);
        llNoDataCreatedHistory = view.findViewById(R.id.llNoDataCreatedHistory);
        llNoDataJoinedInProgress = view.findViewById(R.id.llNoDataJoinedInProgress);
        llNoDataJoinedHistory = view.findViewById(R.id.llNoDataJoinedHistory);

        rvInitiativeCreatedInProgress = view.findViewById(R.id.rvInitiativeCreatedInProgress);
        rvInitiativeCreatedHistory = view.findViewById(R.id.rvInitiativeCreatedHistory);
        rvInitiativeJoindedInProgress = view.findViewById(R.id.rvInitiativeJoinedInProgress);
        rvInitiativeJoindedHistory = view.findViewById(R.id.rvInitiativeJoinedHistory);
        llInitiativeCreated = view.findViewById(R.id.llInitiativeCreated);
        llInitiativeJoined = view.findViewById(R.id.llInitiativeJoined);

        tvCreated = view.findViewById(R.id.tvCreated);
        tvJoined = view.findViewById(R.id.tvJoined);

        coordinatorLayout = getActivity().findViewById(R.id.coordinator_main);
        floatingActionButton = coordinatorLayout.findViewById(R.id.faButton);
    }

    /**
     * initRecyclers
     */
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

    /**
     * setOnClickUI
     */
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
    public void showInitiativeCreated() {
        if (llInitiativeCreated.getVisibility() == View.GONE) {
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
    public void showInitiativeJoined() {
        if (llInitiativeJoined.getVisibility() == View.GONE) {
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
        presenter.loadCreated();
        presenter.loadJoined();
    }

    /**
     * hideFloatingButton
     */
    private void hideFloatingButton() {
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
    public void onSuccessParticipate() {
        Snackbar.make(getActivity().findViewById(R.id.coordinator_main), getString(R.string.success_participate), Snackbar.LENGTH_SHORT).show();
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
        Snackbar.make(getActivity().findViewById(R.id.coordinator_main), (message != null) ? message : getString(R.string.default_error_action), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view, String type) {
        Initiative initiative;
        Bundle bundle;

        switch (type) {
            case TYPE_CREATED + TYPE_INPROGRESS:
                initiative = adapterInitiativeCreatedInProgress.getInitiativeItem(rvInitiativeCreatedInProgress.getChildAdapterPosition(view));

                bundle = new Bundle();
                bundle.putSerializable(Initiative.TAG, initiative);
                bundle.putBoolean(TYPE_CREATED, true);

                goToShowInitiative(bundle);
                break;
            case TYPE_CREATED + TYPE_HISTORY:
                initiative = adapterInitiativeCreatedHistory.getInitiativeItem(rvInitiativeCreatedHistory.getChildAdapterPosition(view));

                bundle = new Bundle();
                bundle.putSerializable(Initiative.TAG, initiative);
                bundle.putBoolean(TYPE_HISTORY, true);

                hideFloatingButton();
                goToShowInitiative(bundle);
                break;
            case TYPE_JOINED + TYPE_INPROGRESS:
                initiative = adapterInitiativeJoinedInProgress.getInitiativeItem(rvInitiativeJoindedInProgress.getChildAdapterPosition(view));

                bundle = new Bundle();
                bundle.putSerializable(Initiative.TAG, initiative);

                hideFloatingButton();
                goToShowInitiative(bundle);
                break;
            case TYPE_JOINED + TYPE_HISTORY:
                initiative = adapterInitiativeJoinedHistory.getInitiativeItem(rvInitiativeJoindedHistory.getChildAdapterPosition(view));

                bundle = new Bundle();
                bundle.putSerializable(Initiative.TAG, initiative);
                bundle.putBoolean(TYPE_HISTORY, true);

                hideFloatingButton();
                goToShowInitiative(bundle);
                break;
            default:
                break;
        }
    }

    /**
     * goToAddInitiative
     */
    private void goToAddInitiative() {
        NavHostFragment.findNavController(this).navigate(R.id.action_initiativeFragment_to_manageInitiativeFragment);
    }

    /**
     * goToShowInitiative
     * @param bundle
     */
    private void goToShowInitiative(Bundle bundle) {
        NavHostFragment.findNavController(this).navigate(R.id.action_initiativeFragment_to_showInitiativeFragment, bundle);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_scan_qr).setVisible(true);
        menu.findItem(R.id.action_scan_qr).setOnMenuItemClickListener(item -> {
            requestCamera();
            floatingActionButton.setVisibility(View.GONE);
            return true;
        });
    }

    /**
     * show the QRScanner
     */
    private void showQRscanner() {
        IntentIntegrator.forSupportFragment(InitiativeFragment.this)
                .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                .setPrompt(getString(R.string.prompt_lector_qr))
                .setRequestCode(JointlyApplication.REQUEST_PERMISSION_CAMERA_CODE)
                .setCameraId(0)
                .setOrientationLocked(false)
                .setCaptureActivity(QRCaptureActivity.class)
                .setBeepEnabled(false)
                .setBarcodeImageEnabled(false)
                .initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == JointlyApplication.REQUEST_PERMISSION_CAMERA_CODE) {
            if(resultCode == Activity.RESULT_OK && data != null) {
                IntentResult intentResult = IntentIntegrator.parseActivityResult(resultCode, data);
                if (intentResult.getContents() == null) {
                    Toast.makeText(getActivity(), getString(R.string.cancel_qr_lector), Toast.LENGTH_SHORT).show();
                } else {
                    presenter.setParticipate(intentResult.getContents().toString());
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.error_cancel_qr_lector), Toast.LENGTH_SHORT).show();
            }
        }
         super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * request permission camare
     */
    private void requestCamera() {
        if (ActivityCompat.checkSelfPermission(JointlyApplication.getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            showQRscanner();
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, JointlyApplication.REQUEST_PERMISSION_CAMERA_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == JointlyApplication.REQUEST_PERMISSION_CAMERA_CODE) {
            if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showQRscanner();
            } else {
                Toast.makeText(getActivity(), getString(R.string.message_permission_camare), Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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