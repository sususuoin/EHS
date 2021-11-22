package com.example.ehs.Admin

import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest


class ManagementUserModify_Request(
    userId : String,
    userPw : String,
    userName : String,
    userEmail : String,
    userBirth : String,
    userGender : String,
    userLevel : String,
    userLevel2 : String,
    listener: Response.Listener<String?>?) : StringRequest(Method.POST, URL, listener, null) {

    private val map: MutableMap<String, String>

    @Throws(AuthFailureError::class)
    override fun getParams(): Map<String, String> {
        return map
    }

    companion object {
        //서버 URL 설정(php 파일 연동)
        private const val URL = "http://13.125.7.2/ManagementUserModify_Request.php"
    }

    init {
        map = HashMap()
        map["userId"] = userId
        map["userPw"] = userPw
        map["userName"] = userName
        map["userEmail"] = userEmail
        map["userBirth"] = userBirth
        map["userGender"] = userGender
        map["userLevel"] = userLevel
        map["userLevel2"] = userLevel2

    }
}