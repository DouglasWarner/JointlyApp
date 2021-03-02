package com.douglas.jointlyapp.ui.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.douglas.jointlyapp.ui.utils.CommonUtils;
import com.google.android.material.imageview.ShapeableImageView;

/**
 * La ventana de el usuario de inicio de sesion
 */
public class ProfileFragment extends Fragment implements ProfileContract.View{

    private ShapeableImageView shImgUser;
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

    BitmapDrawable imageUser;
    User user;

    private ProfileContract.Presenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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

        initUI(view);

        shImgUser.setOnClickListener(v -> {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    openGallery();
                }
                else
                {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, JointlyApplication.REQUEST_PERMISSION_CODE);
                }
            }
            else
            {
                openGallery();
            }
        });

        view.findViewById(R.id.layoutInitiativeCreateds).setVisibility(View.GONE);
        view.findViewById(R.id.divider4).setVisibility(View.GONE);

        presenter = new ProfilePresenter(this);

        presenter.loadUser(JointlyPreferences.getInstance().getUser());
    }

    private void initUI(@NonNull View view) {
        shImgUser = view.findViewById(R.id.imgUser);
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
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, JointlyApplication.REQUEST_IMAGE_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == JointlyApplication.REQUEST_IMAGE_GALLERY)
        {
            if(resultCode == Activity.RESULT_OK && data != null)
            {
                Uri imagen = data.getData();
                shImgUser.setImageURI(imagen);
                imageUser = ((BitmapDrawable)shImgUser.getDrawable());
                user.setImagen(imageUser.getBitmap());
                presenter.updateImage(user);
            }
            else
            {
                Toast.makeText(getActivity(), "No se ha seleccionado ninguna imagen", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == JointlyApplication.REQUEST_PERMISSION_CODE)
        {
            if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                openGallery();
            }
            else
            {
                Toast.makeText(getActivity(), "Se necesitan los permisos para abrir la galeria", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.setGroupVisible(R.id.group_action_profile, true);
        menu.findItem(R.id.action_editAccount).setVisible(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().findViewById(R.id.faButton).setVisibility(View.GONE);
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
    public void onSuccess(User user, int countUserFollowers, int initiativeCreated, int initiativeJoined) {
        shImgUser.setImageBitmap(user.getImagen());
        tvUserName.setText(user.getName());
        tvUserEmail.setText(user.getEmail());
        tvUserLocation.setText(user.getLocation());
        tvUserPhome.setText(user.getPhone());
        tvUserDescription.setText(user.getDescription());
        tvUserFollows.setText(String.format(getString(R.string.tvUserFollowsFormat), countUserFollowers));
        tvUserInitiativeJoineds.setText(String.valueOf(initiativeJoined));
        tvUserInitiativeCreateds.setText(String.valueOf(initiativeCreated));
        tvUserCreatedAt.setText(user.getCreatedAt());

        this.user = user;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}