package com.douglas.jointlyapp.data.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "initiative",
        foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "email", childColumns = "created_by")},
        indices = {@Index("created_by")})
public class Initiative implements Comparable<Initiative>, Serializable, Parcelable {

    public static final String TAG = "Initiative";
    public static final String TABLE_NAME = "initiative";

    //region Variables

    //TODO quizas autoincrement
    @SerializedName("id")
    @NonNull
    @PrimaryKey
    private long id;

    @SerializedName("name")
    @NonNull
    private String name;

    @SerializedName("created_at")
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    private String created_at;

    @SerializedName("target_date")
    @NonNull
    private String target_date;

    @SerializedName("description")
    private String description;

    @SerializedName("target_area")
    @NonNull
    private String target_area;

    @SerializedName("location")
    @NonNull
    private String location;

    @SerializedName("imagen")
    private Bitmap imagen;

    @SerializedName("target_amount")
    @NonNull
    private String target_amount;

    @Ignore
    @SerializedName("status")
    private String status;

    @SerializedName("created_by")
    @NonNull
    private String created_by;

    @SerializedName("ref_code")
    @NonNull
    private String ref_code;

    @SerializedName("is_deleted")
    @ColumnInfo(defaultValue = "0")
    private boolean is_deleted;

    @SerializedName("is_sync")
    @ColumnInfo(defaultValue = "0")
    private boolean is_sync;

    //endregion

    //region Constructs

    @Ignore
    public Initiative() {
    }

    @Ignore
    public Initiative(long id, String name, String created_at, String target_date, String description, String target_area, String location) {
        this.id = id;
        this.name = name;
        this.created_at = created_at;
        this.target_date = target_date;
        this.description = description;
        this.target_area = target_area;
        this.location = location;
    }

    @Ignore
    public Initiative(String name, String created_at, String target_date, String description, String target_area,
                      String location, Bitmap imagen, String target_amount, String created_by) {
        this.name = name;
        this.created_at = created_at;
        this.target_date = target_date;
        this.description = description;
        this.target_area = target_area;
        this.location = location;
        this.imagen = imagen;
        this.target_amount = target_amount;
        this.created_by = created_by;
    }

    @Ignore
    public Initiative(String name, String created_at, String target_date, String description, String target_area,
                      String location, Bitmap imagen, String target_amount, String created_by, String ref_code) {
        this.name = name;
        this.created_at = created_at;
        this.target_date = target_date;
        this.description = description;
        this.target_area = target_area;
        this.location = location;
        this.imagen = imagen;
        this.target_amount = target_amount;
        this.created_by = created_by;
        this.ref_code = ref_code;
    }

    /**
     * Crea una iniciativa
     * @param id
     * @param name
     * @param created_at
     * @param target_date
     * @param description
     * @param target_area
     * @param location
     * @param imagen
     * @param target_amount
     * @param created_by
     */
    public Initiative(long id, String name, String created_at, String target_date, String description, String target_area,
                      String location, Bitmap imagen, String target_amount, String created_by, String ref_code,
                      boolean is_deleted, boolean is_sync) {
        this.id = id;
        this.name = name;
        this.created_at = created_at;
        this.target_date = target_date;
        this.description = description;
        this.target_area = target_area;
        this.location = location;
        this.imagen = imagen;
        this.target_amount = target_amount;
        this.created_by = created_by;
        this.ref_code = ref_code;
        this.is_deleted = is_deleted;
        this.is_sync = is_sync;
    }

    @Ignore
    protected Initiative(Parcel in) {
        id = in.readLong();
        name = in.readString();
        created_at = in.readString();
        target_date = in.readString();
        description = in.readString();
        target_area = in.readString();
        location = in.readString();
        imagen = in.readParcelable(Bitmap.class.getClassLoader());
        target_amount = in.readString();
        status = in.readString();
        created_by = in.readString();
        ref_code = in.readString();
        is_deleted = in.readByte() != 0;
        is_sync = in.readByte() != 0;
    }

    //endregion

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

    //region GETTERs and SETTERs

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTarget_date() {
        return target_date;
    }

    public void setTarget_date(String target_date) {
        this.target_date = target_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTarget_area() {
        return target_area;
    }

    public void setTarget_area(String target_area) {
        this.target_area = target_area;
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

    public String getTarget_amount() {
        return target_amount;
    }

    public void setTarget_amount(String target_amount) {
        this.target_amount = target_amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getRef_code() {
        return ref_code;
    }

    public void setRef_code(String ref_code) {
        this.ref_code = ref_code;
    }

    public boolean getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
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

        Initiative that = (Initiative) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Initiative{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdAt='" + created_at + '\'' +
                ", targetDate='" + target_date + '\'' +
                ", description='" + description + '\'' +
                ", targetArea='" + target_area + '\'' +
                ", location='" + location + '\'' +
                ", imagen=" + imagen +
                ", targetAmount='" + target_amount + '\'' +
                ", created_by='" + created_by + '\'' +
                ", ref_code='" + ref_code + '\'' +
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
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(created_at);
        dest.writeString(target_date);
        dest.writeString(description);
        dest.writeString(target_area);
        dest.writeString(location);
        imagen.writeToParcel(dest, flags);
        dest.writeString(target_amount);
        dest.writeString(status);
        dest.writeString(created_by);
        dest.writeString(ref_code);
        dest.writeByte((byte)(is_deleted ? 1:0));
        dest.writeByte((byte)(is_sync ? 1:0));
    }
}
