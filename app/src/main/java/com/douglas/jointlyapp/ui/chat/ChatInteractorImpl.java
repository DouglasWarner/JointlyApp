package com.douglas.jointlyapp.ui.chat;

import android.util.Log;

import androidx.annotation.NonNull;

import com.douglas.jointlyapp.data.model.Chat;
import com.douglas.jointlyapp.data.model.ChatListAdapter;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.repository.ChatRepository;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.adapter.ChatAdapter;
import com.douglas.jointlyapp.ui.utils.CommonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Entity who connect with the APIS and LOCALDB
 */
public class ChatInteractorImpl {

    interface ChatInteractor {
        void onNoData();
        void onSuccess(List<ChatListAdapter> list);
        void onSendMessageSuccess(ChatListAdapter chcat);
    }

    private final ChatInteractor interactor;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    public ChatInteractorImpl(ChatInteractor interactor) {
        this.interactor = interactor;
    }

    /**
     * loadChat
     * @param initiative
     */
    public void loadChat(final Initiative initiative) {
            database.getReference().child(Chat.TABLE_NAME).child(initiative.getRef_code()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("TAG", "Error getting data", task.getException());
                    }
                    else {
                        Log.e("TAG", String.valueOf(task.getResult().getValue()));
                        String result = String.valueOf(task.getResult().getValue());

//                        interactor.onSuccess(list);
                    }
                }
            });
    }

    /**
     * sendMessage
     * @param initiative
     * @param userEmail
     * @param message
     */
    public void sendMessage(final Initiative initiative, final String userEmail, final String message) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(JointlyApplication.FORMAT_DD_MM_YYYY_HH_MM, Locale.getDefault());
            String date = simpleDateFormat.format(Calendar.getInstance().getTime());
            String idMensage = UUID.randomUUID().toString();
            User user = UserRepository.getInstance().getUser(userEmail);

            // send message
            Chat formatMessage = new Chat(date, initiative.getId(), userEmail, message, false, true);
            ChatListAdapter chatListAdapter = new ChatListAdapter(user.getImagen(), formatMessage);

            database.getReference().child(Chat.TABLE_NAME).child(initiative.getRef_code()).child(idMensage).setValue(chatListAdapter);
            interactor.onSendMessageSuccess(chatListAdapter);
    }
}
