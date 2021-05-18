package com.douglas.jointlyapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

@Entity(tableName = "chat", primaryKeys = {"dateMessage","idInitiative","userEmail"},
        foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "email", childColumns = "userEmail"),
                    @ForeignKey(entity = Initiative.class, parentColumns = "id", childColumns = "idInitiative")},
        indices = {@Index("userEmail")})
public class Chat implements Parcelable {

    @NonNull
    private String dateMessage;
    @NonNull
    private int idInitiative;
    @NonNull
    private String userEmail;
    private String message;

    @Ignore
    public Chat() {
    }

    /**
     * Create a new chat
     * @param dateMessage
     * @param idInitiative
     * @param userEmail
     * @param message
     */
    public Chat(String dateMessage, int idInitiative, String userEmail, String message) {
        this.dateMessage = dateMessage;
        this.idInitiative = idInitiative;
        this.userEmail = userEmail;
        this.message = message;
    }

    @Ignore
    protected Chat(Parcel in) {
        dateMessage = in.readString();
        idInitiative = in.readInt();
        userEmail = in.readString();
        message = in.readString();
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };

    public String getDateMessage() {
        return dateMessage;
    }

    public void setDateMessage(String dateMessage) {
        this.dateMessage = dateMessage;
    }

    public int getIdInitiative() {
        return idInitiative;
    }

    public void setIdInitiative(int idInitiative) {
        this.idInitiative = idInitiative;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chat chat = (Chat) o;

        if (idInitiative != chat.idInitiative) return false;
        if (dateMessage != null ? !dateMessage.equals(chat.dateMessage) : chat.dateMessage != null)
            return false;
        return userEmail != null ? userEmail.equals(chat.userEmail) : chat.userEmail == null;
    }

    @Override
    public int hashCode() {
        int result = dateMessage != null ? dateMessage.hashCode() : 0;
        result = 31 * result + idInitiative;
        result = 31 * result + (userEmail != null ? userEmail.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "dateMessage='" + dateMessage + '\'' +
                ", idInitiative=" + idInitiative +
                ", userEmail='" + userEmail + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dateMessage);
        dest.writeInt(idInitiative);
        dest.writeString(userEmail);
        dest.writeString(message);
    }
}
