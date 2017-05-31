package com.zackstrikesback.imgur;

public class MyAppConstants {
    public static final String MY_IMGUR_CLIENT_ID = "9df18c0b96fcb0d";
    public static final String MY_IMGUR_CLIENT_SECRET = "6de0494f269b235e3fa9896a84b4b684b4939b11";
    //The arbitrary redirect url (Authorization callback URL), ex. awesome://imgur or http://android,
    // declared when registering your app with Imgur API
    public static final String MY_IMGUR_REDIRECT_URL = "https://imgur.com";
    public static String getClientAuth() {
        return "Client-ID " + MY_IMGUR_CLIENT_ID;
    }
}
