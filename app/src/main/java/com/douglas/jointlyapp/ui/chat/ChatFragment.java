package com.douglas.jointlyapp.ui.chat;

import android.os.Bundle;
import android.util.Log;
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
import com.douglas.jointlyapp.data.model.ChatListAdapter;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.adapter.ChatAdapter;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Fragment that represent chat message for initiative
 */
public class ChatFragment extends Fragment implements ChatContract.View {

    //region Variables

    private LinearLayout llLoading;
    private LinearLayout llNoData;
    private TextView tvMessage;
    private ImageButton imgbtnSend;
    private RecyclerView rvChat;
    private ChatAdapter adapter;

    private Initiative initiative;
    private User user;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ChatContract.Presenter presenter;

    //endregion

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

        database = FirebaseDatabase.getInstance();

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        Bundle bundle = getArguments();

        initiative = bundle != null ? (Initiative) bundle.getSerializable(Initiative.TAG) : null;
        user = JointlyApplication.getCurrentSignInUser();
        databaseReference = database.getReference().child(Chat.TABLE_NAME).child(initiative.getRef_code()).getRef();

        initUI(view);
        initRecycler();
        setListeners();

        presenter = new ChatPresenter(this);

        if(initiative != null) {
            toolbar.setTitle(initiative.getName() + " Chat");
        }
    }

    /**
     * initUI
     * @param view
     */
    private void initUI(@NonNull View view) {
        llLoading = view.findViewById(R.id.llLoading);
        llNoData = view.findViewById(R.id.llNoDataChat);
        tvMessage = view.findViewById(R.id.tvMessage);
        imgbtnSend = view.findViewById(R.id.imgBtnSend);
        rvChat = view.findViewById(R.id.rvChatInitiative);
    }

    /**
     * initRecycler
     */
    private void initRecycler() {
        adapter = new ChatAdapter(new ArrayList<>(), JointlyApplication.getCurrentSignInUser().getEmail());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        rvChat.setHasFixedSize(true);
        rvChat.setAdapter(adapter);
        rvChat.setLayoutManager(linearLayoutManager);
    }

    /**
     * setListeners
     */
    private void setListeners() {
        imgbtnSend.setOnClickListener(v -> {
            presenter.sendMessage(initiative, user.getEmail(), tvMessage.getText().toString());
        });

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    HashMap<Object, Object> result = (HashMap<Object, Object>) dataSnapshot.getValue();
                    if(result != null && !result.isEmpty()) {
                        List<ChatListAdapter> newMessages = new ArrayList<>();
                        for (Iterator<Object> it = result.values().iterator(); it.hasNext(); ) {
                            Chat post = new Chat();
                            HashMap<String, Object> e = (HashMap<String, Object>) it.next();
                            String pathImagenUser = (String) e.get("pathImageUser");
                            post.setIdInitiative((Long) ((HashMap) e.get("chat")).get("idInitiative"));
                            post.setDateMessage((String) ((HashMap) e.get("chat")).get("dateMessage"));
                            post.setUserEmail((String) ((HashMap) e.get("chat")).get("userEmail"));
                            post.setMessage((String) ((HashMap) e.get("chat")).get("message"));
                            post.setUuid((String) ((HashMap) e.get("chat")).get("uuid"));
                            post.setRead((Boolean) ((HashMap) e.get("chat")).get("read"));
                            post.setIs_sync((Boolean) ((HashMap) e.get("chat")).get("is_sync"));
                            newMessages.add(new ChatListAdapter(pathImagenUser, post));
                        }

                        adapter.update(newMessages);
                    } else {
                        setNoData();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());

            }
        };

        databaseReference.addValueEventListener(postListener);
    }

    @Override
    public void onStart() {
        super.onStart();
//        presenter.loadChat(initiative);
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
    public void onSuccessSendMessage(ChatListAdapter chat) {
        if(llNoData.getVisibility() == View.VISIBLE)
            llNoData.setVisibility(View.GONE);

//        adapter.addMessage(chat);
        tvMessage.setText("");
    }

    @Override
    public void onSuccess(List<ChatListAdapter> list) {
        if(llNoData.getVisibility() == View.VISIBLE)
            llNoData.setVisibility(View.GONE);

        hideProgress();
        adapter.update(list);
    }
}