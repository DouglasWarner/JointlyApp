package com.douglas.jointlyapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.services.Apis;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.utils.CommonUtils;

import java.util.List;

/**
 * Adapter that manage initiative item recycler
 */
public class InitiativeAdapter extends RecyclerView.Adapter<InitiativeAdapter.ViewHolder> {

    /**
     * interface that connects click on item with his parent
     */
    public interface ManageInitiative {
        void onClick(View view, String status);
    }

    private Context context;
    private List<Initiative> list;
    private ManageInitiative listener;
    private String type;

    public InitiativeAdapter(Context context, List<Initiative> list, ManageInitiative listener, String type) {
        this.context = context;
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
        if (list.get(position).getImage() != null) {
            Glide.with(JointlyApplication.getContext())
                    .setDefaultRequestOptions(CommonUtils.getGlideOptions(Initiative.TAG))
                    .load(Apis.getURLIMAGE()+list.get(position).getImage())
                    .into(holder.imgInitiative);
        } else {
            holder.imgInitiative.setImageBitmap(CommonUtils.getImagenInitiativeDefault(context));
        }
        holder.tvInitiativeName.setText(list.get(position).getName());
        holder.tvDate.setText(list.get(position).getTarget_date().split(" ")[0]);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * update list
     * @param list
     */
    public void update(List<Initiative> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * get initiative
     * @param position
     * @return Initiative
     */
    public Initiative getInitiativeItem(int position) {
        return list.get(position);
    }

    /**
     * ViewHolder for item layout
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgInitiative;
        TextView tvInitiativeName;
        TextView tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgInitiative = itemView.findViewById(R.id.imgBtnInitiative);
            tvInitiativeName = itemView.findViewById(R.id.tvInitiativeName);
            tvDate = itemView.findViewById(R.id.tvInitiativeDate);

            itemView.setOnClickListener(v -> {
                listener.onClick(v, type);
            });
        }
    }
}
