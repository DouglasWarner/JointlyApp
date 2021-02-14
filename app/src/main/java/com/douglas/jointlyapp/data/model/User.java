package com.douglas.jointlyapp.data.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class User implements Comparable<User>, Serializable, Parcelable {

    private int id;
    private String email;
    private String password;
    private String name;
    private String phone;
    private Uri imagen;
    private String location;
    private String description;
    private String createdAt;
    private List<User> userFollowed;
    private List<User> userFollowers;

    public User() {
        userFollowed = new ArrayList<>();
        userFollowers = new ArrayList<>();
    }

    public User(int id, String email, String password, String name, String phone, Uri imagen, String location, String description, String createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.imagen = imagen;
        this.location = location;
        this.description = description;
        this.createdAt = createdAt;
        userFollowed = new ArrayList<>();
        userFollowers = new ArrayList<>();
    }


    protected User(Parcel in) {
        id = in.readInt();
        email = in.readString();
        password = in.readString();
        name = in.readString();
        phone = in.readString();
        imagen = in.readParcelable(Uri.class.getClassLoader());
        location = in.readString();
        description = in.readString();
        createdAt = in.readString();
        userFollowed = in.createTypedArrayList(User.CREATOR);
        userFollowers = in.createTypedArrayList(User.CREATOR);
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

    public Uri getImagen() {
        return imagen;
    }

    public void setImagen(Uri imagen) {
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

    public List<User> getUserFollowed() {
        return userFollowed;
    }

    public List<User> getUserFollowers() {
        return userFollowers;
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
                ", imagen='" + imagen + '\'' +
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
        dest.writeTypedList(userFollowed);
        dest.writeTypedList(userFollowers);
    }
}
