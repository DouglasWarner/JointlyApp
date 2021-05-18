package com.douglas.jointlyapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

@Entity(tableName = "userFollowUser", primaryKeys = {"idUser","idUserFollowed"},
foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "email", childColumns = "idUser"),
            @ForeignKey(entity = User.class, parentColumns = "email", childColumns = "idUserFollowed")},
            indices = {@Index("idUser"), @Index("idUserFollowed")})
public class UserFollowUser implements Parcelable {

    public static final String TAG = "UserFollowUser";

    @NonNull
    private String idUser;
    @NonNull
    private String idUserFollowed;

    @Ignore
    public UserFollowUser() {
    }

    /**
     * Create a new follow
     * @param idUser
     * @param idUserFollowed
     */
    public UserFollowUser(String idUser, String idUserFollowed) {
        this.idUser = idUser;
        this.idUserFollowed = idUserFollowed;
    }

    @Ignore
    protected UserFollowUser(Parcel in) {
        idUser = in.readString();
        idUserFollowed = in.readString();
    }

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

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdUserFollowed() {
        return idUserFollowed;
    }

    public void setIdUserFollowed(String idUserFollowed) {
        this.idUserFollowed = idUserFollowed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserFollowUser that = (UserFollowUser) o;

        if (idUser != that.idUser) return false;
        return idUserFollowed == that.idUserFollowed;
    }

    @Override
    public int hashCode() {
        int result = idUser.hashCode();
        result = 31 * result + idUserFollowed.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UserFollowUser{" +
                "idUser=" + idUser +
                ", idUserFollowed=" + idUserFollowed +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idUser);
        dest.writeString(idUserFollowed);
    }
}
