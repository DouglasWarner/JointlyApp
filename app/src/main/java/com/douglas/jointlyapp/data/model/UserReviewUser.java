package com.douglas.jointlyapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "userReviewUser", primaryKeys = {"user", "user_review", "date"},
    foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "email", childColumns = "user"),
                @ForeignKey(entity = User.class, parentColumns = "email", childColumns = "user_review")},
    indices = {@Index("user"), @Index("user_review")})
public class UserReviewUser implements Serializable, Parcelable {

    public static final String TAG = "UserReviewUser";
    public static final String TABLE_NAME = "userReviewUser";

    //region Variables

    @SerializedName("user")
    @NonNull
    private String user;

    @SerializedName("user_review")
    @NonNull
    private String user_review;

    @SerializedName("date")
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    @NonNull
    private String date;

    @SerializedName("review")
    private String review;

    @SerializedName("stars")
    @NonNull
    private int stars;

    @SerializedName("is_deleted")
    @ColumnInfo(defaultValue = "0")
    private boolean is_deleted;

    @SerializedName("is_sync")
    @ColumnInfo(defaultValue = "0")
    private boolean is_sync;

    //endregion

    //region Contructs

    @Ignore
    public UserReviewUser() {
    }

    @Ignore
    public UserReviewUser(@NonNull String user, @NonNull String user_review, @NonNull String date, String review, int stars) {
        this.user = user;
        this.user_review = user_review;
        this.date = date;
        this.review = review;
        this.stars = stars;
    }

    /**
     * Create a new review
     * @param user
     * @param user_review
     * @param date
     * @param review
     * @param stars
     */
    public UserReviewUser(String user, String user_review, String date, String review, int stars, boolean is_deleted, boolean is_sync) {
        this.user = user;
        this.user_review = user_review;
        this.date = date;
        this.review = review;
        this.stars = stars;
        this.is_deleted = is_deleted;
        this.is_sync = is_sync;
    }

    @Ignore
    protected UserReviewUser(Parcel in) {
        user = in.readString();
        user_review = in.readString();
        date = in.readString();
        review = in.readString();
        stars = in.readInt();
        is_deleted = in.readByte() != 0;
        is_sync = in.readByte() != 0;
    }

    //endregion

    public static final Creator<UserReviewUser> CREATOR = new Creator<UserReviewUser>() {
        @Override
        public UserReviewUser createFromParcel(Parcel in) {
            return new UserReviewUser(in);
        }

        @Override
        public UserReviewUser[] newArray(int size) {
            return new UserReviewUser[size];
        }
    };

    //region GETTERs and SETTERs

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser_review() {
        return user_review;
    }

    public void setUser_review(String user_review) {
        this.user_review = user_review;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
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

        UserReviewUser that = (UserReviewUser) o;

        if (!user.equals(that.user)) return false;
        if (!user_review.equals(that.user_review)) return false;
        return date.equals(that.date);
    }

    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + user_review.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UserReviewUser{" +
                "user='" + user + '\'' +
                ", user_review='" + user_review + '\'' +
                ", date='" + date + '\'' +
                ", review='" + review + '\'' +
                ", stars=" + stars +
                ", is_deleted=" + is_deleted +
                ", is_sync=" + is_sync +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user);
        dest.writeString(user_review);
        dest.writeString(date);
        dest.writeString(review);
        dest.writeInt(stars);
        dest.writeByte((byte)(is_deleted ? 1:0));
        dest.writeByte((byte)(is_sync ? 1:0));
    }
}
