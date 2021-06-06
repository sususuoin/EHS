package com.example.ehs.Closet

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap


object AutoCloset {
    private val MY_CLOSET : String = "closet"

    fun setClothesName(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_CLOSET, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_clothesName", input)
        editor.commit()
    }

    fun getClothesName(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_CLOSET, Context.MODE_PRIVATE)
        return prefs.getString("MY_clothesName", "").toString()
    }

}