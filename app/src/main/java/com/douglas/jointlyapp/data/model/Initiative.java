package com.douglas.jointlyapp.data.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Initiative implements Comparable<Initiative>, Serializable, Parcelable {

    private int id;
    private String name;
    private String createdAt;
    private String targetDate;
    private String targetTime;
    private String description;
    private String targetArea;
    private String location;
    private Uri imagen;
    private String targetAmount;
    private String status;
    private String createdBy;
    private List<User> userJoined;

    public Initiative() {
        this.userJoined = new ArrayList<>();
    }

    public Initiative(int id, String name, String createdAt, String targetDate, String targetTime, String description, String targetArea, String location) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.targetDate = targetDate;
        this.targetTime = targetTime;
        this.description = description;
        this.targetArea = targetArea;
        this.location = location;
        this.userJoined = new ArrayList<>();
    }

    public Initiative(String name, String createdAt, String targetDate, String targetTime, String description, String targetArea, String location, Uri imagen, String targetAmount, String status, String createdBy) {
        this.name = name;
        this.createdAt = createdAt;
        this.targetDate = targetDate;
        this.targetTime = targetTime;
        this.description = description;
        this.targetArea = targetArea;
        this.location = location;
        this.imagen = imagen;
        this.targetAmount = targetAmount;
        this.status = status;
        this.createdBy = createdBy;
        this.userJoined = new ArrayList<>();
    }

    /**
     * Crea una iniciativa
     * @param id
     * @param name
     * @param createdAt
     * @param targetDate
     * @param targetTime
     * @param description
     * @param targetArea
     * @param location
     * @param imagen
     * @param targetAmount
     * @param status
     * @param createdBy
     */
    public Initiative(int id, String name, String createdAt, String targetDate, String targetTime, String description, String targetArea, String location, Uri imagen, String targetAmount, String status, String createdBy) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.targetDate = targetDate;
        this.targetTime = targetTime;
        this.description = description;
        this.targetArea = targetArea;
        this.location = location;
        this.imagen = imagen;
        this.targetAmount = targetAmount;
        this.status = status;
        this.createdBy = createdBy;
        this.userJoined = new ArrayList<>();
    }

    protected Initiative(Parcel in) {
        id = in.readInt();
        name = in.readString();
        createdAt = in.readString();
        targetDate = in.readString();
        targetTime = in.readString();
        description = in.readString();
        targetArea = in.readString();
        location = in.readString();
        imagen = in.readParcelable(Uri.class.getClassLoader());
        targetAmount = in.readString();
        status = in.readString();
        createdBy = in.readString();
        userJoined = in.createTypedArrayList(User.CREATOR);
    }

    public static final Creator<Initiative> CREATOR = new Creator<Initiative>() {
        @Override
        public Initiative createFromParcel(Parcel in) {
            return new Initiative(in);
        }

        @Override
        public Initiative[] newArray(int size) {
            return new Initiative[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(String targetDate) {
        this.targetDate = targetDate;
    }

    public String getTargetTime() {
        return targetTime;
    }

    public void setTargetTime(String targetTime) {
        this.targetTime = targetTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTargetArea() {
        return targetArea;
    }

    public void setTargetArea(String targetArea) {
        this.targetArea = targetArea;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Uri getImagen()
    {
        return imagen;
    }

    public void setImagen(Uri imagen)
    {
        this.imagen = imagen;
    }

    public String getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(String targetAmount) {
        this.targetAmount = targetAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<User> getUserJoined() {
        return userJoined;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Initiative that = (Initiative) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }


    @Override
    public int compareTo(Initiative initiative) {
        return this.compareTo(initiative);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(createdAt);
        dest.writeString(targetDate);
        dest.writeString(targetTime);
        dest.writeString(description);
        dest.writeString(targetArea);
        dest.writeString(location);
        imagen.writeToParcel(dest, flags);
        dest.writeString(targetAmount);
        dest.writeString(status);
        dest.writeString(createdBy);
        dest.writeTypedList(userJoined);
    }
}
