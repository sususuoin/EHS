package com.example.ehs.Home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.ehs.Loading
import com.example.ehs.Login.AutoLogin
import com.example.ehs.MainActivity.Companion.codycolorRecommend
import com.example.ehs.R
import com.example.ehs.ml.ColorModel
import kotlinx.android.synthetic.main.activity_color_recommend.*
import kotlinx.android.synthetic.main.activity_pro_recommend.*
import kotlinx.android.synthetic.main.activity_pro_recommend.tv_userid
import kotlinx.android.synthetic.main.fragment_closet.*
import kotlinx.android.synthetic.main.loading.*
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class ColorRecommendActivity : AppCompatActivity() {

    lateinit var userId : String

    var bitmap: Bitmap? = null

    lateinit var colorcody : String
    var coloruserIdArr = ArrayList<String>()
    var colorplusImgPathArr = ArrayList<String>()
    var colorplusImgNameArr = ArrayList<String>()
    var colorplusImgStyleArr = ArrayList<String>()

    val colorcodyList = mutableListOf<ColorRecommend>()
    val adapter = ColorRecommendListAdapter(colorcodyList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_recommend)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
            this.window.statusBarColor = ContextCompat.getColor(this,R.color.white)
        }

        userId = AutoLogin.getUserId(this@ColorRecommendActivity)
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
        tv_loadingtitle.text = "컬러 추천"

        colorcody = AutoHome.getColorcody(this)
        when(colorcody) {
            "경쾌한" -> tv_colorDetail.text = "채도가 높고 명도가 살짝 높은 선명한 색으로 경쾌한 느낌을 주는 컬러입니다."
            "화려한" -> tv_colorDetail.text = "채도가 높은 원색 계열로 화려한 느낌을 주는 컬러입니다."
            "다이나믹한" -> tv_colorDetail.text = "채도가 높고 명도가 살짝 낮은 딥한 원색 계열로 다이나믹한 느낌을 주는 컬러입니다."
            "모던한" -> tv_colorDetail.text = "컬러감이 두드러지지 않는 무채색 계열로 모던한 느낌을 주는 컬러입니다."
            "점잖은" -> tv_colorDetail.text = "채도가 높고 명도가 낮은 색으로 점잖은 느낌을 주는 컬러입니다."
            "고상한" -> tv_colorDetail.text = "채도가 중간이고 명도가 살짝 높은 그레이 톤이 가미된 색으로 고상한 느낌을 주는 컬러입니다."
            "우아한" -> tv_colorDetail.text = "채도와 명도가 중간인 그레이 톤이 가미된 색으로 우아한 느낌을 주는 컬러입니다."
            "은은한" -> tv_colorDetail.text = "채도와 명도가 중간인 그래이 톤이 가미된 색으로 우아한 느낌을 주는 컬러입니다."
            "내츄럴한" -> tv_colorDetail.text = "채도가 높고 명도가 중간인 베이지 톤이 가미된 색으로 내츄럴한 느낌을 주는 컬러입니다."
            "귀여운" -> tv_colorDetail.text = "채도가 높고 명도가 중간인 파스텔 계열의 색으로 귀여운 느낌을 주는 컬러입니다."
            "맑은" -> tv_colorDetail.text = "채도와 명도가 중간인 파스텔 계열로 맑은 느낌을 주는 컬러입니다."
            "온화한" -> tv_colorDetail.text = "채도와 명도가 중간인 파스텔 계열에 회색 톤이 가미된 색으로 온화한 느낌을 주는 컬러입니다."
        }

        bitmap = BitmapFactory.decodeResource(resources, R.drawable.colortest)

        coloruserIdArr = AutoHome.getColoruserId(this@ColorRecommendActivity)
        colorplusImgPathArr = AutoHome.getColorplusImgPath(this@ColorRecommendActivity)
        colorplusImgNameArr = AutoHome.getColorplusImgName(this@ColorRecommendActivity)
        colorplusImgStyleArr = AutoHome.getColorplusImgStyle(this@ColorRecommendActivity)

        Log.d("zzz", colorplusImgNameArr.size.toString())
        var a_bitmap : Bitmap? = null
        for (i in 0 until colorplusImgNameArr.size) {
            val uThread: Thread = object : Thread() {
                override fun run() {
                    try {
                        Log.d("컬러추천액티비티", colorplusImgNameArr[i])

                        val url = URL(colorplusImgPathArr[i] + colorplusImgNameArr[i])

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
                }
            }
            uThread.start() // 작업 Thread 실행

            try {

                uThread.join()

                var colorcody = ColorRecommend(a_bitmap)
                colorcodyList.add(colorcody)

            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        recycler_colorcody.adapter = adapter
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