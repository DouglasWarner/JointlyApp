package com.douglas.jointlyapp.services;

public class Apis {

    private static final String URL = "http://192.168.1.129:8080/";
    private static Apis api;

    private static InitiativeService initiativeService;
    private static UserService userService;
    private static JointlyService jointlyService;

    static {
        api = new Apis();
        initiativeService = Client.getClient(URL).create(InitiativeService.class);
        userService = Client.getClient(URL).create(UserService.class);
        jointlyService = Client.getClient(URL).create(JointlyService.class);
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
        return URL;
    }
}
