package com.douglas.jointlyapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Entity support for database countries
 */
@Entity(tableName = "countries", primaryKeys = "idCountries")
public class Countries implements Parcelable, Serializable {

    public static final String TAG = "Countries";
    public static final String TABLE_NAME = "Countries";

    //region Variables

    @SerializedName("id")
    @NonNull
    private long idCountries;

    @SerializedName("name")
    private String name;

    @SerializedName("ccaa")
    private String ccaa;

    //endregion

    //region Constructs

    @Ignore
    public Countries() {
    }

    /**
     * Create a new Countries
     * @param idCountries
     * @param name
     * @param ccaa
     */
    public Countries(long idCountries, String name, String ccaa) {
        this.idCountries = idCountries;
        this.name = name;
        this.ccaa = ccaa;
    }

    @Ignore
    protected Countries(Parcel in) {
        idCountries = in.readLong();
        name = in.readString();
        ccaa = in.readString();
    }

    //endregion

    public static final Creator<Countries> CREATOR = new Creator<Countries>() {
        @Override
        public Countries createFromParcel(Parcel in) {
            return new Countries(in);
        }

        @Override
        public Countries[] newArray(int size) {
            return new Countries[size];
        }
    };

    //region GETTERs and SETTERs

    public long getIdCountries() {
        return idCountries;
    }

    public void setIdCountries(long idCountries) {
        this.idCountries = idCountries;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCcaa() {
        return ccaa;
    }

    public void setCcaa(String ccaa) {
        this.ccaa = ccaa;
    }

    //endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Countries countries = (Countries) o;

        return idCountries == countries.idCountries;
    }

    @Override
    public int hashCode() {
        return (int) (idCountries ^ (idCountries >>> 32));
    }

    @Override
    public String toString() {
        return "Countries{" +
                "idCountries=" + idCountries +
                ", name='" + name + '\'' +
                ", ccaa='" + ccaa + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(idCountries);
        dest.writeString(name);
        dest.writeString(ccaa);
    }
}
