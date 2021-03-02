package com.douglas.jointlyapp.ui.chat;

import com.douglas.jointlyapp.data.model.Chat;
import com.douglas.jointlyapp.ui.base.BaseListView;
import com.douglas.jointlyapp.ui.base.BasePresenter;

public interface ChatContract {

    interface View extends BaseListView<Chat>
    {
        void setNoData();
        void showProgress();
        void hideProgress();
        void onSuccessSendMessage(Chat chat);
    }

    interface Presenter extends BasePresenter
    {
        void loadChat(int idInitiative);
        void sendMessage(int idInitiative, String userEmail, String message);
    }
}
