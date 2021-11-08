package com.example.ehs.Home

import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest

class CodyRandom_Request (
    sql_top : String,
    sql_bottom : String,
    sql_shoes : String,
    sql_outer : String,
    sql_bag : String,
    listener: Response.Listener<String?>?) : StringRequest(Method.POST, URL, listener, null) {

    private val map: MutableMap<String, String>

    @Throws(AuthFailureError::class)
    override fun getParams(): Map<String, String> {
        return map
    }

    companion object {
        //서버 URL 설정(php 파일 연동)
        private const val URL = "http://13.125.7.2/CodyRandom_Request.php"
    }

    init {
        map = HashMap()
        map["sql_top"] = sql_top
        map["sql_bottom"] = sql_bottom
        map["sql_shoes"] = sql_shoes
        map["sql_outer"] = sql_outer
        map["sql_bag"] = sql_bag
    }
}

