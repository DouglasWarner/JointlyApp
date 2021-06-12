package com.douglas.jointlyapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.UserReviewUser;

import java.util.List;

/**
 * Adapter that manage user review item recycler
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<UserReviewUser> reviewUserList;

    public ReviewAdapter(List<UserReviewUser> reviewUserList) {
        this.reviewUserList = reviewUserList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(reviewUserList.get(position).getUser());
        holder.tvReview.setText(reviewUserList.get(position).getReview());
        holder.rbStarsReview.setRating(reviewUserList.get(position).getStars());
        //TODO mirar con servidor en aws
//        holder.tvDateReview.setText(CommonUtils.formatDateFromAPI(reviewUserList.get(position).getDate()));
        holder.tvDateReview.setText(reviewUserList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return reviewUserList.size();
    }

    /**
     * update list
     * @param list
     */
    public void update(List<UserReviewUser> list) {
        this.reviewUserList.clear();
        this.reviewUserList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * add message to list
     * @param reviewUser
     */
    public void addMessage(UserReviewUser reviewUser) {
        reviewUserList.add(reviewUser);
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for item layout
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvReview;
        RatingBar rbStarsReview;
        TextView tvDateReview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNameUser);
            tvReview = itemView.findViewById(R.id.itemTvSendMessage);
            rbStarsReview = itemView.findViewById(R.id.itemRatingBarStars);
            tvDateReview = itemView.findViewById(R.id.itemTvDate);
        }
    }
}
