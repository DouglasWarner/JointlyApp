package com.douglas.jointlyapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Initiative implements Comparable<Initiative>, Serializable, Parcelable {

    private int id;
    private String name;
    private String createdAt;
    private String targetDate;
    private String targetHour;
    private String description;
    private String targetArea;
    private String location;
    private String imagen;
    private String targetAmount;
    private String status;
    private String createdBy;

    public Initiative() {
    }

    public Initiative(int id, String name, String createdAt, String targetDate, String targetHour, String description, String targetArea, String location, String imagen, String targetAmount, String status, String createdBy) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.targetDate = targetDate;
        this.targetHour = targetHour;
        this.description = description;
        this.targetArea = targetArea;
        this.location = location;
        this.imagen = imagen;
        this.targetAmount = targetAmount;
        this.status = status;
        this.createdBy = createdBy;
    }

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

    public String getTargetHour() {
        return targetHour;
    }

    public void setTargetHour(String targetHour) {
        this.targetHour = targetHour;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
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
                ", targetHour='" + targetHour + '\'' +
                ", description='" + description + '\'' +
                ", targetArea='" + targetArea + '\'' +
                ", location='" + location + '\'' +
                ", imagen='" + imagen + '\'' +
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
        dest.writeString(name);
        dest.writeString(name);
        dest.writeString(name);
        dest.writeString(name);
        dest.writeString(name);
        dest.writeString(name);
    }
}
