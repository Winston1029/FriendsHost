package com.moupress.app.friendshost.util;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;

import com.moupress.app.friendshost.Const;

public final class SharePreferencesHelper
{
    public static SharedPreferences SP;

    private Context mContext;
    
    public SharePreferencesHelper(Context context)
    {
        this.mContext = context;
        SP = mContext.getSharedPreferences(Const.sPName, 0);
    }
    
    public void Insert(String key, String value)
    {
       SP.edit().putString(key, value).commit();
    }
    
    public void Insert(String key, int value)
    {
       SP.edit().putInt(key, value).commit();
    }
    public void Insert(String key, long value)
    {
       SP.edit().putLong(key, value).commit();
    }
    public void Insert(String key, Boolean value)
    {
       SP.edit().putBoolean(key, value).commit();
    }
    
    

}
