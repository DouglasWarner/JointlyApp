package com.douglas.jointlyapp.ui.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.util.Log;

import com.bumptech.glide.request.RequestOptions;
import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.UserReviewUser;
import com.douglas.jointlyapp.ui.JointlyApplication;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Entity for the common use function on app
 */
public class CommonUtils {

    /**
     * check if the email is valid
     * @param email
     * @return boolean
     */
    public static boolean isEmailValid(String email) {
        Pattern pattern =  Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * check if the password is valid
     * @param password
     * @return boolean
     */
    public static boolean isPasswordValid(String password) {
        Pattern patron = Pattern.compile("^((?=\\w*\\d)(?=\\w*[a-z])(?=\\w*[A-Z]).{8,12})$");
        Matcher match = patron.matcher(password);

        return match.matches();
    }

    /**
     * check if the phone is valid
     * @param phone
     * @return boolean
     */
    public static boolean isPhoneValid(String phone) {
        Pattern pattern = Pattern.compile("^\\d{9}$");
        Matcher matcher = pattern.matcher(phone);

        return matcher.matches();
    }

    /**
     * check if the initiativeName is valid
     * @param name
     * @return boolean
     */
    public static boolean isInitiativeNameValid(String name) {
        Pattern patron = Pattern.compile("^\\w\\D{5,50}$");
        Matcher matcher = patron.matcher(name);

        return matcher.matches();
    }

    /**
     * check if the targetDate is valid
     * @param date
     * @return boolean
     */
    public static boolean isTargetDateValid(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(JointlyApplication.FORMAT_DD_MM_YYYY, Locale.getDefault());

        try {
            Date targetDate = simpleDateFormat.parse(date);
            Date now = Calendar.getInstance(TimeZone.getTimeZone("UTF")).getTime();
            return !targetDate.before(now);
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * show a modal dialog
     * @param context
     * @return ProgressDialog
     */
    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        // Hay que mostrar primero la vista para luego personalizarla
        progressDialog.show();
        if (progressDialog.getWindow() != null){
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        progressDialog.setContentView(R.layout.progress_dialog);
//        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        return progressDialog;
    }

    /**
     * get default initiative imagen for app
     * @param context
     * @return Bitmap
     */
    public static Bitmap getImagenInitiativeDefault(Context context) {
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.playasucia);
    }

    /**
     * get the default user image for app
     * @param context
     * @return Bitmap
     */
    public static Bitmap getImagenUserDefault(Context context) {
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_logomiapp);
    }

    /**
     * get the current date with format
     * @return String
     */
    public static String getDateNow() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(JointlyApplication.FORMAT_YYYY_MM_DD_HH_MM, Locale.getDefault());
        Date now = Calendar.getInstance(Locale.getDefault()).getTime();
        return simpleDateFormat.format(now);
    }

    /**
     * Format date to API
     * @param targetDate
     * @param targetTime
     * @return String
     */
    public static String formatDateToAPI(String targetDate, String targetTime) {
        Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(JointlyApplication.FORMAT_DD_MM_YYYY_HH_MM, Locale.getDefault());
        try {
            date = simpleDateFormat.parse(String.format("%s %s", targetDate, targetTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat dateFormat = new SimpleDateFormat(JointlyApplication.FORMAT_YYYY_MM_DD_HH_MM, Locale.getDefault());
        return dateFormat.format(date);
    }

    /**
     * Format date From API
     * @param datetime
     * @return String
     */
    public static String formatDateFromAPI(String datetime) {
        Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(JointlyApplication.FORMAT_YYYY_MM_DD_HH_MM, Locale.getDefault());
        try {
            date = simpleDateFormat.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat dateFormat = new SimpleDateFormat(JointlyApplication.FORMAT_DD_MM_YYYY_HH_MM, Locale.getDefault());
        return dateFormat.format(date);
    }

    /**
     * get default glide options for display image
     * @param type user or initiative
     * @return RequestOptions
     */
    public static RequestOptions getGlideOptions(String type) {
        return new RequestOptions()
                .centerCrop()
                .encodeQuality(100)
                .placeholder(R.mipmap.ic_app_foreground)
                .error((type.equals(Initiative.TAG)) ? R.drawable.playasucia : R.mipmap.ic_app_round);
    }

    /**
     * Average of all stars review
     * @param reviewUser
     * @return float
     */
    public static float getAverage(List<UserReviewUser> reviewUser) {
        float divisor = 0;
        float dividendo = 0;
        long[] value = new long[6];
        reviewUser.forEach(x-> value[x.getStars()]++);
        float totalStars = 0;
        for (int i = 1; i < value.length; i++) {
            totalStars += value[i];
        }
        for (int i = 0; i < value.length; i++) {
            divisor += ((value[i]/totalStars)*100) * i;
            dividendo +=  ((value[i]/totalStars)*100);
        }
        float result =divisor/dividendo;
        return Double.isNaN(result) ? 0 : result;
    }

    /**
     * Obtiene y parsea la imagen seleccionada
     * @param fileUri
     * @return MultipartBody.Part
     */
    public static MultipartBody.Part uploadFile(Uri fileUri) {
        byte[] content = new byte[]{};

//        if(fileUri.toString().isEmpty()) {
            return MultipartBody.Part.createFormData("picture", "file" , (RequestBody.create(content, MediaType.get("multipart/form-data"))));
//        }

////        try {
////            content = Files.readAllBytes(Paths.get(URI.create(fileUri.getPath())));
////        } catch (IOException e) {
////            Log.e("TAG", String.valueOf(Paths.get(fileUri.getPath())));
////        }
////
////        String name = new File(URI.create(String.valueOf(fileUri.getPath()))).getName();
////
////        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
////        // use the FileUtils to get the actual file by uri
////
////        RequestBody responseBody = RequestBody.create(content, MediaType.get("multipart/form-data"));
////        MultipartBody.Part body =
////                MultipartBody.Part.createFormData("picture", name , responseBody);
//
//        return body;
    }

    /**
     * Convert long to DateFormat
     * @param creationTimestamp
     * @return String
     */
    public static String getDateFromLong(long creationTimestamp) {
        return new SimpleDateFormat(JointlyApplication.FORMAT_DD_MM_YYYY_HH_MM, Locale.getDefault()).format(new Date(creationTimestamp));
    }
}
