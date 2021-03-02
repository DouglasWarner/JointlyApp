package com.douglas.jointlyapp.ui.utils;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.BitmapCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableResource;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.drawable.DrawableResource;
import com.douglas.jointlyapp.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    public static boolean isEmailValid(String email)
    {
        Pattern pattern =  Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean isPasswordValid(String password)
    {
        Pattern patron = Pattern.compile("^((?=\\w*\\d)(?=\\w*[a-z])(?=\\w*[A-Z]).{8,12})$");
        Matcher match = patron.matcher(password);

        return match.matches();
    }

    public static boolean isPhoneValid(String phone)
    {
        Pattern pattern = Pattern.compile("^\\d{9}$");
        Matcher matcher = pattern.matcher(phone);

        return matcher.matches();
    }

    public static boolean isInitiativeNameValid(String name) {
        Pattern patron = Pattern.compile("^\\w\\D{5,50}$");
        Matcher matcher = patron.matcher(name);

        return matcher.matches();
    }

    public static boolean isTargetDateValid(String date)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date targetDate = simpleDateFormat.parse(date);
            Date now = Calendar.getInstance(TimeZone.getTimeZone("UTF")).getTime();

            return !targetDate.before(now);

        } catch (ParseException e) {
            return false;
        }
    }

    public static ProgressDialog showLoadingDialog(Context context){
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

    public static Bitmap getImagenInitiativeDefault(Context context)
    {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.playasucia);

        return bitmap;
    }

    public static Bitmap getImagenUserDefault(Context context)
    {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_logomiapp);

        return bitmap;
    }

    public static String getDateNow()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        Date now = Calendar.getInstance().getTime();

        return simpleDateFormat.format(now);
    }
}
