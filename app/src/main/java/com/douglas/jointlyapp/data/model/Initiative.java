package com.douglas.jointlyapp.data.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "initiative",
        foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "email", childColumns = "createdBy")},
        indices = {@Index("createdBy")})
public class Initiative implements Comparable<Initiative>, Serializable, Parcelable {

    public static final String TAG = "Initiative";

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String name;
    @NonNull
    private String createdAt;
    @NonNull
    private String targetDate;
    @NonNull
    private String targetTime;
    private String description;
    @NonNull
    private String targetArea;
    @NonNull
    private String location;
    private Bitmap imagen;
    @NonNull
    private String targetAmount;
    private String status;
    @NonNull
    private String createdBy;
    @Ignore
    private int countUserJoined;

    @Ignore
    public Initiative() {
    }

    @Ignore
    public Initiative(int id, String name, String createdAt, String targetDate, String targetTime, String description, String targetArea, String location) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.targetDate = targetDate;
        this.targetTime = targetTime;
        this.description = description;
        this.targetArea = targetArea;
        this.location = location;
    }

    @Ignore
    public Initiative(String name, String createdAt, String targetDate, String targetTime, String description, String targetArea, String location, Bitmap imagen, String targetAmount, String status, String createdBy) {
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
    public Initiative(int id, String name, String createdAt, String targetDate, String targetTime, String description, String targetArea, String location, Bitmap imagen, String targetAmount, String status, String createdBy) {
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
    }

    @Ignore
    protected Initiative(Parcel in) {
        id = in.readInt();
        name = in.readString();
        createdAt = in.readString();
        targetDate = in.readString();
        targetTime = in.readString();
        description = in.readString();
        targetArea = in.readString();
        location = in.readString();
        imagen = in.readParcelable(Bitmap.class.getClassLoader());
        targetAmount = in.readString();
        status = in.readString();
        createdBy = in.readString();
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

    public Bitmap getImagen()
    {
        return imagen;
    }

    public void setImagen(Bitmap imagen)
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

    public int getCountUserJoined() {
        return countUserJoined;
    }

    public void setCountUserJoined(int countUserJoined) {
        this.countUserJoined = countUserJoined;
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
    public String toString() {
        return "Initiative{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", targetDate='" + targetDate + '\'' +
                ", targetTime='" + targetTime + '\'' +
                ", description='" + description + '\'' +
                ", targetArea='" + targetArea + '\'' +
                ", location='" + location + '\'' +
                ", imagen=" + imagen +
                ", targetAmount='" + targetAmount + '\'' +
                ", status='" + status + '\'' +
                ", createdBy='" + createdBy + '\'' +
                '}';
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
    }
}
