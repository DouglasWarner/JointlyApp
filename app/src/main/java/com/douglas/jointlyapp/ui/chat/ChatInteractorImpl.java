package com.douglas.jointlyapp.ui.chat;

import android.os.Handler;

import com.douglas.jointlyapp.data.model.Chat;
import com.douglas.jointlyapp.data.repository.ChatRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ChatInteractorImpl {

    interface ChatInteractor
    {
        void onNoData();
        void onSuccess(List<Chat> list);
        void onSendMessageSuccess(Chat chcat);
    }

    private ChatInteractor interactor;

    public ChatInteractorImpl(ChatInteractor interactor) {
        this.interactor = interactor;
    }

    public void loadChat(final long idInitiative)
    {
        //TODO implementar el jobschelude para leer cada cierto tiempo si hay mensajes nuevos
        new Handler().postDelayed(() -> {
            List<Chat> list = ChatRepository.getInstance().getChatInitiative(idInitiative);

            if(list.isEmpty())
            {
                interactor.onNoData();
                return;
            }

            interactor.onSuccess(list);
        },100);
    }

    public void sendMessage(final long idInitiative, final String userEmail, final String message)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        String date = simpleDateFormat.format(Calendar.getInstance().getTime());

        Chat chat = new Chat(date, idInitiative, userEmail, message);

        ChatRepository.getInstance().insert(chat);

        interactor.onSendMessageSuccess(chat);
    }
}
