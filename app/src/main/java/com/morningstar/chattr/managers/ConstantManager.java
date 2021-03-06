/*
 * Created by Sujoy Datta. Copyright (c) 2018. All rights reserved.
 *
 * To the person who is reading this..
 * When you finally understand how this works, please do explain it to me too at sujoydatta26@gmail.com
 * P.S.: In case you are planning to use this without mentioning me, you will be met with mean judgemental looks and sarcastic comments.
 */

package com.morningstar.chattr.managers;

public class ConstantManager {

    public static final String IP_LOCALHOST = "https://chattr-server01.herokuapp.com";
    //    public static final String IP_LOCALHOST = "https://192.168.0.106:3000";
    public static final String SHARED_PREF_FILE_NAME = "ChattrPref";
    public static final String PREF_TITLE_USER_MOBILE = "USER_MOBILE";
    public static final String PREF_TITLE_USER_EMAIL = "USER_EMAIL";
    public static final String PREF_OPENED_CHAT_ID = "OPENED_CHAT";
    //    public static final String PREF_TITLE_USER_NAME = "USER_NAME";
//    public static final String PREF_TITLE_USER_SURNAME = "USER_SURNAME";
    public static final String PREF_TITLE_USER_DP_URL = "USER_DP_URL";
    public static final String PREF_TITLE_USER_USERNAME = "USER_USERNAME";

    public static final String FIREBASE_USERS_TABLE = "Users";
    public static final String FIREBASE_PHONE_NUMBERS_TABLE = "Phone Numbers";
    public static final String FIREBASE_NAME_COLUMN = "Name";
    public static final String FIREBASE_SURNAME_COLUMN = "Surname";
    public static final String FIREBASE_USERNAME_COLUMN = "Username";
    public static final String FIREBASE_MOBILE_NUMBER_COLUMN = "Mobile Number";
    public static final String FIREBASE_DP_LINK_COLUMN = "dp link";
    public static final String FIREBASE_EMAIL_COLUMN = "Email";
    public static final String FIREBASE_IS_ONLINE_COLUMN = "Is_Online";
    public static final String FIREBASE_USER_STATUS_COLUMN = "Status";
    public static final String FIREBASE_IS_LOGGED_IN_COLUMN = "Is_LoggedIn";
    public static final String FIREBASE_USER_INSTANCE_ID = "InstanceId";

    public static final String STRING_USERNAME = "username";
    public static final String STRING_NAME = "name";
    public static final String STRING_SURNAME = "surname";
    public static final String STRING_MOBILE_NUMBER = "mobNumber";
    public static final String STRING_EMAIL = "email";
    public static final String CONTACT_NUMBER = "contactNumber";
    public static final String CONTACT_NAME = "contactName";
    public static final String CONTACT_ID = "contactID";
    public static final String IS_CONTACT_ADDED = "isAdded";
    public static final String CONTACT_USERNAME = "contactUsername";
    public static final String STRING_PASSWORD = "password";

    public static final String REGISTRATION_SUCCESS_MESSAGE = "REGISTRATION_SUCCESS";
    public static final String REGISTRATION_FAILED_MESSAGE = "REGISTRATION_FAILED";

    //SOCKET EVENT NAMES
    public static final String REGISTRATION_COMPLETED_EVENT = "REGISTRATION_COMPLETE";
    public static final String FIREBASE_AUTH_TOKEN_GENERATED = "FIREBASE_AUTH_TOKEN_GENERATED";
    public static final String FRIEND_CREATED = "FRIEND_CREATED";
    public static final String SEND_CHAT_MESSAGE_EVENT = "SEND_CHAT_MESSAGE_EVENT";

    public static final String SUCCESSFUL = "success";
    public static final String UNSUCCESSFUL = "failed";

    public static final String OFF = "offline";
    public static final String ON = "online";
    public static final String BUNDLE_EXTRAS = "bundle_extra";
    public static final String HEADER = "header";
    public static final String FIREBASE_CHATS_DB = "Chats";
    public static final String FIREBASE_NEW_CHAT_IDS_DB = "NewMessageIds";
    public static final String FIREBASE_NEW_CHAT_ID = "NewMessageId";
    public static final String PREF_TITLE_USER_TOKEN = "InstanceId";
    public static final String NEW_MESSAGE_RECEIVED = "NEW_MESSAGE_RECEIVED";
    public static final String FRIEND_USERNAME = "friend_username";
    public static final String FRIEND_NAME = "friend_name";
    public static final String INITIATOR_ACTIVITY = "initiator_activity";
}
