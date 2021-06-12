package com.douglas.jointlyapp.services;

/**
 * Entity API config
 */
public class Apis {

    private static final String URL = "http://192.168.1.129:8080/";
    private static final String URL_AWS = "http://54.78.116.76:8080/";
    private static final boolean IS_AWS = false;

    private static Apis api;

    private static InitiativeService initiativeService;
    private static UserService userService;
    private static JointlyService jointlyService;

    static {
        api = new Apis();
        if(IS_AWS) {
            initiativeService = Client.getClient(URL_AWS).create(InitiativeService.class);
            userService = Client.getClient(URL_AWS).create(UserService.class);
            jointlyService = Client.getClient(URL_AWS).create(JointlyService.class);
        } else {
            initiativeService = Client.getClient(URL).create(InitiativeService.class);
            userService = Client.getClient(URL).create(UserService.class);
            jointlyService = Client.getClient(URL).create(JointlyService.class);
        }
    }

    private Apis() {
    }

    public static Apis getInstance() {
        return api;
    }

    public InitiativeService getInitiativeService() {
        return initiativeService;
    }

    public UserService getUserService() {
        return userService;
    }

    public JointlyService getJointlyService() { return jointlyService; }

    public static String getURL() {
        if(IS_AWS) {
            return URL_AWS;
        } else {
            return URL;
        }
    }

    public static String getURLIMAGE() {
        if(IS_AWS) {
            return URL_AWS+"image/";
        } else {
            return URL+"image/";
        }
    }
}
