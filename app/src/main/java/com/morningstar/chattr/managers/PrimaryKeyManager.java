/*
 * Created by Sujoy Datta. Copyright (c) 2019. All rights reserved.
 *
 * To the person who is reading this..
 * When you finally understand how this works, please do explain it to me too at sujoydatta26@gmail.com
 * P.S.: In case you are planning to use this without mentioning me, you will be met with mean judgemental looks and sarcastic comments.
 */

package com.morningstar.chattr.managers;

public class PrimaryKeyManager {

    private static final String TAG = "PrimaryKeyManager";

    public static String getPrimaryKeyForChatItem(String sender_username) {
        return DateTimeManager.getCurrentSystemDate() + sender_username;
    }

    public static String getObjectKeyForChattrBox(String sender_username, String receiver_username) {
        if (sender_username.compareTo(receiver_username) < 0)
            return sender_username + receiver_username;
        else
            return receiver_username + sender_username;

//        a negative int if this < that
//        0 if this == that
//        a positive int if this > that
    }
}
