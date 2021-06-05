package com.douglas.jointlyapp.ui.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.JointlyDatabase;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserFollowUser;
import com.douglas.jointlyapp.data.model.UserJoinInitiative;
import com.douglas.jointlyapp.data.model.UserReviewUser;
import com.douglas.jointlyapp.data.repository.InitiativeRepository;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.services.APIResponse;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.utils.service.Apis;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(JointlyApplication.DATETIMEFORMAT);

        Date now = Calendar.getInstance(Locale.getDefault()).getTime();

        return simpleDateFormat.format(now);
    }

    //TODO quizar eliminar
    public static void aSyncDataUser() {
        Call<APIResponse<User>> userCall = Apis.getInstance().getUserService().getListUser();
        userCall.enqueue(new Callback<APIResponse<User>>() {
            @Override
            public void onResponse(Call<APIResponse<User>> call, Response<APIResponse<User>> response) {
                Log.e("TAG", response.message());
                if(response.isSuccessful() && response.body() != null) {
                    if(!response.body().isError()) {
                        response.body().getData().stream().forEach(x -> x.setImagen(
                                x.getImagen() == null ? CommonUtils.getImagenUserDefault(JointlyApplication.getContext()) : x.getImagen()
                        ));
                        UserRepository.getInstance().upsertUser(response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse<User>> call, Throwable t) {
                Log.e("ERR", t.getMessage());
                call.cancel();
            }
        });
    }

    public static void aSyncDataInitiative() {
        Call<APIResponse<Initiative>> initiativeCall = Apis.getInstance().getInitiativeService().getListInitiative();
        initiativeCall.enqueue(new Callback<APIResponse<Initiative>>() {
            @Override
            public void onResponse(Call<APIResponse<Initiative>> call, Response<APIResponse<Initiative>> response) {
                Log.e("TAG", response.message());
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isError()) {
                        response.body().getData().stream().forEach(x -> x.setImagen(
                                x.getImagen() == null ? CommonUtils.getImagenInitiativeDefault(JointlyApplication.getContext()) : x.getImagen()
                        ));
                        InitiativeRepository.getInstance().upsertInitiative(response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse<Initiative>> call, Throwable t) {
                Log.e("ERR", t.getMessage());
                call.cancel();
            }
        });
    }

    public static void aSyncDataUserJoinInitiative() {
        Call<APIResponse<UserJoinInitiative>> userJoinCall = Apis.getInstance().getInitiativeService().getListUserJoined();
        userJoinCall.enqueue(new Callback<APIResponse<UserJoinInitiative>>() {
            @Override
            public void onResponse(Call<APIResponse<UserJoinInitiative>> call, Response<APIResponse<UserJoinInitiative>> response) {
                Log.e("TAG", response.message());
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isError())
                        InitiativeRepository.getInstance().upsertUserJoinInitiative(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<APIResponse<UserJoinInitiative>> call, Throwable t) {
                Log.e("ERR", t.getMessage());
                call.cancel();
            }
        });
    }

    public static void aSyncDataUserFollowUser() {
        Call<APIResponse<UserFollowUser>> userFollowCall = Apis.getInstance().getUserService().getListUserFollow();
        userFollowCall.enqueue(new Callback<APIResponse<UserFollowUser>>() {
            @Override
            public void onResponse(Call<APIResponse<UserFollowUser>> call, Response<APIResponse<UserFollowUser>> response) {
                Log.e("TAG", response.message());
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isError()) {
                        UserRepository.getInstance().upsertUserFollowUser(response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse<UserFollowUser>> call, Throwable t) {
                Log.e("ERR", t.getMessage());
                call.cancel();
            }
        });
    }

    public static void aSyncDataUserReviewUser() {
        Call<APIResponse<UserReviewUser>> userReviewCall = Apis.getInstance().getUserService().getListUserReview();
        userReviewCall.enqueue(new Callback<APIResponse<UserReviewUser>>() {
            @Override
            public void onResponse(Call<APIResponse<UserReviewUser>> call, Response<APIResponse<UserReviewUser>> response) {
                Log.e("TAG", response.message());
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isError()) {
                        UserRepository.getInstance().upsertUserReviewUser(response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse<UserReviewUser>> call, Throwable t) {
                Log.e("ERR", t.getMessage());
                call.cancel();
            }
        });
    }
}
