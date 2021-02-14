package com.douglas.jointlyapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.Initiative;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    public interface ManageInitiative
    {
        void onClick(View initiative);
    }

    private List<Initiative> list;
    private ManageInitiative listener;

    public HomeAdapter(List<Initiative> list, ManageInitiative listener) {
        this.list = list;
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
        holder.imgInitiative.setImageURI(list.get(position).getImagen());
        holder.imgUser.setImageResource(R.mipmap.ic_app);
        holder.tvInitiativeName.setText(list.get(position).getName());
        holder.tvCountUser.setText(String.valueOf(list.get(position).getUserJoined().size()));
        holder.tvLocationInitiative.setText(list.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void update(List<Initiative> list)
    {
        this.list.clear();
        this.list.addAll(list);
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
        Toolbar tbCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgInitiative = itemView.findViewById(R.id.imgInitiative);
            imgUser = itemView.findViewById(R.id.imgUser);
            tvInitiativeName = itemView.findViewById(R.id.tvInitiativeName);
            tvCountUser = itemView.findViewById(R.id.tvCountUser);
            tvLocationInitiative = itemView.findViewById(R.id.tvLocationInitiative);

            itemView.setOnClickListener(v -> {
                listener.onClick(v);
            });
            //TODO implementar click on toolbar
        }
    }
}
