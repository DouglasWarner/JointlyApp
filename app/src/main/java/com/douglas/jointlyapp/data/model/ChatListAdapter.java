package com.douglas.jointlyapp.data.model;

/**
 * Entity assistant for chat list initiative
 */
public class ChatListAdapter {

    private String pathImageUser;
    private Chat chat;

    public ChatListAdapter() {
    }

    public ChatListAdapter(String pathImageUser, Chat chat) {
        this.pathImageUser = pathImageUser;
        this.chat = chat;
    }

    public String getPathImageUser() {
        return pathImageUser;
    }

    public void setPathImageUser(String pathImageUser) {
        this.pathImageUser = pathImageUser;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatListAdapter that = (ChatListAdapter) o;

        if (pathImageUser != null ? !pathImageUser.equals(that.pathImageUser) : that.pathImageUser != null)
            return false;
        return chat != null ? chat.equals(that.chat) : that.chat == null;
    }

    @Override
    public int hashCode() {
        int result = pathImageUser != null ? pathImageUser.hashCode() : 0;
        result = 31 * result + (chat != null ? chat.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ChatListAdapter{" +
                "pathImageUser='" + pathImageUser + '\'' +
                ", content=" + chat +
                '}';
    }
}
