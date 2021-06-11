package com.douglas.jointlyapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.comparators.HomeListAdapterSortByLocation;
import com.douglas.jointlyapp.data.comparators.HomeListAdapterSortByUserJoineds;
import com.douglas.jointlyapp.data.comparators.HomeListAdaptersSortByDate;
import com.douglas.jointlyapp.data.model.HomeListAdapter;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.utils.CommonUtils;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> implements Filterable {

    public interface ManageInitiative {
        void onClick(View initiative);
    }

    private List<HomeListAdapter> homeListAdapters;
    private List<HomeListAdapter> homeListAdaptersAUX;
    private ManageInitiative listener;
    private ValueFilter valueFilter;

    public HomeAdapter(List<HomeListAdapter> homeListAdapters, ManageInitiative listener) {
        this.homeListAdapters = homeListAdapters;
        this.homeListAdaptersAUX = homeListAdapters;
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
        if(homeListAdapters.get(position).getInitiative().getImagen() != null) {
            holder.imgInitiative.setImageBitmap(homeListAdapters.get(position).getInitiative().getImagen());
        } else {
            holder.imgInitiative.setImageBitmap(CommonUtils.getImagenInitiativeDefault(JointlyApplication.getContext()));
        }

        User u = homeListAdapters.get(position).getUserOwner();

        if(u != null && u.getImagen() != null)
            holder.imgUser.setImageBitmap(u.getImagen());
        else
            holder.imgUser.setImageBitmap(CommonUtils.getImagenUserDefault(JointlyApplication.getContext()));

        holder.tvInitiativeName.setText(homeListAdapters.get(position).getInitiative().getName());
        holder.tvInitiativeDate.setText(homeListAdapters.get(position).getInitiative().getTarget_date().split(" ")[0]);
        holder.tvCountUser.setText(String.valueOf(homeListAdapters.get(position).getCountUserJoined()));
        holder.tvLocationInitiative.setText(homeListAdapters.get(position).getInitiative().getLocation());
    }

    @Override
    public int getItemCount() {
        return homeListAdapters.size();
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    /**
     *
     */
    public void sortByDate() {
        Collections.sort(homeListAdapters, new HomeListAdaptersSortByDate());
        notifyDataSetChanged();
    }

    /**
     *
     */
    public void sortByLocation() {
        Collections.sort(homeListAdapters, new HomeListAdapterSortByLocation());
        notifyDataSetChanged();
    }

    /**
     *
     */
    public void sortByUsersJoineds() {
        Collections.sort(homeListAdapters, new HomeListAdapterSortByUserJoineds());
        notifyDataSetChanged();
    }

    /**
     *
     * @param homeListAdapters
     */
    public void update(List<HomeListAdapter> homeListAdapters) {
        this.homeListAdapters.clear();
        this.homeListAdapters = homeListAdapters;
        this.homeListAdaptersAUX.clear();
        this.homeListAdaptersAUX.addAll(homeListAdapters);

        notifyDataSetChanged();
    }

    /**
     *
     * @param position
     * @return
     */
    public HomeListAdapter getInitiativeItem(int position) {
        return homeListAdapters.get(position);
    }

    /**
     *
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgInitiative;
        ShapeableImageView imgUser;
        TextView tvInitiativeName;
        TextView tvInitiativeDate;
        TextView tvCountUser;
        TextView tvLocationInitiative;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgInitiative = itemView.findViewById(R.id.imgBtnInitiative);
            imgUser = itemView.findViewById(R.id.imgUser);
            tvInitiativeName = itemView.findViewById(R.id.tvInitiativeName);
            tvInitiativeDate = itemView.findViewById(R.id.tvInitiativeDate);
            tvCountUser = itemView.findViewById(R.id.tvCountUser);
            tvLocationInitiative = itemView.findViewById(R.id.tvLocationInitiative);

            itemView.setOnClickListener(v -> {
                listener.onClick(v);
            });
        }
    }

    /**
     * Filter for the functionality of search view
     */
    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            if(constraint != null && constraint.length() > 0) {
                List<HomeListAdapter> filterHomeListAdapters = new ArrayList<>();

                for (int i = 0; i < homeListAdaptersAUX.size(); i++) {
                    if ((homeListAdaptersAUX.get(i).getInitiative().getName().toUpperCase())
                            .startsWith(constraint.toString().toUpperCase())) {
                        filterHomeListAdapters.add(homeListAdaptersAUX.get(i));
                    }
                }

                for (int i = 0; i < homeListAdaptersAUX.size(); i++) {
                    if ((homeListAdaptersAUX.get(i).getInitiative().getLocation().toUpperCase())
                            .startsWith(constraint.toString().toUpperCase())) {
                        filterHomeListAdapters.add(homeListAdaptersAUX.get(i));
                    }
                }

                filterResults.count = (int) filterHomeListAdapters.stream().distinct().count();
                filterResults.values = filterHomeListAdapters.stream().distinct().collect(Collectors.toList());
            } else {
                filterResults.count = homeListAdaptersAUX.size();
                filterResults.values = homeListAdaptersAUX;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            homeListAdapters = (List<HomeListAdapter>) results.values;
            notifyDataSetChanged();
        }
    }
}
