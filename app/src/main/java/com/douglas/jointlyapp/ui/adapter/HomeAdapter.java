package com.douglas.jointlyapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.comparators.InitiativeSortByDate;
import com.douglas.jointlyapp.data.comparators.InitiativeSortByLocation;
import com.douglas.jointlyapp.data.comparators.InitiativeSortByUserJoineds;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.utils.CommonUtils;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Collections;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    public interface ManageInitiative
    {
        void onClick(View initiative);
    }

    private List<Initiative> list;
    private List<User> userOwners;
    private ManageInitiative listener;

    public HomeAdapter(List<Initiative> list, List<User> userOwners, ManageInitiative listener) {
        this.list = list;
        this.userOwners = userOwners;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.initiative_home_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, int position) {
        holder.imgInitiative.setImageBitmap(list.get(position).getImagen());
        User u = userOwners.stream().findFirst().filter(x -> x.getEmail().equals(list.get(position).getCreatedBy())).orElse(null);

        if(u != null)
            holder.imgUser.setImageBitmap(u.getImagen());
        else
            holder.imgUser.setImageBitmap(CommonUtils.getImagenUserDefault(JointlyApplication.getContext()));

        holder.tvInitiativeName.setText(list.get(position).getName());
        holder.tvCountUser.setText(String.valueOf(list.get(position).getCountUserJoined()));
        holder.tvLocationInitiative.setText(list.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void update(List<Initiative> list, List<User> userOwners)
    {
        this.list.clear();
        this.list.addAll(list);
        this.userOwners.clear();
        this.userOwners.addAll(userOwners);
        notifyDataSetChanged();
    }

    //TODO refactorizar
    public void sortByDate() {
        Collections.sort(list, new InitiativeSortByDate());
        notifyDataSetChanged();
    }

    public void sortByLocation() {
        Collections.sort(list, new InitiativeSortByLocation());
        notifyDataSetChanged();
    }

    public void sortByUsersJoineds() {
        Collections.sort(list, new InitiativeSortByUserJoineds());
        notifyDataSetChanged();
    }

    public Initiative getInitiativeItem(int position)
    {
        return list.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgInitiative;
        ShapeableImageView imgUser;
        TextView tvInitiativeName;
        TextView tvCountUser;
        TextView tvLocationInitiative;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgInitiative = itemView.findViewById(R.id.imgBtnInitiative);
            imgUser = itemView.findViewById(R.id.imgUser);
            tvInitiativeName = itemView.findViewById(R.id.tvInitiativeName);
            tvCountUser = itemView.findViewById(R.id.tvCountUser);
            tvLocationInitiative = itemView.findViewById(R.id.tvLocationInitiative);

            itemView.setOnClickListener(v -> {
                listener.onClick(v);
            });
        }
    }
}
