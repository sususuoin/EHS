package com.example.ehs

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_clothes_save.*


class ClothesSaveActivity : AppCompatActivity(){
    val TAG: String = "옷저장하는 화면"


    var bitmap :Bitmap? =null

    var uploadImgName : String? = ""
    lateinit var clothesImg : Bitmap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clothes_save)

        val intent = intent
        val arr = getIntent().getByteArrayExtra("clothesImg")
        clothesImg = BitmapFactory.decodeByteArray(arr, 0, arr!!.size)

        iv_clothes.setImageBitmap(clothesImg)

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

//            val intent = Intent(applicationContext, ClosetFragment::class.java)
//            startActivityForResult(intent, 101)


        }





        //은정아 혹시 이 메모를 보고있따면 이아래에 이거 필요없다면 지워주겟니 ? 밑에 주석처리해놓은 거는 지우지 말아죠
        fun onOptionsItemSelected(item: MenuItem): Boolean {
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
}



//
//        val uThread: Thread = object : Thread() {
//            override fun run() {
//                try {
//
//                    //서버에 올려둔 이미지 URL
//                    val url = URL("http://54.180.101.123/clothes/" +uploadImgName)
//
//
//                    //Web에서 이미지 가져온 후 ImageView에 지정할 Bitmap 만들기
//                    /* URLConnection 생성자가 protected로 선언되어 있으므로
//                     개발자가 직접 HttpURLConnection 객체 생성 불가 */
//                    val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
//
//                    /* openConnection()메서드가 리턴하는 urlConnection 객체는
//                    HttpURLConnection의 인스턴스가 될 수 있으므로 캐스팅해서 사용한다*/
//
//                    conn.setDoInput(true) //Server 통신에서 입력 가능한 상태로 만듦
//                    conn.connect() //연결된 곳에 접속할 때 (connect() 호출해야 실제 통신 가능함)
//                    val iss: InputStream = conn.getInputStream() //inputStream 값 가져오기
//                    bitmap =BitmapFactory.decodeStream(iss) // Bitmap으로 반환
//
//
//                } catch (e: MalformedURLException) {
//                    e.printStackTrace()
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//        }
//
//        uThread.start() // 작업 Thread 실행
//
//
//        try {
//
//            //메인 Thread는 별도의 작업을 완료할 때까지 대기한다!
//            //join() 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다림
//            //join() 메서드는 InterruptedException을 발생시킨다.
//            uThread.join()
//
//            //작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
//            //UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지 지정
//            iv_clothes.setImageBitmap(bitmap)
//
//        } catch (e: InterruptedException) {
//            e.printStackTrace()
//        }
//
//
//    }



