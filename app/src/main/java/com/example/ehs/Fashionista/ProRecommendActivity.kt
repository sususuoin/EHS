package com.example.ehs.Fashionista

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ehs.Home.HomeFragment
import com.example.ehs.Home.StyleRecommend
import com.example.ehs.Login.AutoLogin
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_pro_recommend.*
import kotlinx.android.synthetic.main.fragment_closet.*
import kotlinx.android.synthetic.main.loading.*
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class ProRecommendActivity : AppCompatActivity() {

    lateinit var userId : String

    val proRecommendList1 = mutableListOf<ProRecommend>()
    val proRecommendList2 = mutableListOf<ProRecommend>()
    val proRecommendList3 = mutableListOf<ProRecommend>()

    val adapter1 = ProRecommendyListAdapter(proRecommendList1)
    val adapter2 = ProRecommendyListAdapter(proRecommendList2)
    val adapter3 = ProRecommendyListAdapter(proRecommendList3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pro_recommend)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
            this.window.statusBarColor = ContextCompat.getColor(this,R.color.white)
        }

        userId = AutoLogin.getUserId(this@ProRecommendActivity)
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

        tv_loadingtitle.text = "전문가 추천"
        HomeFragment.homeloading?.finish()

        var proIdArr = AutoPro.getProProfileId(this)
        var proImgArr = AutoPro.getProProfileImg(this)
        var proStyle = AutoPro.getStyle(this)
        var proplusImgPathArr = AutoPro.getProplusImgPath(this)
        var proplusImgNameArr = AutoPro.getProplusImgName(this)

        val proIdArr2: HashSet<String> = HashSet(proIdArr)
        val proIdArr3: ArrayList<String> = ArrayList(proIdArr2)

        Log.d("aaa", proIdArr3.toString())
        tv_proStyle.text = proStyle
        proId1.text = proIdArr3[0]
        proId2.text = proIdArr3[1]
        proId3.text = proIdArr3[2]

        Log.d("afsadf" ,proIdArr.indexOf(proIdArr3[0]).toString())


        iv_proImg1.setImageBitmap(AutoLogin.StringToBitmap(proImgArr[0]))
        iv_proImg2.setImageBitmap(AutoLogin.StringToBitmap(proImgArr[1]))
        iv_proImg3.setImageBitmap(AutoLogin.StringToBitmap(proImgArr[2]))


        var bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.diao1)
        var bitmap2 = BitmapFactory.decodeResource(resources, R.drawable.diao2)
        var bitmap3 = BitmapFactory.decodeResource(resources, R.drawable.diao3)
        var bitmap4 = BitmapFactory.decodeResource(resources, R.drawable.diao4)
        var bitmap5 = BitmapFactory.decodeResource(resources, R.drawable.diao5)
        var bitmap6 = BitmapFactory.decodeResource(resources, R.drawable.diao6)
        var bitmap7 = BitmapFactory.decodeResource(resources, R.drawable.diao7)
        var bitmap8 = BitmapFactory.decodeResource(resources, R.drawable.diao8)
        var bitmap9 = BitmapFactory.decodeResource(resources, R.drawable.diao9)

        var asdf1 = ProRecommend(bitmap1)
        var asdf2 = ProRecommend(bitmap2)
        var asdf3 = ProRecommend(bitmap3)
        var asdf4 = ProRecommend(bitmap4)
        var asdf5 = ProRecommend(bitmap5)
        var asdf6 = ProRecommend(bitmap6)
        var asdf7 = ProRecommend(bitmap7)
        var asdf8 = ProRecommend(bitmap8)
        var asdf9 = ProRecommend(bitmap9)

        proRecommendList1.add(asdf1)
        proRecommendList1.add(asdf2)
        proRecommendList1.add(asdf3)
        proRecommendList1.add(asdf4)

        proRecommendList2.add(asdf5)
        proRecommendList2.add(asdf6)
        proRecommendList2.add(asdf7)

        proRecommendList3.add(asdf8)
        proRecommendList3.add(asdf9)

//        var a_bitmap : Bitmap? = null
//        for (i in 0 until proplusImgPathArr.size) {
//            val uThread: Thread = object : Thread() {
//                override fun run() {
//                    try {
//
//                        val url = URL(proplusImgPathArr[i] + proplusImgNameArr[i])
//
//                        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
//
//                        conn.setDoInput(true)
//                        conn.connect()
//                        val iss: InputStream = conn.getInputStream()
//                        a_bitmap = BitmapFactory.decodeStream(iss)
//
//                    } catch (e: MalformedURLException) {
//                        e.printStackTrace()
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//            uThread.start() // 작업 Thread 실행
//
//            try {
//
//                uThread.join()
//
//                var fashionistaCody = StyleRecommend(a_bitmap!!)
//                stylecodyList.add(fashionistaCody)
//
//            } catch (e: InterruptedException) {
//                e.printStackTrace()
//            }
//        }


        val Linear1 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rc_procody1.layoutManager = Linear1
        rc_procody1.setHasFixedSize(true)
        rc_procody1.adapter = adapter1

        val Linear2 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rc_procody2.layoutManager = Linear2
        rc_procody2.setHasFixedSize(true)
        rc_procody2.adapter = adapter2

        val Linear3 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rc_procody3.layoutManager = Linear3
        rc_procody3.setHasFixedSize(true)
        rc_procody3.adapter = adapter3

        adapter1.notifyDataSetChanged()
        adapter2.notifyDataSetChanged()
        adapter3.notifyDataSetChanged()
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