package com.douglas.jointlyapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.Chat;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;

import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>  {

    public static final int LEFT_MESSAGE = 0;
    public static final int RIGHT_MESSAGE = 1;
    private List<Chat> list;
    private String user;

    public ChatAdapter(List<Chat> list, String user) {
        this.list = list;
        this.user = user;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == RIGHT_MESSAGE)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            return new ChatAdapter.ViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            return new ChatAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        holder.tvUser.setText(list.get(position).getUserEmail());
        holder.tvMessage.setText(list.get(position).getMessage());
        holder.tvDate.setText(list.get(position).getDateMessage());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void update(List<Chat> list)
    {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addMessage(Chat chat)
    {
        list.add(chat);
        user = chat.getUserEmail();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        TextView tvUser;
        TextView tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvUser = itemView.findViewById(R.id.tvUser);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getUserEmail().equals(user))
            return RIGHT_MESSAGE;
        else
            return LEFT_MESSAGE;
    }
}
