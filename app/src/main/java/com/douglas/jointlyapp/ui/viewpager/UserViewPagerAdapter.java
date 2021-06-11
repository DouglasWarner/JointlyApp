package com.douglas.jointlyapp.ui.viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.infouser.InfoUserFragment;
import com.douglas.jointlyapp.ui.reviewuser.ReviewUserFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for ViewPager2 from UserProfile
 */
public class UserViewPagerAdapter extends FragmentStateAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();
    private User user;

    public UserViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    //TODO ver como hacer que no se creen solamente una vez o que los metodos load de presenter se ejecuten cuando se visualize la vista
    // si no se puede no pasa nada, sigue con lo demas

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new Fragment();
        switch (position) {
            case 0:
                fragment = new InfoUserFragment(user);
                break;
            case 1:
                fragment = new ReviewUserFragment(user);
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    /**
     * Add fragment for ViewPager2
     * @param fragment
     */
    public void addFragment(Fragment fragment) {
        fragmentList.add(fragment);
    }

    /**
     * Add the current user for show data on profile fragment
     * @param user
     */
    public void addUser(User user) {
        this.user = user;
    }

    public Fragment getFragment(int position) {
        return fragmentList.get(position);
    }
}
