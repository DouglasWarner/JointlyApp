package com.douglas.jointlyapp.services;

import com.google.gson.annotations.SerializedName;

public class APIResponse<T> {

    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private T data;

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
