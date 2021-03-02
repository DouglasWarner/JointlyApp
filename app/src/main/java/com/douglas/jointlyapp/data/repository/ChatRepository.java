package com.douglas.jointlyapp.data.repository;

import com.douglas.jointlyapp.data.JointlyDatabase;
import com.douglas.jointlyapp.data.dao.ChatDao;
import com.douglas.jointlyapp.data.model.Chat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ChatRepository {

    private static ChatRepository chatRepository;
    private JointlyDatabase db;
    private ChatDao chatDao;
    private List<Chat> list;

    static {
        chatRepository = new ChatRepository();
    }

    private ChatRepository()
    {
        this.list = new ArrayList<>();
        db = JointlyDatabase.getDatabase();
        chatDao = db.chatDao();
    }

    public static ChatRepository getInstance()
    {
        return chatRepository;
    }

    /**
     * Devuelve la lista de mensajes de la iniciativa
     * @param idInitiative
     * @return
     */
    public List<Chat> getChatInitiative(int idInitiative)
    {
        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> chatDao.getChatInitiative(idInitiative)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            return list;
        }
    }

    /**
     * Inserta un mensaje en el chat
     * @param chat
     * @return
     */
    public long insert(Chat chat)
    {
        long result = 0;
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(()-> chatDao.insert(chat)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }finally {
            return result;
        }
    }
}
