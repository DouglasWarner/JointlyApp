package com.douglas.jointlyapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "chat", primaryKeys = {"dateMessage","idInitiative","userEmail"},
        foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "email", childColumns = "userEmail"),
                    @ForeignKey(entity = Initiative.class, parentColumns = "id", childColumns = "idInitiative")},
        indices = {@Index("userEmail")})
public class Chat implements Parcelable {

    public static final String TAG = "Chat";
    public static final String TABLE_NAME = "chat";

    //region Variables

    @SerializedName("date_message")
    @NonNull
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    private String dateMessage;

    @SerializedName("id_initiative")
    @NonNull
    private long idInitiative;

    @SerializedName("user_email")
    @NonNull
    private String userEmail;

    @SerializedName("message")
    private String message;

    @SerializedName("is_sync")
    @ColumnInfo(defaultValue = "0")
    private boolean is_sync;

    //endregion

    //region Construct

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
    public Chat(String dateMessage, long idInitiative, String userEmail, String message, boolean is_sync) {
        this.dateMessage = dateMessage;
        this.idInitiative = idInitiative;
        this.userEmail = userEmail;
        this.message = message;
        this.is_sync = is_sync;
    }

    @Ignore
    protected Chat(Parcel in) {
        dateMessage = in.readString();
        idInitiative = in.readLong();
        userEmail = in.readString();
        message = in.readString();
        is_sync = in.readByte() != 0;
    }

    //endregion

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

    //region GETTERs and SETTERs

    @NonNull
    public String getDateMessage() {
        return dateMessage;
    }

    public void setDateMessage(String dateMessage) {
        this.dateMessage = dateMessage;
    }

    public long getIdInitiative() {
        return idInitiative;
    }

    public void setIdInitiative(long idInitiative) {
        this.idInitiative = idInitiative;
    }

    @NonNull
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(@NonNull String userEmail) {
        this.userEmail = userEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIs_sync() {
        return is_sync;
    }

    public void setIs_sync(boolean is_sync) {
        this.is_sync = is_sync;
    }

    //endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chat chat = (Chat) o;

        if (idInitiative != chat.idInitiative) return false;
        if (!dateMessage.equals(chat.dateMessage)) return false;
        return userEmail.equals(chat.userEmail);
    }

    @Override
    public int hashCode() {
        int result = dateMessage.hashCode();
        result = 31 * result + (int) (idInitiative ^ (idInitiative >>> 32));
        result = 31 * result + userEmail.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "dateMessage='" + dateMessage + '\'' +
                ", idInitiative=" + idInitiative +
                ", userEmail='" + userEmail + '\'' +
                ", message='" + message + '\'' +
                ", is_sync=" + is_sync +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dateMessage);
        dest.writeLong(idInitiative);
        dest.writeString(userEmail);
        dest.writeString(message);
        dest.writeByte((byte)(is_sync ? 1:0));
    }
}
