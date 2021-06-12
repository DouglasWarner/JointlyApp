package com.douglas.jointlyapp.ui.chat;

import com.douglas.jointlyapp.data.model.Chat;

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
    public void loadChat(long idInitiative) {
        view.showProgress();
        interactor.loadChat(idInitiative);
    }

    @Override
    public void sendMessage(long idInitiative, String userEmail, String message) {
        interactor.sendMessage(idInitiative, userEmail, message);
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
    public void onSuccess(List<Chat> list) {
        view.hideProgress();
        view.onSuccess(list);
    }

    @Override
    public void onSendMessageSuccess(Chat chat) {
        view.onSuccessSendMessage(chat);
    }
}
