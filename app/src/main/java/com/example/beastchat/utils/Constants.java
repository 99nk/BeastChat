package com.example.beastchat.utils;

import com.example.beastchat.entities.User;

import java.util.HashMap;

public class Constants
{
    public static final String IP_LOCAL_HOST="http://192.168.43.226:3000";

    //Keys for shared preferences

    public static final String USER_INFO_PREFERENCE="USER_INFO_PREFERENCE";
    public static final String USER_EMAIL="USER_EMAIL";
    public static final String USER_NAME="USER_NAME";
    public static final String USER_PICTURE="USER_PICTURE";
    public static final String FIREBASE_PATH_USERS= "users";
    public static final String FIREBASE_PATH_FRIEND_REQUEST_SENT="friendRequestsSent";
    public static final String FIREBASE_PATH_FRIEND_REQUEST_RECEIVED="friendRequestRecieved";
    public static final String FIREBASE_PATH_USER_FRIENDS="userFriends";
    public static String encodeEmail(String email)
    {
        return email.replace(".",",");
    }
    public static boolean isIncludedInMap(HashMap<String, User> userHashMap,User user)
    {
        return userHashMap!=null && userHashMap.size()!=0&& userHashMap.containsKey(user.getEmail());
    }

}
