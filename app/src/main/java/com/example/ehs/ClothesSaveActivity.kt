package com.example.ehs

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_clothes_save.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class ClothesSaveActivity : AppCompatActivity(){
    val TAG: String = "옷저장하는 화면"


    var bitmap :Bitmap? =null

    var uploadFileName : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clothes_save)

        val intent = getIntent()
        uploadFileName = intent.getStringExtra("uploadFileName")


        // 액션바 대신 툴바를 사용하도록 설정한다
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        ab.setDisplayHomeAsUpEnabled(true) // 툴바 설정 완료


        tv_category.setOnClickListener {
            val bottomSheet = BottomSheet_category()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }

        tv_color.setOnClickListener {
            val bottomSheet = BottomSheet_color()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }

        tv_season.setOnClickListener {
            val bottomSheet = BottomSheet_season()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }

        // 에디트 버튼 클릭
        btn_edit.setOnClickListener {

        }


        //완료하기 버튼클릭
        btn_complete.setOnClickListener {
            Log.d(TAG, uploadFileName!!)
            var userId = "asdf"

            saveToDB(userId, uploadFileName!!)



        }

        val uThread: Thread = object : Thread() {
            override fun run() {
                try {

                    //서버에 올려둔 이미지 URL
                    val url = URL("http://54.180.101.123/clothes/" +uploadFileName)


                    //Web에서 이미지 가져온 후 ImageView에 지정할 Bitmap 만들기
                    /* URLConnection 생성자가 protected로 선언되어 있으므로
                     개발자가 직접 HttpURLConnection 객체 생성 불가 */
                    val conn: HttpURLConnection = url.openConnection() as HttpURLConnection

                    /* openConnection()메서드가 리턴하는 urlConnection 객체는
                    HttpURLConnection의 인스턴스가 될 수 있으므로 캐스팅해서 사용한다*/

                    conn.setDoInput(true) //Server 통신에서 입력 가능한 상태로 만듦
                    conn.connect() //연결된 곳에 접속할 때 (connect() 호출해야 실제 통신 가능함)
                    val iss: InputStream = conn.getInputStream() //inputStream 값 가져오기
                    bitmap =BitmapFactory.decodeStream(iss) // Bitmap으로 반환


                } catch (e: MalformedURLException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        uThread.start() // 작업 Thread 실행


        try {

            //메인 Thread는 별도의 작업을 완료할 때까지 대기한다!
            //join() 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다림
            //join() 메서드는 InterruptedException을 발생시킨다.
            uThread.join()

            //작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
            //UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지 지정
            iv_clothes.setImageBitmap(bitmap)

        } catch (e: InterruptedException) {
            e.printStackTrace()
        }


    }

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



    private fun saveToDB(userId: String, clothesName :String) {
        val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
            override fun onResponse(response: String?) {
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getBoolean("success")

                    if (success) {
                        Toast.makeText(this@ClothesSaveActivity, "저장성공", Toast.LENGTH_SHORT).show()
                        Log.d("제발이잉이ㅣ", "이것이 저장성공인것인가")


                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    return
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
        val clothesPath = "/var/www/html/clothes/"
        val clothesSave_Request = ClothesSave_Request(userId, clothesPath, clothesName, responseListener)
        val queue = Volley.newRequestQueue(this@ClothesSaveActivity)
        queue.add(clothesSave_Request)

    }
}




