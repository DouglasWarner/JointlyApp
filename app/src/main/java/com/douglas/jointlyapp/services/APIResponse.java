package com.douglas.jointlyapp.services;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class APIResponse<T> {

    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<T> data;

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public List<T> getData() {
        return data;
    }
}
