package com.example.ehs.Login

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64


object AutoLogin {
    private val MY_ACCOUNT : String = "account"

    fun setUserId(context: Context, input: String?) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT,
            Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_ID", input)
        editor.commit()
    }

    fun getUserId(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT,
            Context.MODE_PRIVATE)
        return prefs.getString("MY_ID", "").toString()
    }

    fun setUserPw(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT,
            Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_PASS", input)
        editor.commit()
    }

    fun getUserPw(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT,
            Context.MODE_PRIVATE)
        return prefs.getString("MY_PASS", "").toString()
    }

    fun clearUser(context: Context) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT,
            Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.commit()
    }


    fun setUserName(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT,
            Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_NAME", input)
        editor.commit()
    }

    fun getUserName(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT,
            Context.MODE_PRIVATE)
        return prefs.getString("MY_NAME", "").toString()
    }

    fun setUserEmail(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT,
            Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_EMAIL", input)
        editor.commit()
    }

    fun getUserEmail(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT,
            Context.MODE_PRIVATE)
        return prefs.getString("MY_EMAIL", "").toString()
    }

    fun setUserBirth(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT,
            Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_BIRTH", input)
        editor.commit()
    }

    fun getUserBirth(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT,
            Context.MODE_PRIVATE)
        return prefs.getString("MY_BIRTH", "").toString()
    }

    fun setUserGender(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT,
            Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_GENDER", input)
        editor.commit()
    }

    fun getUserGender(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT,
            Context.MODE_PRIVATE)
        return prefs.getString("MY_GENDER", "").toString()
    }

    fun setUserLevel2(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT,
            Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_LEVEL2", input)
        editor.commit()
    }

    fun getUserLevel2(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT,
            Context.MODE_PRIVATE)
        return prefs.getString("MY_LEVEL2", "").toString()
    }

    fun setUserLevel(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT,
            Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_LEVEL", input)
        editor.commit()
    }

    fun getUserLevel(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT,
            Context.MODE_PRIVATE)
        return prefs.getString("MY_LEVEL", "").toString()
    }

    fun setUserProfileImg(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT,
            Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_PROFILEIMG", input)
        editor.commit()
    }

    fun getUserProfileImg(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT,
            Context.MODE_PRIVATE)
        return prefs.getString("MY_PROFILEIMG", "").toString()
    }

    fun StringToBitmap(encodedString: String?): Bitmap? {

        try{
            val encodeByte: ByteArray = Base64.decode(encodedString, Base64.DEFAULT) // String 화 된 이미지를  base64방식으로 인코딩하여 byte배열을 만듬
            val options = BitmapFactory.Options()
            options.inSampleSize = 4
            val src = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size) //만들어진 bitmap을 return
            return Bitmap.createScaledBitmap(src, 100, 100, true)
        }catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

}