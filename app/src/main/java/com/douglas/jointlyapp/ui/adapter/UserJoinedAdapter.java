package com.douglas.jointlyapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.User;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class UserJoinedAdapter extends RecyclerView.Adapter<UserJoinedAdapter.ViewHolder>{

    public interface ManageInitiative {
        void onClick(View User);
    }

    private List<User> list;
    private UserJoinedAdapter.ManageInitiative listener;

    public UserJoinedAdapter(List<User> list, ManageInitiative listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserJoinedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_joined_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserJoinedAdapter.ViewHolder holder, int position) {
        holder.imgUser.setImageBitmap(list.get(position).getImagen());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void update(List<User> list) {
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.imgUserJoined);

            itemView.setOnClickListener(v -> {
                listener.onClick(v);
            });
        }
    }
}
