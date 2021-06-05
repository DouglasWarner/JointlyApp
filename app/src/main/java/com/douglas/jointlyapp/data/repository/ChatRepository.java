package com.douglas.jointlyapp.data.repository;

import com.douglas.jointlyapp.data.JointlyDatabase;
import com.douglas.jointlyapp.data.dao.ChatDao;
import com.douglas.jointlyapp.data.model.Chat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ChatRepository {

    private static final ChatRepository chatRepository;
    private final ChatDao chatDao;
    private List<Chat> list;

    static {
        chatRepository = new ChatRepository();
    }

    private ChatRepository()
    {
        this.list = new ArrayList<>();
        JointlyDatabase db = JointlyDatabase.getDatabase();
        chatDao = db.chatDao();
    }

    public static ChatRepository getInstance()
    {
        return chatRepository;
    }

    /**
     * Devuelve la lista de mensajes de la iniciativa
     * @param idInitiative
     * @return list
     */
    public List<Chat> getChatInitiative(long idInitiative) {
        List<Chat> result;
        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> chatDao.getChatInitiative(idInitiative)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            result = list;
        }
        return result;
    }

    /**
     * Inserta un mensaje en el chat
     * @param chat
     * @return id chat
     */
    public long insert(Chat chat) {
        long res;
        long result = 0;
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> chatDao.insert(chat)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            res = result;
        }
        return res;
    }

    /**
     * Update or Insert for Sync with the API
     * @param list
     */
    public void upsertChat(List<Chat> list) {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> chatDao.upsert(list));
    }

}
