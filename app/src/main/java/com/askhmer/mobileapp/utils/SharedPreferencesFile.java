package com.askhmer.mobileapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Longdy on 6/15/2016.
 */
public class SharedPreferencesFile {
    public static final String PREFER_KEY = "lockScreen";


    private Context mContext;
    private static SharedPreferencesFile mInstance = null;
    private SharedPreferences mSettings = null;
    private SharedPreferences.Editor mEditor = null;


    public SharedPreferencesFile(Context context, String sharedPrefName) {
        mContext = context;
        mSettings = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
    }

    public static SharedPreferencesFile newInstance(Context context, String sharedPrefName ) {
        if (mInstance == null) {
            mInstance = new SharedPreferencesFile(context, sharedPrefName);
        }
        return mInstance;
    }

    /**
     * @param perferKey
     * @param perferValue
     */
    public void putBooleanSharedPreference(String perferKey, boolean perferValue){
        mEditor.putBoolean(perferKey, perferValue);
        mEditor.commit();
    }

    /**
     * @param perferKey
     * @param perferValue
     */
    public void putStringSharedPreference(String perferKey,String perferValue){
        mEditor.putString(perferKey, perferValue);
        mEditor.commit();
    }

    /**
     * @param perferFileName
     * @param perferKey
     * @param perferValue
     */
    public void putIntSharedPreference(String perferFileName,String perferKey,int perferValue){
        mEditor.putInt(perferKey, perferValue);
        mEditor.commit();
    }

    /**
     * @param perferKey
     * @return
     */
    public boolean getBooleanSharedPreference(String perferKey){
        return mSettings.getBoolean(perferKey, false);
    }

    /**
     * @param perferKey
     * @return
     */
    public String getStringSharedPreference(String perferKey){
        return mSettings.getString(perferKey, null);
    }

    /**
     * @param perferFileName
     * @param perferKey
     * @return
     */
    public int getIntSharedPreference(String perferFileName, String perferKey){
        return mSettings.getInt(perferKey, 0);
    }
}
