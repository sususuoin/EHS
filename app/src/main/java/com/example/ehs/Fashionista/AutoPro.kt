package com.example.ehs.Fashionista

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONException


object AutoPro {
    private val MY_PRO : String = "pro"

    fun setStyle(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_PROSTYLE", input)
        editor.commit()
    }

    fun getStyle(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        return prefs.getString("MY_PROSTYLE", "").toString()
    }


    fun setProProfileImg(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_ProProfileImg", a.toString())
        } else {
            editor.putString("MY_ProProfileImg", null)
        }
        editor.apply()

    }

    fun getProProfileImg(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_ProProfileImg", "")
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


    fun setProProfileId(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_ProProfileId", a.toString())
        } else {
            editor.putString("MY_ProProfileId", null)
        }
        editor.apply()

    }

    fun getProProfileId(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_ProProfileId", "")
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


}