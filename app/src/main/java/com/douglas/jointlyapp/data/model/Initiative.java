package com.douglas.jointlyapp.data.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

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
    @NonNull
    private String refCode;

    @Ignore
    private int countUserJoined;

    @Ignore
    public Initiative() {
    }

    @Ignore
    public Initiative(int id, String name, String createdAt, String targetDate, String description, String targetArea, String location) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.targetDate = targetDate;
        this.description = description;
        this.targetArea = targetArea;
        this.location = location;
    }

    @Ignore
    public Initiative(String name, String createdAt, String targetDate, String description, String targetArea, String location, Bitmap imagen, String targetAmount, String status, String createdBy) {
        this.name = name;
        this.createdAt = createdAt;
        this.targetDate = targetDate;
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
     * @param description
     * @param targetArea
     * @param location
     * @param imagen
     * @param targetAmount
     * @param status
     * @param createdBy
     */
    public Initiative(int id, String name, String createdAt, String targetDate, String description, String targetArea, String location, Bitmap imagen, String targetAmount, String status, String createdBy, String refCode) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.targetDate = targetDate;
        this.description = description;
        this.targetArea = targetArea;
        this.location = location;
        this.imagen = imagen;
        this.targetAmount = targetAmount;
        this.status = status;
        this.createdBy = createdBy;
        this.refCode = refCode;
    }

    @Ignore
    protected Initiative(Parcel in) {
        id = in.readInt();
        name = in.readString();
        createdAt = in.readString();
        targetDate = in.readString();
        description = in.readString();
        targetArea = in.readString();
        location = in.readString();
        imagen = in.readParcelable(Bitmap.class.getClassLoader());
        targetAmount = in.readString();
        status = in.readString();
        createdBy = in.readString();
        refCode = in.readString();
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

    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
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
                ", description='" + description + '\'' +
                ", targetArea='" + targetArea + '\'' +
                ", location='" + location + '\'' +
                ", imagen=" + imagen +
                ", targetAmount='" + targetAmount + '\'' +
                ", status='" + status + '\'' +
                ", created_by='" + createdBy + '\'' +
                ", ref_code='" + refCode + '\'' +
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
        dest.writeString(description);
        dest.writeString(targetArea);
        dest.writeString(location);
        imagen.writeToParcel(dest, flags);
        dest.writeString(targetAmount);
        dest.writeString(status);
        dest.writeString(createdBy);
        dest.writeString(refCode);
    }
}
