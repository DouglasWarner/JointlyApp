package com.douglas.jointlyapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 * Adapter that manage user joined item recycler
 */
public class UserJoinedAdapter extends RecyclerView.Adapter<UserJoinedAdapter.ViewHolder>{

    /**
     * interface that connects click on item with his parent
     */
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
        if(list.get(position).getImagen() != null)
            Glide.with(JointlyApplication.getContext())
                    .setDefaultRequestOptions(CommonUtils.getGlideOptions(User.TAG))
                    .load(Apis.getURLIMAGE()+list.get(position).getImagen())
                    .into(holder.imgUser);
        else
            holder.imgUser.setImageBitmap(CommonUtils.getImagenUserDefault(JointlyApplication.getContext()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * update list
     * @param list
     */
    public void update(List<User> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * get the user item
     * @param position
     * @return User
     */
    public User getUserItem(int position)
    {
        return list.get(position);
    }

    /**
     * ViewHolder for item layout
     */
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
