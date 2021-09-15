package com.example.ehs.Feed

import com.android.volley.AuthFailureError
import com.android.volley.Response

import com.android.volley.toolbox.StringRequest


class FeedCodySave_Request (
    feed_userId: String,
    feed_ImgName : String,
    feed_style : String,
    listener: Response.Listener<String?>?) : StringRequest(Method.POST, URL, listener, null) {

    private val map: MutableMap<String, String>

    @Throws(AuthFailureError::class)
    override fun getParams(): Map<String, String> {
        return map
    }

    companion object {
        //서버 URL 설정(php 파일 연동)
        private const val URL = "http://13.125.7.2/FeedCodySave_Request.php"
    }

    //private Map<String, String>parameters;
    init {
        map = HashMap()
        map["feed_userId"] = feed_userId
        map["feed_ImgName"] = feed_ImgName
        map["feed_style"] = feed_style
    }
}
