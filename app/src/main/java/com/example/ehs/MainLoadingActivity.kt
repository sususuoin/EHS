package com.example.ehs


import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.Fashionista.AutoPro
import com.example.ehs.Fashionista.FashionistaCody_Request
import com.example.ehs.Fashionista.FashionistaUser_Request
import com.example.ehs.Feed.AutoFeed
import com.example.ehs.Feed.FeedRanking_Request
import com.example.ehs.Login.LoginActivity
import kotlinx.android.synthetic.main.fragment_closet.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainLoadingActivity : Activity() {
    val TAG: String = "로딩화면"

    private var backKeyPressedTime: Long = 0

    override fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 1500) {
            backKeyPressedTime = System.currentTimeMillis()
            Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG).show()
            return
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 1500) {
            this.finishAffinity()
            System.runFinalization()
            System.exit(0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_loading)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
            this.window.statusBarColor = ContextCompat.getColor(this,R.color.white)
        }

        Log.d("로딩화면", "시작")
        startLoading()
    }

    fun startLoading() {
        val handler = Handler()
        handler.postDelayed(Runnable {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            FashionistaUser()
            Feed_ranking()
            finish() }, 2500)
        Log.d("로딩화면", "끝")
    }

    /**
     * 전문가 리스트 출력
     */
    fun FashionistaUser() {
        Log.d("로딩화면", "끝1")
        var fuserId: String
        var fuserLevel: String
        var fuserProfileImg : String

        var fuserIdArr = ArrayList<String>()
        var fuserLevelArr = ArrayList<String>()
        var fuserProImgArr = ArrayList<String>()

        val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
            override fun onResponse(response: String?) {
                try {

                    var jsonObject = JSONObject(response)

                    val arr: JSONArray = jsonObject.getJSONArray("response")

                    for (i in 0 until arr.length()) {
                        val fuserObject = arr.getJSONObject(i)

                        fuserId = fuserObject.getString("userId")
                        fuserLevel = fuserObject.getString("userLevel")
                        fuserProfileImg = fuserObject.getString("userProfileImg")
//                        fuserProfile = AutoLogin.StringToBitmap(fuserProfileImg)

                        fuserIdArr.add(fuserId)
                        fuserLevelArr.add(fuserLevel)
                        fuserProImgArr.add(fuserProfileImg)
                    }
                    AutoPro.setProuserId(this@MainLoadingActivity, fuserIdArr)
                    AutoPro.setProuserLevel(this@MainLoadingActivity, fuserLevelArr)
                    AutoPro.setProuserProImg(this@MainLoadingActivity, fuserProImgArr)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
        val fashionistaUserRequest = FashionistaUser_Request(responseListener)
        val queue = Volley.newRequestQueue(this)
        queue.add(fashionistaUserRequest)
    }

    fun Feed_ranking() {
        Log.d("로딩화면", "끝2")
        var feedNum: String
        var feed_userId: String
        var like_cnt: String
        var feed_ImgName: String

        var feedNumArr = ArrayList<String>()
        var feed_userIdArr = ArrayList<String>()
        var like_cntArr = ArrayList<String>()
        var feed_ImgNameArr = ArrayList<String>()

        val responseListener: Response.Listener<String?> =
            Response.Listener<String?> { response ->
                try {

                    var jsonObject = JSONObject(response)

                    val arr: JSONArray = jsonObject.getJSONArray("response")

                    if(arr.length() == 0 ) {
                        feedNumArr.clear()
                        feed_userIdArr.clear()
                        like_cntArr.clear()
                        feed_ImgNameArr.clear()
                    }
                    for (i in 0 until arr.length()) {
                        val Object = arr.getJSONObject(i)

                        feedNum = Object.getString("feedNum")
                        feed_userId = Object.getString("feed_userId")
                        like_cnt = Object.getString("like_cnt")
                        feed_ImgName = Object.getString("feed_ImgName")

                        feedNumArr.add(feedNum)
                        feed_userIdArr.add(feed_userId)
                        like_cntArr.add(like_cnt)
                        feed_ImgNameArr.add(feed_ImgName)

                    }
                    AutoFeed.setFeedRank_feedNum(this, feedNumArr)
                    AutoFeed.setFeedRank_feed_userId(this, feed_userIdArr)
                    AutoFeed.setFeedRank_like_cnt(this, like_cntArr)
                    AutoFeed.setFeedRank_feed_ImgName(this, feed_ImgNameArr)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        val feedRanking_Request = FeedRanking_Request(responseListener)
        val queue = Volley.newRequestQueue(this)
        queue.add(feedRanking_Request)
    }

}