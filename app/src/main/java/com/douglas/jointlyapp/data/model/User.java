package com.douglas.jointlyapp.data.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "user",
        indices = {@Index(value = "email", unique = true)})
public class User implements Comparable<User>, Serializable, Parcelable {

    public static final String TAG = "User";

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    @ColumnInfo(name = "email")
    private String email;
    @NonNull
    private String password;
    @NonNull
    private String name;
    private String phone;
    private Bitmap imagen;
    private String location;
    private String description;
    @NonNull
    private String createdAt;

    @Ignore
    private int userFollows;

    @Ignore
    public User() {
    }

    @Ignore
    public User(@NonNull String email, @NonNull String password, @NonNull String name, String phone, Bitmap imagen, String location, String description, @NonNull String createdAt) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.imagen = imagen;
        this.location = location;
        this.description = description;
        this.createdAt = createdAt;
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
     * @param createdAt
     */
    public User(int id, @NonNull String email, @NonNull String password, @NonNull String name, String phone, Bitmap imagen, String location, String description, @NonNull String createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.imagen = imagen;
        this.location = location;
        this.description = description;
        this.createdAt = createdAt;
    }

    @Ignore
    protected User(Parcel in) {
        id = in.readInt();
        email = in.readString();
        password = in.readString();
        name = in.readString();
        phone = in.readString();
        imagen = in.readParcelable(Bitmap.class.getClassLoader());
        location = in.readString();
        description = in.readString();
        createdAt = in.readString();
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getUserFollows() {
        return userFollows;
    }

    public void setUserFollows(int userFollows) {
        this.userFollows = userFollows;
    }

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
                ", createdAt='" + createdAt + '\'' +
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
        dest.writeInt(id);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(name);
        dest.writeString(phone);
        imagen.writeToParcel(dest, flags);
        dest.writeString(location);
        dest.writeString(description);
        dest.writeString(createdAt);
    }
}
