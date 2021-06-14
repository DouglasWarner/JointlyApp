package com.douglas.jointlyapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.Chat;
import com.douglas.jointlyapp.data.model.ChatListAdapter;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.utils.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Adapter that manage chat messages recycler
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>  {

    public static final int LEFT_MESSAGE = 0;
    public static final int RIGHT_MESSAGE = 1;
    private List<ChatListAdapter> list;
    private String user;

    public ChatAdapter(List<ChatListAdapter> list, String user) {
        this.list = list;
        this.user = user;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == RIGHT_MESSAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        holder.tvUser.setText(list.get(position).getChat().getUserEmail());
        holder.tvMessage.setText(list.get(position).getChat().getMessage());
        holder.tvDate.setText(list.get(position).getChat().getDateMessage());
        Glide.with(JointlyApplication.getContext())
                .setDefaultRequestOptions(CommonUtils.getGlideOptions(User.TAG))
                .load(list.get(position).getPathImageUser())
                .into(holder.ivImageUser);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * update list
     * @param list
     */
    public void update(List<ChatListAdapter> list) {
        Collections.sort(list, new Comparator<ChatListAdapter>() {
            @Override
            public int compare(ChatListAdapter o1, ChatListAdapter o2) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(JointlyApplication.FORMAT_DD_MM_YYYY_HH_MM, Locale.getDefault());
                int result = 0;
                try {
                    result = simpleDateFormat.parse(o1.getChat().getDateMessage()).compareTo(simpleDateFormat.parse(o2.getChat().getDateMessage()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return result;
            }
        });
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * add message to the list
     * @param chat
     */
    public void addMessage(ChatListAdapter chat) {
        list.add(chat);
        user = chat.getChat().getUserEmail();
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for item layout
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        TextView tvUser;
        TextView tvDate;
        ImageView ivImageUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvUser = itemView.findViewById(R.id.tvUser);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            ivImageUser = itemView.findViewById(R.id.ivImageUser);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getChat().getUserEmail().equals(user))
            return RIGHT_MESSAGE;
        else
            return LEFT_MESSAGE;
    }
}
