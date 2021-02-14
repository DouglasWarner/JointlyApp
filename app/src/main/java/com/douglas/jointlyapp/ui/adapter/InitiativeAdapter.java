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

import java.util.List;

public class InitiativeAdapter extends RecyclerView.Adapter<InitiativeAdapter.ViewHolder> {

    public interface ManageInitiative
    {
        void onClick(View initiative);
    }

    private List<Initiative> list;
    private ManageInitiative listener;

    public InitiativeAdapter(List<Initiative> list, ManageInitiative listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public InitiativeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.initiative_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InitiativeAdapter.ViewHolder holder, int position) {
        holder.imgInitiative.setImageResource(R.drawable.playasucia);
//        holder.imgInitiative.setImageBitmap(BitmapFactory.decodeByteArray(list.get(position).getImagen(), 0, list.get(position).getImagen().length));
        holder.tvInitiativeName.setText(list.get(position).getName());
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
        TextView tvInitiativeName;
        Toolbar tbCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgInitiative = itemView.findViewById(R.id.imgInitiative);
            tvInitiativeName = itemView.findViewById(R.id.tvInitiativeName);

            itemView.setOnClickListener(v -> {
                listener.onClick(v);
            });
            //TODO implementar click on toolbar
        }
    }
}
