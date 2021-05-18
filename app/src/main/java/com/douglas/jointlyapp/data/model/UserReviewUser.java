package com.douglas.jointlyapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import java.io.Serializable;

@Entity(tableName = "userReviewUser", primaryKeys = {"idUser", "idUserReview"},
    foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "email", childColumns = "idUser"),
                @ForeignKey(entity = User.class, parentColumns = "email", childColumns = "idUserReview")},
    indices = {@Index("idUser"), @Index("idUserReview")})
public class UserReviewUser implements Serializable, Parcelable {

    public static final String TAG = "UserReviewUser";

    @NonNull
    private String idUser;
    @NonNull
    private String idUserReview;
    @NonNull
    private String date;
    private String review;
    @NonNull
    private int stars;

    @Ignore
    public UserReviewUser() {
    }

    /**
     * Create a new review
     * @param idUser
     * @param idUserReview
     * @param date
     * @param review
     * @param stars
     */
    public UserReviewUser(String idUser, String idUserReview, String date, String review, int stars) {
        this.idUser = idUser;
        this.idUserReview = idUserReview;
        this.date = date;
        this.review = review;
        this.stars = stars;
    }

    @Ignore
    protected UserReviewUser(Parcel in) {
        idUser = in.readString();
        idUserReview = in.readString();
        date = in.readString();
        review = in.readString();
        stars = in.readInt();
    }

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

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdUserReview() {
        return idUserReview;
    }

    public void setIdUserReview(String idUserReview) {
        this.idUserReview = idUserReview;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserReviewUser that = (UserReviewUser) o;

        if (idUser != null ? !idUser.equals(that.idUser) : that.idUser != null) return false;
        return idUserReview != null ? idUserReview.equals(that.idUserReview) : that.idUserReview == null;
    }

    @Override
    public int hashCode() {
        int result = idUser != null ? idUser.hashCode() : 0;
        result = 31 * result + (idUserReview != null ? idUserReview.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserReviewUser{" +
                "idUser='" + idUser + '\'' +
                ", idUserReview='" + idUserReview + '\'' +
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
        dest.writeString(idUser);
        dest.writeString(idUserReview);
        dest.writeString(date);
        dest.writeString(review);
        dest.writeInt(stars);
    }
}
