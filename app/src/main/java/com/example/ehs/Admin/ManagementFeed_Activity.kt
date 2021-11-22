package com.example.ehs.Admin

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_management_feed.*
import kotlinx.android.synthetic.main.fragment_closet.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class ManagementFeed_Activity : AppCompatActivity() {

    val feedManagementlist = mutableListOf<ManagementFeed>()
    val adapter = ManagementFeedListAdapter(feedManagementlist)

    var mfeedNumArr = ArrayList<String>()
    var mfeed_userIdArr = ArrayList<String>()
    var mfeed_ImgNameArr = ArrayList<String>()
    var mfeed_styleArr = ArrayList<String>()
    var mfeed_dateArr = ArrayList<String>()

    var mfeedNumArr2 = ArrayList<String>()
    var mlike_cntArr = ArrayList<String>()
    var munlike_cntArr = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_management_feed)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
            this.window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        }
        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        setSupportActionBar(toolbar_profile)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        //뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 툴바 설정 완료


        rv_feedmanagement.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        var mfeedNum: String
        var mfeed_userId: String
        var mfeed_ImgName : String
        var mfeed_style : String
        var mfeed_date : String


//        nsprogress.isVisible = true
//        userManagementlist.clear()

        feednsprogress.isVisible = true
        mfeedNumArr.clear()
        mfeed_userIdArr.clear()
        mfeed_ImgNameArr.clear()
        mfeed_styleArr.clear()
        mfeed_dateArr.clear()

        val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
            override fun onResponse(response: String?) {
                try {

                    var jsonObject = JSONObject(response)

                    val arr: JSONArray = jsonObject.getJSONArray("response")

                    for (i in 0 until arr.length()) {
                        val fuserObject = arr.getJSONObject(i)

                        mfeedNum = fuserObject.getString("feedNum")
                        mfeed_userId = fuserObject.getString("feed_userId")
                        mfeed_ImgName = fuserObject.getString("feed_ImgName")
                        mfeed_style = fuserObject.getString("feed_style")
                        mfeed_date = fuserObject.getString("feed_date")

                        mfeedNumArr.add(mfeedNum)
                        mfeed_userIdArr.add(mfeed_userId)
                        mfeed_ImgNameArr.add(mfeed_ImgName)
                        mfeed_styleArr.add(mfeed_style)
                        mfeed_dateArr.add(mfeed_date)
                    }

                    getlikecnt()


                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
        val managementFeed_Request = ManagementFeed_Request(responseListener)
        val queue = Volley.newRequestQueue(this)
        queue.add(managementFeed_Request)
    }

    /**
     * 툴바 뒤로가기 기능
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun getlikecnt() {


        var mfeedNum2: String
        var mlike_cnt: String
        var munlike_cnt : String

        val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
            override fun onResponse(response: String?) {
                try {

                    var jsonObject = JSONObject(response)

                    val arr: JSONArray = jsonObject.getJSONArray("response")

                    for (i in 0 until arr.length()) {
                        val fuserObject = arr.getJSONObject(i)

                        mfeedNum2 = fuserObject.getString("feedNum2")
                        mlike_cnt = fuserObject.getString("like_cnt")
                        munlike_cnt = fuserObject.getString("unlike_cnt")

                        mfeedNumArr2.add(mfeedNum2)
                        mlike_cntArr.add(mlike_cnt)
                        munlike_cntArr.add(munlike_cnt)
                    }

                    parseResult(0, mfeedNumArr.size)


//                    for (i in 0 until mfeedNum.size) {
//                        var fuserProfile = AutoLogin.StringToBitmap(muserProfileImgArr[i], 100, 100)
//                        var user = ManagementUser(muserIdArr[i], muserLevelArr[i], fuserProfile, muserRegisterDateArr[i])
//                        userManagementlist.add(user)
//                        adapter.notifyDataSetChanged()
//                    }
//
//                    feednsprogress.isVisible = false

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
        val managementFeed_Request2 = ManagementFeed_Request2(responseListener)
        val queue = Volley.newRequestQueue(this@ManagementFeed_Activity)
        queue.add(managementFeed_Request2)
    }

    fun parseResult(before_page: Int, after_page: Int) {
        var a_bitmap: Bitmap? = null
        for (i in before_page until after_page) {
            val uThread: Thread = object : Thread() {
                override fun run() {
                    try {

                        Log.d("Closet프래그먼터리스트123", mfeed_ImgNameArr[i])

                        //서버에 올려둔 이미지 URL
                        val url = URL("http://13.125.7.2/img/cody/" + mfeed_ImgNameArr[i])

                        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection

                        conn.setDoInput(true) //Server 통신에서 입력 가능한 상태로 만듦
                        conn.connect() //연결된 곳에 접속할 때 (connect() 호출해야 실제 통신 가능함)
                        val iss: InputStream = conn.getInputStream() //inputStream 값 가져오기
                        a_bitmap = BitmapFactory.decodeStream(iss) // Bitmap으로 반환

                    } catch (e: MalformedURLException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            uThread.start() // 작업 Thread 실행

            try {

                uThread.join()

                var index = mfeedNumArr2.indexOf(mfeedNumArr[i])
                var managementFeed = ManagementFeed(mfeedNumArr[i], mfeed_userIdArr[i], a_bitmap, mfeed_dateArr[i], mlike_cntArr[index], munlike_cntArr[index])
                feedManagementlist.add(managementFeed)
                adapter.notifyDataSetChanged()

            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        feednsprogress.isVisible = false
        adapter.notifyDataSetChanged()
    }
}