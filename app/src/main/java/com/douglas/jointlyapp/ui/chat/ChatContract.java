package com.douglas.jointlyapp.ui.chat;

import com.douglas.jointlyapp.data.model.Chat;
import com.douglas.jointlyapp.data.model.ChatListAdapter;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.ui.base.BaseListView;
import com.douglas.jointlyapp.ui.base.BasePresenter;

/**
 * Interface that set the call logic within view and presenter
 */
public interface ChatContract {

    interface View extends BaseListView<ChatListAdapter> {
        void setNoData();
        void showProgress();
        void hideProgress();
        void onSuccessSendMessage(ChatListAdapter chat);
    }

    interface Presenter extends BasePresenter {
        void loadChat(Initiative initiative);
        void sendMessage(Initiative initiative, String userEmail, String message);
    }
}
