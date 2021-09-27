package com.example.ehs.Calendar

import com.android.volley.AuthFailureError
import com.android.volley.Response

import com.android.volley.toolbox.StringRequest


class CalendarCodySave_Request (
    userId: String,
    calendarPath : String,
    calendarName : String,
    calendarYear :String,
    calendarMonth : String,
    calendarDay : String,
    listener: Response.Listener<String?>?) : StringRequest(Method.POST, URL, listener, null) {

    private val map: MutableMap<String, String>

    @Throws(AuthFailureError::class)
    override fun getParams(): Map<String, String> {
        return map
    }

    companion object {
        //서버 URL 설정(php 파일 연동)
        private const val URL = "http://13.125.7.2/CalendarCodySave_Request.php"
    }

    //private Map<String, String>parameters;
    init {
        map = HashMap()
        map["userId"] = userId
        map["calendarPath"] = calendarPath
        map["calendarName"] = calendarName
        map["calendarYear"] = calendarYear
        map["calendarMonth"] = calendarMonth
        map["calendarDay"] = calendarDay

    }
}
