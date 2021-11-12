package com.example.ehs.Fashionista

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.ehs.Closet.AutoCloset
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

    fun setProuserId(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_ProuserId", a.toString())
        } else {
            editor.putString("MY_ProuserId", null)
        }
        editor.apply()

    }

    fun getProuserId(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_ProuserId", "")
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

    fun setProuserLevel(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_ProuserLevel", a.toString())
        } else {
            editor.putString("MY_ProuserLevel", null)
        }
        editor.apply()

    }

    fun getProuserLevel(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_ProuserLevel", "")
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

    fun setProuserProImg(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_ProuserProImg", a.toString())
        } else {
            editor.putString("MY_ProuserProImg", null)
        }
        editor.apply()

    }

    fun getProuserProImg(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_ProuserProImg", "")
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

    fun setProplusImgPath(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_proplusImgPath", a.toString())
        } else {
            editor.putString("MY_proplusImgPath", null)
        }
        editor.apply()

    }

    fun getProplusImgPath(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_proplusImgPath", "")
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

    fun setProplusImgName(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_ProplusImgName", a.toString())
        } else {
            editor.putString("MY_ProplusImgName", null)
        }
        editor.apply()

    }

    fun getProplusImgName(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_ProplusImgName", "")
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

    fun setFavoriteuserId(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_FavoriteuserId", a.toString())
        } else {
            editor.putString("MY_FavoriteuserId", null)
        }
        editor.apply()

    }

    fun getFavoriteuserId(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_FavoriteuserId", "")
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

    fun setFavoriteuserId2(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_FavoriteuserId2", a.toString())
        } else {
            editor.putString("MY_FavoriteuserId2", null)
        }
        editor.apply()

    }

    fun getFavoriteuserId2(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_FavoriteuserId2", "")
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

    fun setFavoriteuserHashTag(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_FavoriteuserHashTag", a.toString())
        } else {
            editor.putString("MY_FavoriteuserHashTag", null)
        }
        editor.apply()

    }

    fun getFavoriteuserHashTag(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_FavoriteuserHashTag", "")
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


    fun setFavoriteuserImg(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_FavoriteuserImg", a.toString())
        } else {
            editor.putString("MY_FavoriteuserImg", null)
        }
        editor.apply()

    }

    fun getFavoriteuserImg(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_FavoriteuserImg", "")
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

    fun setplusImgPath(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_PlusImgPath", a.toString())
        } else {
            editor.putString("MY_PlusImgPath", null)
        }
        editor.apply()

    }

    fun getplusImgPath(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_PlusImgPath", "")
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


    fun setplusImgName(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_PlusImgName", a.toString())
        } else {
            editor.putString("MY_PlusImgName", null)
        }
        editor.apply()

    }

    fun getplusImgName(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_PlusImgName", "")
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

    fun setFuserId(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_FuserId", a.toString())
        } else {
            editor.putString("MY_FuserId", null)
        }
        editor.apply()

    }

    fun getFuserId(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_FuserId", "")
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
    fun setFcodyImgName(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_FcodyImgName", a.toString())
        } else {
            editor.putString("MY_FcodyImgName", null)
        }
        editor.apply()

    }

    fun getFcodyImgName(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_FcodyImgName", "")
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
    fun setFcodyStyle(context: Context, input: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until input.size) {
            a.put(input[i])
        }
        if (input.isNotEmpty()) {
            editor.putString("MY_FcodyStyle", a.toString())
        } else {
            editor.putString("MY_FcodyStyle", null)
        }
        editor.apply()

    }

    fun getFcodyStyle(context: Context): ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val arr = prefs.getString("MY_FcodyStyle", "")
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



    fun clearPro(context: Context) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.commit()
    }


}