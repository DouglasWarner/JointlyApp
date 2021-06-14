package com.douglas.jointlyapp.ui.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.services.Apis;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.infouser.InfoUserFragment;
import com.douglas.jointlyapp.ui.reviewuser.ReviewUserFragment;
import com.douglas.jointlyapp.ui.utils.CommonUtils;
import com.douglas.jointlyapp.ui.viewpager.UserViewPagerAdapter;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Locale;

/**
 * Fragment that represent the current user
 */
public class ProfileFragment extends Fragment implements ProfileContract.View {

    //region Variables

    public static final String LOGINUSER = "LOGINUSER";

    private ShapeableImageView imgUser;
    private ImageView ivEditImagenUser;
    private TextView tvUserName;
    private TextView tvUserLocation;
    private TextView tvUserEmail;
    private TextView tvUserPhome;
    private RatingBar rbUser;
    private TextView tvRatingUser;

    private User user;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    private Uri pathImagen;

    private InfoUserFragment infoUserFragment;
    private ReviewUserFragment reviewUserFragment;

    private ProfileContract.Presenter presenter;

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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);
        setListeners();
        setUser();
        setUpViewPager(viewPager2);

        presenter = new ProfilePresenter(this);
    }

    /**
     * setUpViewPager for ViewPager2
     * @param viewPager2
     */
    private void setUpViewPager(ViewPager2 viewPager2) {
        UserViewPagerAdapter userViewPagerAdapter = new UserViewPagerAdapter(getActivity().getSupportFragmentManager(), getLifecycle());
        userViewPagerAdapter.addUser(user);
        infoUserFragment = new InfoUserFragment(user);
        reviewUserFragment = new ReviewUserFragment(user, null);
        userViewPagerAdapter.addFragment(infoUserFragment);
        userViewPagerAdapter.addFragment(reviewUserFragment);
        String[] title = new String[]{"Info","Review"};

        viewPager2.setAdapter(userViewPagerAdapter);
        viewPager2.setUserInputEnabled(false);
        viewPager2.setOffscreenPageLimit(2);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> tab.setText(title[position])).attach();
    }

    /**
     * initUI
     * @param view
     */
    private void initUI(@NonNull View view) {
        imgUser = view.findViewById(R.id.imgUser);
        ivEditImagenUser = view.findViewById(R.id.ivEditImageUser);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserLocation = view.findViewById(R.id.tvUserLocation);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        tvUserPhome = view.findViewById(R.id.tvUserPhone);
        rbUser = view.findViewById(R.id.rbUser);
        tvRatingUser = view.findViewById(R.id.tvAverageRating);

        // set UserViewPager
        viewPager2 = view.findViewById(R.id.vpUser);
        tabLayout = view.findViewById(R.id.tbLayoutUser);
    }

    /**
     * setListeners
     */
    private void setListeners() {
        ivEditImagenUser.setOnClickListener(v -> {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, JointlyApplication.REQUEST_PERMISSION_CODE);
                }
            } else {
                openGallery();
            }
        });
    }

    /**
     * openGallery to select the imagen file
     */
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, JointlyApplication.REQUEST_IMAGE_GALLERY);
    }

    /**
     * setUser
     */
    private void setUser() {
        this.user = JointlyApplication.getCurrentSignInUser();

        if(user.getImagen() != null) {
            Glide.with(JointlyApplication.getContext())
                    .setDefaultRequestOptions(CommonUtils.getGlideOptions(User.TAG))
                    .load(Apis.getURLIMAGE()+user.getImagen())
                    .into(imgUser);
        } else {
            imgUser.setImageBitmap(CommonUtils.getImagenUserDefault(JointlyApplication.getContext()));
        }
        tvUserName.setText(user.getName());
        tvUserLocation.setText(user.getLocation());
        tvUserEmail.setText(user.getEmail());
        tvUserPhome.setText(user.getPhone());
    }

    @Override
    public void onStart() {
        super.onStart();

        presenter.loadRatingUser(user.getEmail());
        getActivity().findViewById(R.id.faButton).setVisibility(View.GONE);
    }

    @Override
    public void setRatingUser(float average) {
        rbUser.setRating(average);
        tvRatingUser.setText(String.format(Locale.getDefault(),"%.2f/%d", average, rbUser.getNumStars()));
    }

    @Override
    public void setUpdateImage() {
        imgUser.setImageURI(pathImagen);
    }

    @Override
    public void onError(String message) {
        Snackbar.make(getActivity().findViewById(R.id.coordinator_main), message != null ? message : getString(R.string.default_error_action), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == JointlyApplication.REQUEST_IMAGE_GALLERY) {
            if(resultCode == Activity.RESULT_OK && data != null) {
                pathImagen = data.getData();
                user.setImagen(pathImagen.toString());
                presenter.updateImage(user, pathImagen);
            } else {
                Toast.makeText(getActivity(), "No se ha seleccionado ninguna imagen", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == JointlyApplication.REQUEST_PERMISSION_CODE) {
            if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
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