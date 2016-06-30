package com.askhmer.mobileapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Longdy on 6/15/2016.
 */
public class SharedPreferencesFile {
    public static final String PREFER_KEY = "lockScreen";
    public static final String FILE_INFORMATION_TEMP = "file_information_temp";
    public static final String IS_KEY_INTRO_FINISH = "IS_KEY_INTRO_FINISH";
    public static final String KEY_INFORMATION_TEMP_PHONE = "KEY_INFORMATION_TEMP_PHONE";
    public static final String KEY_INFORMATION_TEMP_CASHID = "KEY_INFORMATION_TEMP_CASHID";
    public static final String KEY_INFORMATION_TEMP_NAME = "KEY_INFORMATION_TEMP_NAME";
    public static final String KEY_INFORMATION_TEMP_PASSWORD = "KEY_INFORMATION_TEMP_PASSWORD";
    public static final String KEY_INFORMATION_TEMP_GENDER = "KEY_INFORMATION_TEMP_GENDER";
    public static final String KEY_INFORMATION_TEMP_AGE = "KEY_INFORMATION_TEMP_AGE";
    public static final String KEY_INFORMATION_TEMP_NATIONAL = "KEY_INFORMATION_TEMP_NATIONAL";
    public static final String KEY_INFORMATION_TEMP_LOCATION = "KEY_INFORMATION_TEMP_LOCATION";
    public static final String IS_OPEN_INFORMATION_SCREEN_KEY = "IS_OPEN_INFORMATION_SCREEN_KEY";
    public static final String KEY_INFORMATION_TEMP_TOKEN = "KEY_INFORMATION_TEMP_TOKEN";


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

    /**
     *
     */
    public void deleteSharedPreference(){
        mEditor.clear();
        mEditor.commit();
    }
}
