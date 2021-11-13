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

    lateinit var colorcody : String // 컬러 뭔지
    var coloruserIdArr = ArrayList<String>() // 유저 아이디
    var colorplusImgPathArr = ArrayList<String>()
    var colorplusImgNameArr = ArrayList<String>()
    var colorplusImgStyleArr = ArrayList<String>()

    val colorcodyList = mutableListOf<ColorRecommend>() // 컬러 가져온거
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

        colorcody = AutoHome.getColorcody(this)
        when(colorcody) {
            "Vivid" -> tv_colorDetail.text = "채도가 높고 명도가 중간인 원색으로 선명하고 다이나믹한 느낌을 주는 컬러입니다."
            "Bright" -> tv_colorDetail.text = "채도가 높고 명도가 살짝 높은 밝은 색으로 경쾌하고 발랄한 느낌을 주는 컬러입니다."
            "Strong" -> tv_colorDetail.text = "채도가 높고 명도가 살짝 낮은 색으로 강렬하고 화려한 느낌을 주는 컬러입니다."
            "Deep" -> tv_colorDetail.text = "채도가 높고 명도가 낮은 색으로 안정적이고 고풍스러운 느낌을 주는 컬러입니다."
            "Light" -> tv_colorDetail.text = "채도가 중간이고 명도가 높은 색으로 화사하고 귀여운 느낌을 주는 컬러입니다."
            "Soft" -> tv_colorDetail.text = "채도와 명도가 중간인 색으로 부드럽고 편안한 느낌을 주는 컬러입니다."
            "Dull" -> tv_colorDetail.text = "채도가 중간이고 명도가 다소 낮은 색으로 차분하고 고상한 느낌을 주는 컬러입니다."
            "Dark" -> tv_colorDetail.text = "채도가 중간이고 명도가 낮은 색으로 성숙하고 중후한 느낌을 주는 컬러입니다."
            "Pale" -> tv_colorDetail.text = "채도가 낮고 명도가 높은 색으로 연약하고 맑은 느낌을 주는 컬러입니다."
            "Light Grayish" -> tv_colorDetail.text = "채도가 낮고 명도가 다소 높은 그레이 톤이 가미된 색으로 은은하고 세련된 느낌을 주는 컬러입니다."
            "Grayish" -> tv_colorDetail.text = "채도가 낮고 명도가 다소 낮은 그레이 톤이 가미된 색으로 안정적이고 품위있는 느낌을 주는 컬러입니다."
            "Dark Grayish" -> tv_colorDetail.text = "채도가 낮고 명도가 낮은 그레이 톤이 가미된 색으로 무게감이 느껴주고 어두운 느낌을 주는 컬러입니다."
            "Colorless" -> tv_colorDetail.text = "컬러감이 두드러지지 않는 무채색 계열로 모던한 느낌을 주는 컬러입니다."
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