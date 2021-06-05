package com.douglas.jointlyapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

@Entity(tableName = "userFollowUser", primaryKeys = {"user","user_follow"},
foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "email", childColumns = "user"),
            @ForeignKey(entity = User.class, parentColumns = "email", childColumns = "user_follow")},
            indices = {@Index("user"), @Index("user_follow")})
public class UserFollowUser implements Parcelable {

    public static final String TAG = "UserFollowUser";
    public static final String TABLE_NAME = "userFollowUser";

    //region Variables

    @NonNull
    private String user;
    @NonNull
    private String user_follow;
    private boolean is_deleted;

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
    public UserFollowUser(String user, String user_follow, boolean is_deleted) {
        this.user = user;
        this.user_follow = user_follow;
        this.is_deleted = is_deleted;
    }

    @Ignore
    protected UserFollowUser(Parcel in) {
        user = in.readString();
        user_follow = in.readString();
        is_deleted = in.readByte() != 0;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser_follow() {
        return user_follow;
    }

    public void setUser_follow(String user_follow) {
        this.user_follow = user_follow;
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

        if (user != that.user) return false;
        return user_follow == that.user_follow;
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
                "idUser=" + user +
                ", idUserFollowed=" + user_follow +
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
    }
}
