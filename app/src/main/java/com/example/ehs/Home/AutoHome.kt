package com.example.ehs.Home

import android.content.Context
import android.content.SharedPreferences

object AutoHome {
    private val MY_HOME : String = "location"

    fun setLocation(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_HOME, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_Location", input)
        editor.commit()
    }

    fun getLocation(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_HOME, Context.MODE_PRIVATE)
        return prefs.getString("MY_Location", "").toString()
    }


    fun setLongitude(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_HOME, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_Longitude", input)
        editor.commit()
    }

    fun getLongitude(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_HOME, Context.MODE_PRIVATE)
        return prefs.getString("MY_Longitude", "").toString()
    }


    fun setLatitude(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_HOME, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_Latitude", input)
        editor.commit()
    }

    fun getLatitude(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_HOME, Context.MODE_PRIVATE)
        return prefs.getString("MY_Latitude", "").toString()
    }


    fun clearHome(context: Context) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_HOME, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.commit()
    }
}