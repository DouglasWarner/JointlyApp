package com.douglas.jointlyapp.ui.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.comparators.FavoriteSortByFollowers;
import com.douglas.jointlyapp.data.comparators.FavoriteSortByLocation;
import com.douglas.jointlyapp.data.comparators.FavoriteSortByName;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Collections;
import java.util.List;

public class UserFavoriteAdapter extends RecyclerView.Adapter<UserFavoriteAdapter.ViewHolder> {

    public interface ManageUserFavorite
    {
        void onClick(View User);
        void onClickBtnFollow(View user);
    }

    private List<User> list;
    private ManageUserFavorite listener;
    private boolean follow;

    public UserFavoriteAdapter(List<User> list, ManageUserFavorite listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserFavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_favorite_item, parent, false);
        return new UserFavoriteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserFavoriteAdapter.ViewHolder holder, int position) {
        holder.imgUser.setImageBitmap(list.get(position).getImagen());
        holder.tvUserName.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void update(List<User> list)
    {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void sortByName() {
        Collections.sort(list, new FavoriteSortByName());
        notifyDataSetChanged();
    }

    public void sortByLocation() {
        Collections.sort(list, new FavoriteSortByLocation());
        notifyDataSetChanged();
    }

    public void sortByUsersFollows() {
        Collections.sort(list, new FavoriteSortByFollowers());
        notifyDataSetChanged();
    }

    public User getUserItem(int position) {
        return list.get(position);
    }

    public void updateUnFollow(boolean b) {
        follow = b;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imgUser;
        TextView tvUserName;
        Button btnFollowUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.imgUserFavorite);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            btnFollowUser = itemView.findViewById(R.id.btnFollowUser);

            btnFollowUser.setOnClickListener(v -> {
                ((Button)v).setText((follow) ? "Siguiendo" : "Seguir");
                ((Button)v).setBackgroundColor((follow) ? itemView.getResources().getColor(R.color.primaryDarkColor) : itemView.getResources().getColor(R.color.secondaryLightColor));
                listener.onClickBtnFollow(itemView);
            });

            itemView.setOnClickListener(v -> listener.onClick(v));
        }
    }
}
