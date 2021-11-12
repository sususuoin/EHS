package com.example.ehs.Home

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.ehs.Fashionista.AutoPro
import org.json.JSONArray
import org.json.JSONException

object AutoHome {
    private val MY_HOME : String = "Home"

    fun setColorcody(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_HOME, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_colorcody", input)
        editor.commit()
    }

    fun getColorcody(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_HOME, Context.MODE_PRIVATE)
        return prefs.getString("MY_colorcody", "").toString()
    }


    fun setColoruserId(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_HOME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_coloruserId", a.toString())
        } else {
            editor.putString("MY_coloruserId", null)
        }
        editor.apply()

    }

    fun getColoruserId(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_HOME, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_coloruserId", "")
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

    fun setColorplusImgPath(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_HOME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_ColorplusImgPath", a.toString())
        } else {
            editor.putString("MY_ColorplusImgPath", null)
        }
        editor.apply()

    }

    fun getColorplusImgPath(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_HOME, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_ColorplusImgPath", "")
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

    fun setColorplusImgName(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_HOME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_ColorplusImgName", a.toString())
        } else {
            editor.putString("MY_ColorplusImgName", null)
        }
        editor.apply()

    }

    fun getColorplusImgName(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_HOME, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_ColorplusImgName", "")
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

    fun setColorplusImgStyle(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_HOME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_ColorplusImgStyle", a.toString())
        } else {
            editor.putString("MY_ColorplusImgStyle", null)
        }
        editor.apply()

    }

    fun getColorplusImgStyle(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_HOME, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_ColorplusImgStyle", "")
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

    fun setRandom_clothesCategory(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_HOME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_Random_clothesCategory", a.toString())
        } else {
            editor.putString("MY_Random_clothesCategory", null)
        }
        editor.apply()

    }

    fun getRandom_clothesCategory(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_HOME, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_Random_clothesCategory", "")
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

    fun setRandom_clothesName(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_HOME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_Random_clothesName", a.toString())
        } else {
            editor.putString("MY_Random_clothesName", null)
        }
        editor.apply()

    }

    fun getRandom_clothesName(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_HOME, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_Random_clothesName", "")
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

    fun setRandom_clothesCategory_Detail(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_HOME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_Random_clothesCategory_Detail", a.toString())
        } else {
            editor.putString("MY_Random_clothesCategory_Detail", null)
        }
        editor.apply()

    }

    fun getRandom_clothesCategory_Detail(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_HOME, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_Random_clothesCategory_Detail", "")
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