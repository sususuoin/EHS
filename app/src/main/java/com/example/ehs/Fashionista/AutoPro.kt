package com.example.ehs.Fashionista

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.Login.AutoLogin
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


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



    fun FashionistaUser() {

        var fuserId: String
        var fuserLevel: String
        var fuserProfileImg : String
        var fuserProfile : Bitmap?

        val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
            override fun onResponse(response: String?) {
                try {

                    var jsonObject = JSONObject(response)
                    var response = jsonObject.toString()

                    val arr: JSONArray = jsonObject.getJSONArray("response")

                    Log.d("이이이이잉~~나는 언제잘수있을까 ?", response)
                    Log.d("이이이이잉~~나는 언제잘수있을까123 ?", arr.toString())


                    for (i in 0 until arr.length()) {
                        val fuserObject = arr.getJSONObject(i)
                        Log.d("이이이이잉~~나는sad12  ?", arr[i].toString())

                        fuserId = fuserObject.getString("userId")
                        fuserLevel = fuserObject.getString("userLevel")
                        fuserProfileImg = fuserObject.getString("userProfileImg")
                        fuserProfile = AutoLogin.StringToBitmap(fuserProfileImg)

                        var fashin = Fashionista(fuserId, fuserLevel, fuserProfile)
                        FashionistaList.add(fashin)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
        val fashionistaUserRequest = FashionistaUser_Request(responseListener)
        val context : Context? = null
        val queue = Volley.newRequestQueue(context)
        queue.add(fashionistaUserRequest)


    }


    fun clearPro(context: Context) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_PRO, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.commit()
    }


}