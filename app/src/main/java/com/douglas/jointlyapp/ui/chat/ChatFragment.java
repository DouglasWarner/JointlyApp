package com.douglas.jointlyapp.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.Chat;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.ui.adapter.ChatAdapter;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that represent chat message for initiative
 */
public class ChatFragment extends Fragment implements ChatContract.View {

    //TODO terminar

    private LinearLayout llLoading;
    private LinearLayout llNoData;
    private TextView tvMessage;
    private ImageButton imgbtnSend;
    private RecyclerView rvChat;
    private ChatAdapter adapter;

    private ChatContract.Presenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        Bundle bundle = getArguments();

        Initiative initiative = bundle != null ? (Initiative) bundle.getSerializable(Initiative.TAG) : null;
        String userEmail = JointlyPreferences.getInstance().getUser();

        initUI(view);
        initRecycler();

        presenter = new ChatPresenter(this);

        if(initiative != null) {
            toolbar.setTitle(initiative.getName() + " Chat");
            imgbtnSend.setOnClickListener(v -> {
                presenter.sendMessage(initiative.getId(), userEmail, tvMessage.getText().toString());
            });
            presenter.loadChat(initiative.getId());
        }
    }

    private void initUI(@NonNull View view) {
        llLoading = view.findViewById(R.id.llLoading);
        llNoData = view.findViewById(R.id.llNoDataChat);
        tvMessage = view.findViewById(R.id.tvMessage);
        imgbtnSend = view.findViewById(R.id.imgBtnSend);
        rvChat = view.findViewById(R.id.rvChatInitiative);
    }

    private void initRecycler() {
        adapter = new ChatAdapter(new ArrayList<>(), JointlyPreferences.getInstance().getUser());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        rvChat.setHasFixedSize(true);
        rvChat.setAdapter(adapter);
        rvChat.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void setNoData() {
        llNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgress() {
        llLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        llLoading.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessSendMessage(Chat chat) {
        if(llNoData.getVisibility() == View.VISIBLE)
            llNoData.setVisibility(View.GONE);
        adapter.addMessage(chat);
        tvMessage.setText("");
    }

    @Override
    public void onSuccess(List<Chat> list) {
        if(llNoData.getVisibility() == View.VISIBLE)
            llNoData.setVisibility(View.GONE);

        adapter.update(list);
    }
}