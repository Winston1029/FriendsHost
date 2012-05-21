package com.moupress.app.friendshost.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {
    private static String sPrefName = "FriendsHostSharedPref";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(sPrefName, 0);
    }

    public static String getMyStringPref(Context context, String key) {
        return getPrefs(context).getString(key, "");
    }

    public static int getMyIntPref(Context context, String key) {
        return getPrefs(context).getInt(key, 0);
    }
    
    public static boolean getMyBoolPref(Context context, String key)
    {
         return getPrefs(context).getBoolean(key, false);	
    }

    public static void setMyStringPref(Context context, String key, String value) {
        // perform validation etc..
        getPrefs(context).edit().putString(key, value).commit();
    }

    public static void setMyIntPref(Context context, String key, int value) {
        // perform validation etc..
        getPrefs(context).edit().putInt(key, value).commit();
    }
    
    public static void setMyBoolPref(Context context, String key, boolean value) {
        // perform validation etc..
        getPrefs(context).edit().putBoolean(key, value).commit();
    }
    
}
