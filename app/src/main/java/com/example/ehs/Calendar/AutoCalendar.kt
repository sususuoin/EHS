package com.example.ehs.Calendar

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONException


object AutoCalendar {
    private val MY_CALENDAR : String = "calendar"

    fun setCalendarchoiceImg(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_CALENDAR, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_CalendarchoiceImg", a.toString())
        } else {
            editor.putString("MY_CalendarchoiceImg", null)
        }
        editor.apply()

    }

    fun getCalendarchoiceImg(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_CALENDAR, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_CalendarchoiceImg", "")
        val urls = ArrayList<String>()
        if (arr != null) {
            try {
                val a = JSONArray(arr)
                for (i in 0 until a.length()) {
                    val url = a.optString(i)
                    urls.add(url)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return urls

    }

    fun setSelectday(context: Context, input: String?) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_CALENDAR, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_setselectday", input)
        editor.commit()
    }

    fun getSelectday(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_CALENDAR, Context.MODE_PRIVATE)
        return prefs.getString("MY_setselectday", "").toString()
    }

    fun clearCalendar(context: Context) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_CALENDAR, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.commit()
    }


}