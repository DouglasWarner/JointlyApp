package com.douglas.jointlyapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import java.io.Serializable;

@Entity(tableName = "userReviewUser", primaryKeys = {"user", "user_review"},
    foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "email", childColumns = "user"),
                @ForeignKey(entity = User.class, parentColumns = "email", childColumns = "user_review")},
    indices = {@Index("user"), @Index("user_review")})
public class UserReviewUser implements Serializable, Parcelable {

    public static final String TAG = "UserReviewUser";
    public static final String TABLE_NAME = "userReviewUser";

    //region Variables

    @NonNull
    private String user;
    @NonNull
    private String user_review;
    @NonNull
    private String date;
    private String review;
    @NonNull
    private int stars;
    private boolean is_deleted;

    //endregion

    //region Contructs

    @Ignore
    public UserReviewUser() {
    }

    /**
     * Create a new review
     * @param user
     * @param user_review
     * @param date
     * @param review
     * @param stars
     */
    public UserReviewUser(String user, String user_review, String date, String review, int stars, boolean is_deleted) {
        this.user = user;
        this.user_review = user_review;
        this.date = date;
        this.review = review;
        this.stars = stars;
        this.is_deleted = is_deleted;
    }

    @Ignore
    protected UserReviewUser(Parcel in) {
        user = in.readString();
        user_review = in.readString();
        date = in.readString();
        review = in.readString();
        stars = in.readInt();
        is_deleted = in.readByte() != 0;
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

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    //endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserReviewUser that = (UserReviewUser) o;

        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        return user_review != null ? user_review.equals(that.user_review) : that.user_review == null;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (user_review != null ? user_review.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserReviewUser{" +
                "idUser='" + user + '\'' +
                ", idUserReview='" + user_review + '\'' +
                ", date='" + date + '\'' +
                ", review='" + review + '\'' +
                ", stars=" + stars +
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
        dest.writeByte((byte)(is_deleted? 1:0));
    }
}
