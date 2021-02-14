package com.douglas.jointlyapp.ui.favorite;

import android.icu.text.RelativeDateTimeFormatter;
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
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.adapter.UserFavoriteAdapter;
import com.douglas.jointlyapp.ui.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment implements FavoriteContract.View, UserFavoriteAdapter.ManageUserFavorite {

    private LinearLayout llLoading;
    private LinearLayout llNoData;
    private RecyclerView rvUserFavorite;
    private UserFavoriteAdapter adapter;

    private FavoritePresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        llLoading = view.findViewById(R.id.llLoading);
        llNoData = view.findViewById(R.id.llNoDataInitiativeCreated);

        rvUserFavorite = view.findViewById(R.id.rvUserFavorite);

        adapter = new UserFavoriteAdapter(new ArrayList<>(), this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvUserFavorite.setLayoutManager(layoutManager);
        rvUserFavorite.setAdapter(adapter);

        presenter = new FavoritePresenter(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        User user = new User(0,"douglas@gmail.com","Dou123456","Douglas","666666666", CommonUtils.getImagenUserDefault(JointlyApplication.getContext()),"Málaga","Me dedico a crear iniciativas para ayudar a los mas necesitados","12/02/2021");
        presenter.load(user);
        getActivity().findViewById(R.id.faButton).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View User) {
        Bundle b = new Bundle();
        b.putSerializable("user", (User)adapter.getUserItem(rvUserFavorite.getChildAdapterPosition(User)));

        NavHostFragment.findNavController(this).navigate(R.id.action_favoriteFragment_to_profileFragment, b);
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
    public void onSuccess(List<User> list) {
        if(llNoData.getVisibility() == View.VISIBLE)
            llNoData.setVisibility(View.GONE);

        adapter.update(list);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }
}