package com.example.ehs.Closet

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONException


object AutoCloset {
    private val MY_CLOSET : String = "closet"

    fun setClothesName(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_CLOSET, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_clothesName", a.toString())
        } else {
            editor.putString("MY_clothesName", null)
        }
        editor.apply()

    }

    fun getClothesName(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_CLOSET, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_clothesName", "")
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