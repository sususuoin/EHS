package com.example.ehs.Closet

import com.android.volley.AuthFailureError
import com.android.volley.Response

import com.android.volley.toolbox.StringRequest


class CodySave_Request (
    userId: String,
    codyImgPath : String,
    codyImgName : String,
    codyStyle :String,
    listener: Response.Listener<String?>?) : StringRequest(Method.POST, URL, listener, null) {

    private val map: MutableMap<String, String>

    @Throws(AuthFailureError::class)
    override fun getParams(): Map<String, String> {
        return map
    }

    companion object {
        //서버 URL 설정(php 파일 연동)
        private const val URL = "http://54.180.101.123/CodySave_Request.php"
    }

    //private Map<String, String>parameters;
    init {
        map = HashMap()
        map["userId"] = userId
        map["codyImgPath"] = codyImgPath
        map["codyImgName"] = codyImgName
        map["codyStyle"] = codyStyle
    }
}
