package com.example.ehs.Fashionista

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.Home.HomeFragment
import com.example.ehs.Login.AutoLogin
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_pro_recommend.*
import kotlinx.android.synthetic.main.fragment_closet.*
import kotlinx.android.synthetic.main.loading.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
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

        for ( i in  0 until proIdArr3.size) {
           var proNum =  proIdArr.lastIndexOf(proIdArr3[i])
            Log.d("proNum", proNum.toString())
            Log.d("proNum", proIdArr.toString())
        }

        Log.d("proNum1", proplusImgPathArr.toString())
        Log.d("proNum2", proplusImgNameArr.toString())
        var proId1bitmap1 = AutoLogin.StringToBitmap(proImgArr[proIdArr.lastIndexOf(proIdArr3[0])], 100, 100)
        var proId2bitmap2 = AutoLogin.StringToBitmap(proImgArr[proIdArr.lastIndexOf(proIdArr3[1])], 100, 100)
        var proId3bitmap3 = AutoLogin.StringToBitmap(proImgArr[proIdArr.lastIndexOf(proIdArr3[2])], 100, 100)

        iv_proImg1.setImageBitmap(proId1bitmap1)
        iv_proImg2.setImageBitmap(proId2bitmap2)
        iv_proImg3.setImageBitmap(proId3bitmap3)

        var a_bitmap : Bitmap? = null
        for (i in 0 until proplusImgPathArr.size) {
            val uThread: Thread = object : Thread() {
                override fun run() {
                    try {

                        val url = URL(proplusImgPathArr[i] + proplusImgNameArr[i])

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

                if(i <= proIdArr.lastIndexOf(proIdArr3[0])) {
                    Log.d("proNum", i.toString())
                    var asdf1 = ProRecommend(a_bitmap!!)
                    proRecommendList1.add(asdf1)
                } else if ( i <= proIdArr.lastIndexOf(proIdArr3[1])) {
                    var asdf2 = ProRecommend(a_bitmap!!)
                    proRecommendList2.add(asdf2)
                } else if ( i <= proIdArr.lastIndexOf(proIdArr3[2])) {
                    var asdf3 = ProRecommend(a_bitmap!!)
                    proRecommendList3.add(asdf3)
                }

//                var fashionistaCody = StyleRecommend(a_bitmap!!)
//                stylecodyList.add(fashionistaCody)

            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }


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

        ll_userid1.setOnClickListener{
            show_profile(proId1, proId1bitmap1!!)
        } // item 클릭하면 FashionistaProfile_Activity로 이동
        ll_userid2.setOnClickListener{
            show_profile(proId2, proId2bitmap2!!)
        }
        ll_userid3.setOnClickListener{
            show_profile(proId3, proId3bitmap3!!)
        }

    }

    fun show_profile(proId : TextView, proIdbitmap : Bitmap) {
        var dialog = ProgressDialog(this)
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog.setMessage("이동 중입니다.")
        dialog.show()

        Toast.makeText(this, "asdf${proId.text}",
            Toast.LENGTH_SHORT).show()
        var fashionistaId = proId.text.toString()
        var fashionistaProfile = proIdbitmap

        val stream = ByteArrayOutputStream()
        fashionistaProfile.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()

        var userId = fashionistaId
        var cuserId: String
        var plusImgName: String
        var plusImgPath: String

        var plusImgNameArr = ArrayList<String>()
        var plusImgPathArr = ArrayList<String>()

        val responseListener: Response.Listener<String?> =
            Response.Listener<String?> { response ->
                try {

                    var jsonObject = JSONObject(response)
                    val arr: JSONArray = jsonObject.getJSONArray("response")

                    if(arr.length() == 0) {
                        plusImgNameArr.clear()
                    }
                    else {
                        for (i in 0 until arr.length()) {
                            val plusObject = arr.getJSONObject(i)
                            cuserId = plusObject.getString("userId")
                            plusImgPath = plusObject.getString("plusImgPath")
                            plusImgName = plusObject.getString("plusImgName")

                            plusImgPathArr.add(plusImgPath)
                            plusImgNameArr.add(plusImgName)

                            Log.d("으음없는건가,..?", plusImgName)
                        }
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                AutoPro.setplusImgPath(this, plusImgPathArr)
                AutoPro.setplusImgName(this, plusImgNameArr)

                val intent = Intent(this, FashionistaProfile_Activity::class.java)
                intent.putExtra("fashionistaId", fashionistaId)
                intent.putExtra("fashionistaProfile", byteArray)

                dialog.dismiss()
                startActivity(intent)

            }
        val fashionistaProfileServer_Request = FashionistaProfileServer_Request(userId, responseListener)
        val queue = Volley.newRequestQueue(this)
        queue.add(fashionistaProfileServer_Request)

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