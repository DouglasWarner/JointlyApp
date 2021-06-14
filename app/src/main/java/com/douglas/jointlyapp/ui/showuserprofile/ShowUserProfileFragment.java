package com.douglas.jointlyapp.ui.showuserprofile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
 * Fragment that represents other user
 */
public class ShowUserProfileFragment extends Fragment implements ShowUserProfileContract.View, ReviewUserFragment.onSendRating{

    //region Variables

    private ShapeableImageView imgUser;
    private ImageView ivEditImagenUser;
    private TextView tvUserName;
    private TextView tvUserLocation;
    private TextView tvUserEmail;
    private TextView tvUserPhome;
    private RatingBar rbUser;
    private TextView tvRatingUser;

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    private User user;
    private MenuItem favorite;

    private InfoUserFragment infoUserFragment;
    private ReviewUserFragment reviewUserFragment;

    private ShowUserProfileContract.Presenter presenter;

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

        Bundle bundle = getArguments();
        user = (bundle != null) ? (User) bundle.getSerializable(User.TAG) : null;

        initUI(view);
        setUser(user);

        // set UserViewPager
        viewPager2 = view.findViewById(R.id.vpUser);
        tabLayout = view.findViewById(R.id.tbLayoutUser);
        setUpViewPager(viewPager2);

        presenter = new ShowUserProfilePresenter(this);
    }

    /**
     * setUpViewPager for ViewPager2
     * @param viewPager2
     */
    private void setUpViewPager(ViewPager2 viewPager2) {
        UserViewPagerAdapter userViewPagerAdapter = new UserViewPagerAdapter(getActivity().getSupportFragmentManager(), getLifecycle());
        userViewPagerAdapter.addUser(user);
        infoUserFragment = new InfoUserFragment(user);
        reviewUserFragment = new ReviewUserFragment(user, this);
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
    }

    /**
     * setUser
     * @param user
     */
    private void setUser(User user) {
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

        // set visibility for edit imagen button
        ivEditImagenUser.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.loadRatingUser(user.getEmail());
        getActivity().findViewById(R.id.faButton).setVisibility(View.GONE);
    }

    @Override
    public void setSuccessUnFollow() {
        favorite.setIcon(R.drawable.ic_favorite_border);
    }

    @Override
    public void setSuccessFollow() {
        favorite.setIcon(R.drawable.ic_favorite);
    }

    @Override
    public void setUserStateFollow(boolean follow) {
        favorite.setIcon(follow ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
    }

    @Override
    public void setRatingUser(float average) {
        rbUser.setRating(average);
        tvRatingUser.setText(String.format(Locale.getDefault(),"%.2f/%d", average, rbUser.getNumStars()));
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
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        favorite = menu.findItem(R.id.action_favorite).setVisible(true);
        favorite.setOnMenuItemClickListener(item -> {
            presenter.manageFollowUser(user);
            return true;
        });

        presenter.loadUserStateFollow(user);
    }

    @Override
    public void updateRating() {
        presenter.loadRatingUser(user.getEmail());
    }
}