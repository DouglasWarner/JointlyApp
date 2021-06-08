package com.douglas.jointlyapp.data.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "user",
        indices = {@Index(value = "email", unique = true)})
public class User implements Comparable<User>, Serializable, Parcelable {

    public static final String TAG = "User";
    public static final String TABLE_NAME = "user";

    //region Variables

    @SerializedName("id")
    @NonNull
    @PrimaryKey
    private long id;

    @SerializedName("email")
    @NonNull
    @ColumnInfo(name = "email")
    private String email;

    @SerializedName("password")
    @NonNull
    private String password;

    @SerializedName("name")
    @NonNull
    private String name;

    @SerializedName("phone")
    private String phone;

    @SerializedName("imagen")
    private Bitmap imagen;

    @SerializedName("location")
    private String location;

    @SerializedName("description")
    private String description;

    @SerializedName("created_at")
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    private String created_at;

    @SerializedName("is_sync")
    @ColumnInfo(defaultValue = "0")
    private boolean is_sync;

    //endregion

    //region Contructs

    @Ignore
    public User() {
    }

    @Ignore
    public User(@NonNull String email, @NonNull String password, @NonNull String name, String phone, Bitmap imagen, String location, String description, @NonNull String created_at) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.imagen = imagen;
        this.location = location;
        this.description = description;
        this.created_at = created_at;
    }

    /**
     * Crea un usuario
     * @param id
     * @param email
     * @param password
     * @param name
     * @param phone
     * @param imagen
     * @param location
     * @param description
     * @param created_at
     */
    public User(long id, @NonNull String email, @NonNull String password, @NonNull String name, String phone,
                Bitmap imagen, String location, String description, @NonNull String created_at,
                boolean is_sync) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.imagen = imagen;
        this.location = location;
        this.description = description;
        this.created_at = created_at;
        this.is_sync = is_sync;
    }

    @Ignore
    protected User(Parcel in) {
        id = in.readLong();
        email = in.readString();
        password = in.readString();
        name = in.readString();
        phone = in.readString();
        imagen = in.readParcelable(Bitmap.class.getClassLoader());
        location = in.readString();
        description = in.readString();
        created_at = in.readString();
        is_sync = in.readByte() != 0;
    }

    //endregion

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    //region GETTERs and SETTERs

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
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

        User user = (User) o;

        return email != null ? email.equals(user.email) : user.email == null;
    }

    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", imagen=" + imagen +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", created_at='" + created_at + '\'' +
                ", is_sync=" + is_sync +
                '}';
    }

    @Override
    public int compareTo(User user) {
        return this.compareTo(user);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(name);
        dest.writeString(phone);
        imagen.writeToParcel(dest, flags);
        dest.writeString(location);
        dest.writeString(description);
        dest.writeString(created_at);
        dest.writeByte((byte)(is_sync ? 1:0));
    }
}
