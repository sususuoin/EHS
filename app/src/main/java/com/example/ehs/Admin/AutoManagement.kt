package com.example.ehs.Admin

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONException


object AutoManagement {
    private val management : String = "management"

    fun setMuserId(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(management, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("management_userId", a.toString())
        } else {
            editor.putString("management_userId", null)
        }
        editor.apply()

    }

    fun getMuserId(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(management, Context.MODE_PRIVATE)
        val arr = prefs.getString("management_userId", "")
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

    fun setMuserPw(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(management, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("management_userPw", a.toString())
        } else {
            editor.putString("management_userPw", null)
        }
        editor.apply()

    }

    fun getMuserPw(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(management, Context.MODE_PRIVATE)
        val arr = prefs.getString("management_userPw", "")
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

    fun setMuserName(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(management, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("management_userName", a.toString())
        } else {
            editor.putString("management_userName", null)
        }
        editor.apply()

    }

    fun getMuserName(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(management, Context.MODE_PRIVATE)
        val arr = prefs.getString("management_userName", "")
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

    fun setMuserEmail(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(management, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("management_userEmail", a.toString())
        } else {
            editor.putString("management_userEmail", null)
        }
        editor.apply()

    }

    fun getMuserEmail(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(management, Context.MODE_PRIVATE)
        val arr = prefs.getString("management_userEmail", "")
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

    fun setMuserBirth(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(management, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("management_userBirth", a.toString())
        } else {
            editor.putString("management_userBirth", null)
        }
        editor.apply()

    }

    fun getMuserBirth(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(management, Context.MODE_PRIVATE)
        val arr = prefs.getString("management_userBirth", "")
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

    fun setMuserGender(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(management, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("management_userGender", a.toString())
        } else {
            editor.putString("management_userGender", null)
        }
        editor.apply()

    }

    fun getMuserGender(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(management, Context.MODE_PRIVATE)
        val arr = prefs.getString("management_userGender", "")
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

    fun setMuserLevel2(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(management, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("management_userLevel2", a.toString())
        } else {
            editor.putString("management_userLevel2", null)
        }
        editor.apply()

    }

    fun getMuserLevel2(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(management, Context.MODE_PRIVATE)
        val arr = prefs.getString("management_userLevel2", "")
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

    fun setMuserLevel(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(management, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("management_userLevel", a.toString())
        } else {
            editor.putString("management_userLevel", null)
        }
        editor.apply()

    }

    fun getMuserLevel(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(management, Context.MODE_PRIVATE)
        val arr = prefs.getString("management_userLevel", "")
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

    fun setMuserTag(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(management, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("management_userTag", a.toString())
        } else {
            editor.putString("management_userTag", null)
        }
        editor.apply()

    }

    fun getMuserTag(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(management, Context.MODE_PRIVATE)
        val arr = prefs.getString("management_userTag", "")
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

    fun setMuserProfile(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(management, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("management_userProfile", a.toString())
        } else {
            editor.putString("management_userProfile", null)
        }
        editor.apply()

    }

    fun getMuserProfile(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(management, Context.MODE_PRIVATE)
        val arr = prefs.getString("management_userProfile", "")
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


    fun clearManagement(context: Context) {
        val prefs : SharedPreferences = context.getSharedPreferences(management, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.commit()
    }


}