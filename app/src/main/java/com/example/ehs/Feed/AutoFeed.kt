package com.example.ehs.Feed

import android.content.Context
import android.content.SharedPreferences
import com.example.ehs.Fashionista.AutoPro
import org.json.JSONArray
import org.json.JSONException


object AutoFeed {
    private val MY_Feed : String = "feed"

    fun setFeedNum(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_Feed, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_feedNum", a.toString())
        } else {
            editor.putString("MY_feedNum", null)
        }
        editor.apply()

    }

    fun getFeedNum(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_Feed, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_feedNum", "")
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

    fun setFeedId(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_Feed, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_feedId", a.toString())
        } else {
            editor.putString("MY_feedId", null)
        }
        editor.apply()

    }

    fun getFeedId(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_Feed, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_feedId", "")
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

    fun setFeedStyle(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_Feed, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_feedStyle", a.toString())
        } else {
            editor.putString("MY_feedStyle", null)
        }
        editor.apply()

    }

    fun getFeedStyle(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_Feed, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_feedStyle", "")
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

    fun setFeedName(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_Feed, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_feedName", a.toString())
        } else {
            editor.putString("MY_feedName", null)
        }
        editor.apply()

    }

    fun getFeedName(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_Feed, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_feedName", "")
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


    fun setFeedNumlike(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_Feed, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_Feednumlike", a.toString())
        } else {
            editor.putString("MY_Feednumlike", null)
        }
        editor.apply()

    }

    fun getFeedNumlike(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_Feed, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_Feednumlike", "")
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


    fun setFeedliketrue(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_Feed, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_Feedliketrue", a.toString())
        } else {
            editor.putString("MY_Feedliketrue", null)
        }
        editor.apply()

    }

    fun getFeedliketrue(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_Feed, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_Feedliketrue", "")
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

    fun clearFeed(context: Context) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_Feed, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.commit()
    }

}