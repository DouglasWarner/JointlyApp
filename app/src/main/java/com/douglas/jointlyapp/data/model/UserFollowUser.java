package com.douglas.jointlyapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "userFollowUser", primaryKeys = {"user","user_follow"},
foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "email", childColumns = "user"),
            @ForeignKey(entity = User.class, parentColumns = "email", childColumns = "user_follow")},
            indices = {@Index("user"), @Index("user_follow")})
public class UserFollowUser implements Parcelable {

    public static final String TAG = "UserFollowUser";
    public static final String TABLE_NAME = "userFollowUser";

    //region Variables

    @SerializedName("user")
    @NonNull
    private String user;

    @SerializedName("user_follow")
    @NonNull
    private String user_follow;

    @SerializedName("is_deleted")
    @ColumnInfo(defaultValue = "0")
    private boolean is_deleted;

    @SerializedName("is_sync")
    @ColumnInfo(defaultValue = "0")
    private boolean is_sync;

    //endregion

    //region Contructs

    @Ignore
    public UserFollowUser() {
    }

    /**
     * Create a new follow
     * @param user
     * @param user_follow
     */
    public UserFollowUser(String user, String user_follow, boolean is_deleted, boolean is_sync) {
        this.user = user;
        this.user_follow = user_follow;
        this.is_deleted = is_deleted;
        this.is_sync = is_sync;
    }

    @Ignore
    public UserFollowUser(String user, String user_follow) {
        this.user = user;
        this.user_follow = user_follow;
    }

    @Ignore
    protected UserFollowUser(Parcel in) {
        user = in.readString();
        user_follow = in.readString();
        is_deleted = in.readByte() != 0;
        is_sync = in.readByte() != 0;
    }

    //endregion

    public static final Creator<UserFollowUser> CREATOR = new Creator<UserFollowUser>() {
        @Override
        public UserFollowUser createFromParcel(Parcel in) {
            return new UserFollowUser(in);
        }

        @Override
        public UserFollowUser[] newArray(int size) {
            return new UserFollowUser[size];
        }
    };

    //region GETTERs and SETTERs

    @NonNull
    public String getUser() {
        return user;
    }

    public void setUser(@NonNull String user) {
        this.user = user;
    }

    @NonNull
    public String getUser_follow() {
        return user_follow;
    }

    public void setUser_follow(@NonNull String user_follow) {
        this.user_follow = user_follow;
    }

    public boolean isIs_sync() {
        return is_sync;
    }

    public void setIs_sync(boolean is_sync) {
        this.is_sync = is_sync;
    }

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

//endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserFollowUser that = (UserFollowUser) o;

        if (!user.equals(that.user)) return false;
        return user_follow.equals(that.user_follow);
    }

    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + user_follow.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UserFollowUser{" +
                "user='" + user + '\'' +
                ", user_follow='" + user_follow + '\'' +
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
        dest.writeString(user);
        dest.writeString(user_follow);
        dest.writeByte((byte)(is_deleted ? 1:0));
        dest.writeByte((byte)(is_sync ? 1:0));
    }
}
