package com.example.pentagon.sngistaudit;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceClass {
    public static final String PROPERTY_USERNAME = "SNAPPHELP_USERNAME";
    public static final String PROPERTY_PASSWORD = "SNAPPHELP_PASSWORD";
    public static final String PROPERTY_USERID = "SNAPPHELP_USERID";
    public static final String PROPERTY_SKIP = "SNAPPHELP_USERSKIP";
    public static final String PROPERTY_TYPE = "SNAPPHELP_USERTYPE";
    public static final String PROPERTY_Json = "SNAPPHELP_USERjson";
  SharedPreferences prefs = null;

    public SharedPreferenceClass(SharedPreferences prefs) {
        this.prefs = prefs;
    }
    public String getPROPERTY_Json() {



        String USERNAME = null;


        try {

            USERNAME = prefs.getString(PROPERTY_Json, null);

        } catch (NullPointerException ert) {
        }

        return USERNAME;

    }
    public void setPROPERTY_Json(String value) {

        //   final SharedPreferences prefs = getSharedPreferencesForServer(context);

        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(PROPERTY_Json, value);


        editor.commit();

    }
    public void setPropertyType(String value) {

        //   final SharedPreferences prefs = getSharedPreferencesForServer(context);

        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(PROPERTY_TYPE, value);


        editor.commit();

    }


    public String getPropertyType() {



        String USERNAME = null;


        try {

            USERNAME = prefs.getString(PROPERTY_TYPE, null);

        } catch (NullPointerException ert) {
        }

        return USERNAME;

    }
    public void setPropertySkip(String value) {

        //   final SharedPreferences prefs = getSharedPreferencesForServer(context);

        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(PROPERTY_SKIP, value);


        editor.commit();

    }


    public String getPropertySkip() {



        String USERNAME = null;


        try {

            USERNAME = prefs.getString(PROPERTY_SKIP, null);

        } catch (NullPointerException ert) {
        }

        return USERNAME;

    }


    public void setPropertyUsername(String value) {

        //   final SharedPreferences prefs = getSharedPreferencesForServer(context);

        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(PROPERTY_USERNAME, value);


        editor.commit();

    }


    public String getPropertyUsername() {



        String USERNAME = null;


        try {

            USERNAME = prefs.getString(PROPERTY_USERNAME, null);

        } catch (NullPointerException ert) {
        }

        return USERNAME;

    }


    public String getPropertyPassword() {


        String PASSWORD = null;

        try {
            PASSWORD = prefs.getString(PROPERTY_PASSWORD, null);
        } catch (NullPointerException ert) {

        }

        return PASSWORD;

    }

    public String getPropertyUserid() {


        String USERID = null;

        try {
            USERID = prefs.getString(PROPERTY_USERID, null);
        } catch (NullPointerException ert) {
            return null;
        }

        return USERID;

    }



    public void clearPreference(){

        prefs.edit().clear().commit();
    }









    public void setPropertyPassword( String value) {

        //   final SharedPreferences prefs = getSharedPreferencesForServer(context);

        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(PROPERTY_PASSWORD, value);


        editor.commit();

    }

    public void setPropertyUserid( String value) {


        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(PROPERTY_USERID, value);


        editor.commit();

    }











    private SharedPreferences getSharedPreferencesForServer(Context context) {

        // This sample app persists the serverIp in shared preferences, but

        // how you store the regID in your app is up to you.

        return PreferenceManager.getDefaultSharedPreferences(context);//context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

    }


}