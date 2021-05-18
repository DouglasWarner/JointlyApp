package com.douglas.jointlyapp.services;

import com.douglas.jointlyapp.data.model.Initiative;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class APIResponse {

    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<Initiative> data;

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public List<Initiative> getData() {
        return data;
    }
}
