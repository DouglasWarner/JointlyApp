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

import java.io.Serializable;

/**
 * Entity UserJoinInitiative
 */
@Entity(tableName = "userJoinInitiative", primaryKeys = {"id_initiative","user_email"},
    foreignKeys = {@ForeignKey(entity = Initiative.class, parentColumns = "id", childColumns = "id_initiative"),
                @ForeignKey(entity = User.class, parentColumns = "email", childColumns = "user_email")},
    indices = @Index("user_email"))
public class UserJoinInitiative implements Serializable, Parcelable {

    public static final String TAG = "UserJoinInitiative";
    public static final String TABLE_NAME = "userJoinInitiative";

    //region Variables

    @SerializedName("id_initiative")
    @NonNull
    private long id_initiative;

    @SerializedName("user_email")
    @NonNull
    private String user_email;

    @SerializedName("date")
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    private String date;

    @SerializedName("type")
    @ColumnInfo(defaultValue = "0")
    private int type;

    @SerializedName("is_deleted")
    @ColumnInfo(defaultValue = "0")
    private boolean is_deleted;

    @SerializedName("is_sync")
    @ColumnInfo(defaultValue = "0")
    private boolean is_sync;

    //endregion

    //region Contructs

    @Ignore
    public UserJoinInitiative() {
    }

    @Ignore
    public UserJoinInitiative(long id_initiative, @NonNull String user_email, boolean is_deleted, boolean is_sync) {
        this.id_initiative = id_initiative;
        this.user_email = user_email;
        this.is_deleted = is_deleted;
        this.is_sync = is_sync;
    }

    /**
     * Create a new user join initiative
     * @param id_initiative
     * @param user_email
     * @param date
     * @param type
     */
    public UserJoinInitiative(long id_initiative, String user_email, String date, int type, boolean is_deleted, boolean is_sync) {
        this.id_initiative = id_initiative;
        this.user_email = user_email;
        this.date = date;
        this.type = type;
        this.is_deleted = is_deleted;
        this.is_sync = is_sync;
    }

    @Ignore
    protected UserJoinInitiative(Parcel in) {
        id_initiative = in.readLong();
        user_email = in.readString();
        date = in.readString();
        type = in.readInt();
        is_deleted = in.readByte() != 0;
        is_sync = in.readByte() != 0;
    }

    //endregion

    public static final Creator<UserJoinInitiative> CREATOR = new Creator<UserJoinInitiative>() {
        @Override
        public UserJoinInitiative createFromParcel(Parcel in) {
            return new UserJoinInitiative(in);
        }

        @Override
        public UserJoinInitiative[] newArray(int size) {
            return new UserJoinInitiative[size];
        }
    };

    //region GETTERs and SETTERs

    public long getId_initiative() {
        return id_initiative;
    }

    public void setId_initiative(long id_initiative) {
        this.id_initiative = id_initiative;
    }

    @NonNull
    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(@NonNull String user_email) {
        this.user_email = user_email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public boolean getIs_sync() {
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

        UserJoinInitiative that = (UserJoinInitiative) o;

        if (id_initiative != that.id_initiative) return false;
        return user_email.equals(that.user_email);
    }

    @Override
    public int hashCode() {
        int result = (int) (id_initiative ^ (id_initiative >>> 32));
        result = 31 * result + user_email.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UserJoinInitiative{" +
                "id_initiative=" + id_initiative +
                ", user_email='" + user_email + '\'' +
                ", date='" + date + '\'' +
                ", type=" + type +
                ", is_deleted=" + is_deleted +
                ", is_sync=" + is_sync +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id_initiative);
        dest.writeString(user_email);
        dest.writeString(date);
        dest.writeInt(type);
        dest.writeByte((byte)(is_deleted ? 1:0));
        dest.writeByte((byte)(is_sync ? 1:0));
    }
}
