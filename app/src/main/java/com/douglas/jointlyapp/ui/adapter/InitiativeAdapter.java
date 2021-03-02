package com.douglas.jointlyapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.Initiative;

import java.util.List;

public class InitiativeAdapter extends RecyclerView.Adapter<InitiativeAdapter.ViewHolder> {

    public interface ManageInitiative
    {
        void onClick(View initiative, String status);
    }

    private List<Initiative> list;
    private ManageInitiative listener;
    private String type;

    public InitiativeAdapter(List<Initiative> list, ManageInitiative listener, String type) {
        this.list = list;
        this.listener = listener;
        this.type = type;
    }

    @NonNull
    @Override
    public InitiativeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.initiative_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InitiativeAdapter.ViewHolder holder, int position) {
        holder.imgInitiative.setImageBitmap(list.get(position).getImagen());
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgInitiative = itemView.findViewById(R.id.imgBtnInitiative);
            tvInitiativeName = itemView.findViewById(R.id.tvInitiativeName);

            itemView.setOnClickListener(v -> {
                listener.onClick(v, type);
            });
        }
    }
}
