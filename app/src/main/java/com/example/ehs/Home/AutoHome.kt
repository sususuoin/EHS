package com.example.ehs.Home

import android.content.Context
import android.content.SharedPreferences

object AutoHome {
    private val MY_Location : String = "location"

    fun setLocation(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_Location, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_Location", input)
        editor.commit()
    }

    fun getLocation(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_Location, Context.MODE_PRIVATE)
        return prefs.getString("MY_Location", "").toString()
    }
}