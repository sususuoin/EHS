package com.example.ehs.Closet

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import org.json.JSONArray
import org.json.JSONException


object AutoCody {
    private val MY_CODY : String = "cody"

    fun setCodyName(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_CODY, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_codyName", a.toString())
        } else {
            editor.putString("MY_codyName", null)
        }
        editor.apply()

    }

    fun getCodyName(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_CODY, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_codyName", "")
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


    fun setCodyStyle(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_CODY, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_CodyStyle", a.toString())
        } else {
            editor.putString("MY_CodyStyle", null)
        }
        editor.apply()

    }

    fun getCodyStyle(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_CODY, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_CodyStyle", "")
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

    fun clearCody(context: Context) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_CODY, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.commit()
    }

}