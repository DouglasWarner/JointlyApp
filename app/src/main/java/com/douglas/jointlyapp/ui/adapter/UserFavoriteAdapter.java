package com.douglas.jointlyapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class UserFavoriteAdapter extends RecyclerView.Adapter<UserFavoriteAdapter.ViewHolder> {

    public interface ManageUserFavorite
    {
        void onClick(View User);
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
        holder.imgUser.setImageURI(list.get(position).getImagen());
        holder.tvUserName.setText(list.get(position).getName());
        //TODO implementar cuando se deja de seguir
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

    public User getUserItem(int position)
    {
        return list.get(position);
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
                if(!follow) {
//                    btnFollowUser.setBackgroundColor(JointlyApplication.getContext()..getResources().getColor(R.color.white));
                    btnFollowUser.setText("Seguir");
                    follow = !follow;
                }
                else
                {
//                    btnFollowUser.setBackgroundColor(JointlyApplication.getContext().getResources().getColor(R.color.primaryColor));
                    btnFollowUser.setText("Siguiendo");
                    follow = !follow;
                }
            });
            itemView.setOnClickListener(v -> {
                listener.onClick(v);
            });
        }
    }
}
