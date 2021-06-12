package com.douglas.jointlyapp.data.repository;

import com.douglas.jointlyapp.data.JointlyDatabase;
import com.douglas.jointlyapp.data.dao.ChatDao;
import com.douglas.jointlyapp.data.model.Chat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Entity that connect to SQlite
 */
public class ChatRepository {

    private static final ChatRepository chatRepository;
    private final ChatDao chatDao;

    static {
        chatRepository = new ChatRepository();
    }

    private ChatRepository() {
        JointlyDatabase db = JointlyDatabase.getDatabase();
        chatDao = db.chatDao();
    }

    public static ChatRepository getInstance() {
        return chatRepository;
    }

    /**
     * Devuelve la lista de mensajes de la iniciativa
     * @param idInitiative
     * @return list
     */
    public List<Chat> getChatInitiative(long idInitiative) {
        List<Chat> result = new ArrayList<>();
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> chatDao.getChatInitiative(idInitiative)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Inserta un mensaje en el chat
     * @param chat
     * @return id chat
     */
    public long insert(Chat chat) {
        long result = 0;
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> chatDao.insert(chat)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
