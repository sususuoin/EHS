package com.example.ehs.Fashionista

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.Closet.ClosetFragment
import com.example.ehs.Login.AutoLogin
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_fashionista_profile.*
import kotlinx.android.synthetic.main.fashionista_profile_item.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class FashionistaProfile_Activity : AppCompatActivity() {

    val REQUEST_OPEN_GALLERY = 2
    lateinit var bitmap : Bitmap
    lateinit var resized : Bitmap

    lateinit var userId : String
    lateinit var fashionistaId : String

    val FashionistaFeedList = mutableListOf<FashionistaUserProfiles>()

    var FashionistaImgPathArr = ArrayList<String>()
    var FashionistaImgNameArr = ArrayList<String>()
    var a_bitmap : Bitmap? = null

    var adapter = FashionistaProfileAdapter(FashionistaFeedList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fashionista_profile)
        userId = AutoLogin.getUserId(this)

        val intent = intent
        fashionistaId = intent.getStringExtra("fashionistaId").toString()
        val arr = intent.getByteArrayExtra("fashionistaProfile")
        val fashionistaProfile = BitmapFactory.decodeByteArray(arr, 0, arr!!.size)
        iv_profile.setImageBitmap(fashionistaProfile)

        //Log.d("비트맵", fashionistaProfile!!)
        tv_profileid.text = fashionistaId

        if(fashionistaId == userId) {
            btn_profilePlus.isVisible
        } else {
            btn_profilePlus.isVisible = false
        }

        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        val toolbar = findViewById(R.id.toolbar_profile) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        // 툴바에 뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 여기까지 툴바 설정 완료
        

        btn_profilePlus.setOnClickListener {
            openGallery()
        }


        val gridLayoutManager = GridLayoutManager(applicationContext, 3)
        rv_feed.layoutManager = gridLayoutManager
//        rv_feed.setHasFixedSize(true)

        rv_feed.adapter = adapter
        adapter.notifyDataSetChanged()


    }

    override fun onResume() {
        super.onResume()
        FashionistaImgPathArr = AutoPro.getplusImgPath(this)
        FashionistaImgNameArr = AutoPro.getplusImgName(this)

        Log.d("텔미", FashionistaImgNameArr.toString())
        for (i in 0 until FashionistaImgNameArr.size) {
            val uThread: Thread = object : Thread() {
                override fun run() {
                    try {
                        Log.d("zzzzasd", FashionistaImgPathArr[i])
                        val url = URL(FashionistaImgPathArr[i] + FashionistaImgNameArr[i])

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
                count()
                var fashionistaFeed = FashionistaUserProfiles(a_bitmap)
                FashionistaFeedList.add(fashionistaFeed)

            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        adapter.notifyDataSetChanged()

    }

    /**
     * 툴바 뒤로가기 버튼 액션 설정
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
    } // 툴바 뒤로가기 액션 설정 끝
    

    /**
     * 갤러리 오픈 함수
     */
    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(intent, REQUEST_OPEN_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            when(requestCode) { //resultCode가 Ok이고
                REQUEST_OPEN_GALLERY -> { // requestcode가 REQUEST_OPEN_GALLERY이면
                    val currentImageUrl: Uri? = data?.data // data의 data형태로 들어옴
//                    uploadImgName = getName(currentImageUrl)
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,
                            currentImageUrl)
                        resized = Bitmap.createScaledBitmap(bitmap!!, 500, 500, true)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        else {
            Toast.makeText(this, "취소하였습니다.", Toast.LENGTH_SHORT).show()
        }

        val intent = Intent(this, ProfilePlus_Activity::class.java)
        val stream = ByteArrayOutputStream()
        resized.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val plusImgArr = stream.toByteArray()
        intent.putExtra("plusImgArr", plusImgArr)
        startActivity(intent)

    }

    fun count() {
        var userId = fashionistaId
        var FollowerCount: String
        var PostCount: String
        var HashTag: String

        val responseListener: Response.Listener<String?> =
            Response.Listener<String?> { response ->
                try {

                    var jsonObject = JSONObject(response)
                    val arr: JSONArray = jsonObject.getJSONArray("response")

                    for (i in 0 until arr.length()) {
                        val countObject = arr.getJSONObject(i)
                        FollowerCount = countObject.getString("FollowerCount")
                        PostCount = countObject.getString("PostCount")
                        HashTag = countObject.getString("HashTag")

                        Log.d("으음없는건가,..1212?", FollowerCount)
                        Log.d("으음없는건가,..1212?", PostCount)

                        tv_follower.text = FollowerCount
                        tv_post.text = PostCount
                        tv_hashtag.text = "#"+HashTag

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        val fashionistaProfileCount_Request = FashionistaProfileCount_Request(userId!!,
            responseListener)
        val queue = Volley.newRequestQueue(this)
        queue.add(fashionistaProfileCount_Request)

    }



}