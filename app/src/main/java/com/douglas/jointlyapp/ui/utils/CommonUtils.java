package com.douglas.jointlyapp.ui.utils;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.douglas.jointlyapp.R;

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

    public static Uri getImagenInitiativeDefault(Context context)
    {
        Uri uri = Uri.parse("android.resource://com.douglas.jointlyapp/drawable/playasucia.png");

        return uri;
//        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
//                "://" + context.getResources().getResourcePackageName(R.drawable.playasucia)
//                + '/' + context.getResources().getResourceTypeName(R.drawable.playasucia)
//                + '/' + context.getResources().getResourceEntryName(R.drawable.playasucia));
    }

    public static Uri getImagenUserDefault(Context context)
    {
        Uri uri = Uri.parse("android.resource://com.douglas.jointlyapp/mipmap/ic_app");

        return uri;
//        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
//                "://" + context.getResources().getResourcePackageName(R.drawable.playasucia)
//                + '/' + context.getResources().getResourceTypeName(R.drawable.playasucia)
//                + '/' + context.getResources().getResourceEntryName(R.drawable.playasucia));
    }
}
