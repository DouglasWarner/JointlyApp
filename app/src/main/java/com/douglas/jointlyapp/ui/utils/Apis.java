package com.douglas.jointlyapp.ui.utils;

import com.douglas.jointlyapp.services.InitiativeService;

public class Apis {

    public static final String URL = "http://192.168.1.130:8080/";

    public static InitiativeService getInitiativeService()
    {
        return Client.getClient(URL).create(InitiativeService.class);
    }
}
