package com.example.ehs.Home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ehs.Fashionista.AutoPro
import com.example.ehs.Login.AutoLogin
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_style_recommend.*
import kotlinx.android.synthetic.main.fragment_closet.*
import kotlinx.android.synthetic.main.loading.*
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class StyleRecommendActivity : AppCompatActivity() {

    lateinit var userId : String

    val stylecodyList = mutableListOf<StyleRecommend>()
    val adapter = StyleRecommendListAdapter(stylecodyList)

    var bitmap: Bitmap? = null
    var fuserIdArr = ArrayList<String>()
    var fcodyImgName = ArrayList<String>()
    var fcodyStyle = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_style_recommend)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
            this.window.statusBarColor = ContextCompat.getColor(this,R.color.white)
        }

        userId = AutoLogin.getUserId(this@StyleRecommendActivity)
        tv_userid.text = userId
        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        val toolbar = findViewById(R.id.toolbar_prorecommend) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        //뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 툴바 설정 완료

        HomeFragment.homeloading?.finish()

        fuserIdArr = AutoPro.getFuserId(this@StyleRecommendActivity)
        fcodyImgName = AutoPro.getFcodyImgName(this@StyleRecommendActivity)
        fcodyStyle = AutoPro.getFcodyStyle(this@StyleRecommendActivity)

        Log.d("zzz", fcodyImgName.size.toString())
        var a_bitmap : Bitmap? = null
        for (i in 0 until fcodyImgName.size) {
            val uThread: Thread = object : Thread() {
                override fun run() {
                    try {
                        Log.d("컬러추천액티비티", fcodyImgName[i])

                        val url = URL("http://13.125.7.2/img/cody/" + fcodyImgName[i])

                        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection

                        conn.setDoInput(true)
                        conn.connect()
                        val iss: InputStream = conn.getInputStream()
                        a_bitmap = BitmapFactory.decodeStream(iss)

                    } catch (e: MalformedURLException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    var fashionistaCody = StyleRecommend(a_bitmap!!)
                    stylecodyList.add(fashionistaCody)
                }
            }
            uThread.start() // 작업 Thread 실행

            try {

                uThread.join()

            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        val Linear = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_stylecody.layoutManager = Linear
        recycler_stylecody.setHasFixedSize(true)

        recycler_stylecody.adapter = adapter
        adapter.notifyDataSetChanged()

    }

    /**
     * 툴바 뒤로가기 버튼 기능 구현
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

}