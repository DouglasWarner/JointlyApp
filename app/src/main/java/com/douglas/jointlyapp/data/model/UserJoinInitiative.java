package com.douglas.jointlyapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;


@Entity(tableName = "userJoinInitiative", primaryKeys = {"idInitiative","idUser","joined_at"},
    foreignKeys = {@ForeignKey(entity = Initiative.class, parentColumns = "id", childColumns = "idInitiative"),
                @ForeignKey(entity = User.class, parentColumns = "email", childColumns = "idUser")},
    indices = @Index("idUser"))
public class UserJoinInitiative implements Parcelable {

    public static final String TAG = "UserJoinInitiative";

    @NonNull
    private int idInitiative;
    @NonNull
    private String idUser;
    @NonNull
    private String joined_at;

    @Ignore
    public UserJoinInitiative() {
    }

    public UserJoinInitiative(int idInitiative, String idUser, @NonNull String joined_at) {
        this.idInitiative = idInitiative;
        this.idUser = idUser;
        this.joined_at = joined_at;
    }

    @Ignore
    protected UserJoinInitiative(Parcel in) {
        idInitiative = in.readInt();
        idUser = in.readString();
        joined_at = in.readString();
    }

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

    public int getIdInitiative() {
        return idInitiative;
    }

    public void setIdInitiative(int idInitiative) {
        this.idInitiative = idInitiative;
    }

    @NonNull
    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(@NonNull String idUser) {
        this.idUser = idUser;
    }

    @NonNull
    public String getJoined_at() {
        return joined_at;
    }

    public void setJoined_at(@NonNull String joined_at) {
        this.joined_at = joined_at;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserJoinInitiative that = (UserJoinInitiative) o;

        if (idInitiative != that.idInitiative) return false;
        if (!idUser.equals(that.idUser)) return false;
        return joined_at.equals(that.joined_at);
    }

    @Override
    public int hashCode() {
        int result = idInitiative;
        result = 31 * result + idUser.hashCode();
        result = 31 * result + joined_at.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UserJoinInitiative{" +
                "idInitiative=" + idInitiative +
                ", idUser='" + idUser + '\'' +
                ", joined_at='" + joined_at + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idInitiative);
        dest.writeString(idUser);
        dest.writeString(joined_at);
    }
}
