package com.douglas.jointlyapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Entity support for database targetArea
 */
@Entity(tableName = "targetArea", primaryKeys = "idTargetArea")
public class TargetArea implements Serializable, Parcelable {

    public static final String TAG = "TargetArea";
    public static final String TABLE_NAME = "TargetArea";

    //region Variables

    @SerializedName("id_target_area")
    @NonNull
    private long idTargetArea;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    //endregion

    //region Constructs

    @Ignore
    public TargetArea() {
    }

    /**
     * Create a new targetArea
     * @param idTargetArea
     * @param name
     * @param description
     */
    public TargetArea(long idTargetArea, String name, String description) {
        this.idTargetArea = idTargetArea;
        this.name = name;
        this.description = description;
    }

    @Ignore
    protected TargetArea(Parcel in) {
        idTargetArea = in.readLong();
        name = in.readString();
        description = in.readString();
    }

    //endregion

    public static final Creator<TargetArea> CREATOR = new Creator<TargetArea>() {
        @Override
        public TargetArea createFromParcel(Parcel in) {
            return new TargetArea(in);
        }

        @Override
        public TargetArea[] newArray(int size) {
            return new TargetArea[size];
        }
    };

    //region GETTERs and SETTERs

    public long getIdTargetArea() {
        return idTargetArea;
    }

    public void setIdTargetArea(long idTargetArea) {
        this.idTargetArea = idTargetArea;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TargetArea that = (TargetArea) o;

        return idTargetArea == that.idTargetArea;
    }

    @Override
    public int hashCode() {
        return (int) (idTargetArea ^ (idTargetArea >>> 32));
    }

    @Override
    public String toString() {
        return "TargetArea{" +
                "idTargetArea=" + idTargetArea +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(idTargetArea);
        dest.writeString(name);
        dest.writeString(description);
    }
}
