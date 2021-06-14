package com.douglas.jointlyapp.ui.chat;

import com.douglas.jointlyapp.data.model.ChatListAdapter;
import com.douglas.jointlyapp.data.model.Initiative;

import java.util.List;

/**
 * Entity that connects within view and interactor
 */
public class ChatPresenter implements ChatContract.Presenter, ChatInteractorImpl.ChatInteractor {

    private ChatContract.View view;
    private ChatInteractorImpl interactor;

    public ChatPresenter(ChatContract.View view) {
        this.view = view;
        interactor = new ChatInteractorImpl(this);
    }

    @Override
    public void loadChat(Initiative initiative) {
        view.showProgress();
        interactor.loadChat(initiative);
    }

    @Override
    public void sendMessage(Initiative initiative, String userEmail, String message) {
        interactor.sendMessage(initiative, userEmail, message);
    }

    @Override
    public void onDestroy() {
        interactor = null;
        view = null;
    }

    @Override
    public void onNoData() {
        view.setNoData();
        view.hideProgress();
    }

    @Override
    public void onSuccess(List<ChatListAdapter> list) {
        view.hideProgress();
        view.onSuccess(list);
    }

    @Override
    public void onSendMessageSuccess(ChatListAdapter chat) {
        view.onSuccessSendMessage(chat);
    }
}
