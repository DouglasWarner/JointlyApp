package com.douglas.jointlyapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.services.Apis;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.utils.CommonUtils;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

/**
 * Adapter that manage user favorite item recycler
 */
public class UserFavoriteAdapter extends RecyclerView.Adapter<UserFavoriteAdapter.ViewHolder> {

    /**
     * interface that connects click on item with his parent
     */
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
        if(list.get(position).getImagen() != null)
            Glide.with(JointlyApplication.getContext())
                    .setDefaultRequestOptions(CommonUtils.getGlideOptions(User.TAG))
                    .load(Apis.getURLIMAGE()+list.get(position).getImagen())
                    .into(holder.imgUser);
        else
            holder.imgUser.setImageBitmap(CommonUtils.getImagenUserDefault(JointlyApplication.getContext()));
        holder.tvUserName.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * update list
     * @param list
     */
    public void update(List<User> list)
    {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * get user
     * @param position
     * @return User
     */
    public User getUserItem(int position) {
        return list.get(position);
    }

    /**
     * update the follow state
     * @param b
     */
    public void updateUnFollow(boolean b) {
        follow = b;
    }

    /**
     * ViewHolder for item layout
     */
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
