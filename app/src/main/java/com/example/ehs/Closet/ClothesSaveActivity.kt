package com.example.ehs.Closet

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.Login.AutoLogin
import com.example.ehs.BottomSheet.BottomSheet_category
import com.example.ehs.BottomSheet.BottomSheet_color
import com.example.ehs.BottomSheet.BottomSheet_season
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_clothes_save.*
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class ClothesSaveActivity : AppCompatActivity(){
    val TAG: String = "옷저장하는 화면"

    lateinit var clothesName : String
    lateinit var clothesImg : Bitmap

    val serverUrl = "http://54.180.101.123/upload3.php"
    var originURL :String = "http://54.180.101.123/clothes/origin/"
    lateinit var realURL : String

    lateinit var mProgressDialog: ProgressDialog

    override fun onDestroy() {
        super.onDestroy()

        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clothes_save)

        //사용자 아이디받아오기
        var userId = AutoLogin.getUserId(this@ClothesSaveActivity)

        val intent = intent
        val originImgName = getIntent().getStringExtra("originImgName")
        Log.d(TAG, originImgName!!)
        realURL = originURL+originImgName
//
//        clothesImg = setImg(realURL)
//
//        iv_clothes.setImageBitmap(clothesImg)


        var task = back()
        task.execute(originURL+originImgName);


//        val intent = intent
//        val arr = getIntent().getByteArrayExtra("clothesImg")
//        clothesImg = BitmapFactory.decodeByteArray(arr, 0, arr!!.size)
//        iv_clothes.setImageBitmap(clothesImg)

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


            Log.d(TAG, "서버에 저장을 시작합니다")
//
//            val job = GlobalScope.launch() {
//                uploadBitmap(clothesImg)
//
//            }
//            runBlocking {
//                job.join()
//                delay(3000L)  //3초동안 기다리기
//
//                uploadDB(userId)
//            }


            GlobalScope.launch(Dispatchers.Main) {
                launch(Dispatchers.Main) {
                    uploadBitmap(clothesImg)
                }

                delay(2000L)
                uploadDB(userId)
            }

            Log.d(TAG, "서버에 저장을 완료했다다")
            this.finish()


        }



        //은정아 혹시 이 메모를 보고있따면 이아래에 이거 필요없다면 지워주겟니 ?
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

//onCreate() 끝



    }






    fun getFileDataFromDrawable(bitmap: Bitmap): ByteArray? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }


    fun uploadBitmap(bitmap: Bitmap) {
        val clothesUploadRequest: ClothesUpload_Request =
            object : ClothesUpload_Request(
                Method.POST, serverUrl,
                Response.Listener<NetworkResponse> { response ->
                    try {

                        val obj = JSONObject(String(response!!.data))
                        Toast.makeText(this, obj.toString(), Toast.LENGTH_SHORT).show()


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
                    Log.e("GotError", "" + error.message)
                }) {
                override fun getByteData(): Map<String, DataPart>? {
                    val params: MutableMap<String, DataPart> = HashMap()
                    val imagename = System.currentTimeMillis()
                    clothesName = imagename.toString()
                    params["image"] = DataPart("$imagename.JPEG", getFileDataFromDrawable(bitmap)!!)
                    return params
                }
            }

        //adding the request to volley
        Volley.newRequestQueue(this).add(clothesUploadRequest)


    }


    fun uploadDB(userId: String) {
        val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
            override fun onResponse(response: String?) {
                try {
                    val jsonObject = JSONObject(response)
                    var success = jsonObject.getBoolean("success")

                    if(success) {
                        Toast.makeText(
                            this@ClothesSaveActivity, jsonObject.toString(), Toast.LENGTH_LONG
                        ).show()

                    } else {
                        Toast.makeText(this@ClothesSaveActivity, "실패 두둥탁", Toast.LENGTH_LONG).show()

                        return
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

        }

        val clothesPath = "/var/www/html/clothes/"
        val clothesSave_Request = ClothesSave_Request(userId, clothesPath, clothesName, responseListener)
        val queue = Volley.newRequestQueue(this@ClothesSaveActivity)
        queue.add(clothesSave_Request)
    }


     open inner class back : AsyncTask<String?, Int?, Bitmap>() {
         protected override fun onPreExecute() {

             // Create a progressdialog
             mProgressDialog = ProgressDialog(this@ClothesSaveActivity)
             mProgressDialog.setTitle("Loading...")
             mProgressDialog.setMessage("Image uploading...")
             mProgressDialog.setCanceledOnTouchOutside(false)
             mProgressDialog.setIndeterminate(false)
             mProgressDialog.show()
         }


        override fun doInBackground(vararg urls: String?): Bitmap {
            try {
                val myFileUrl = URL(urls[0])
                val conn: HttpURLConnection = myFileUrl.openConnection() as HttpURLConnection
                conn.doInput = true
                conn.connect()
                val iss: InputStream = conn.inputStream
                clothesImg = BitmapFactory.decodeStream(iss)

            } catch (e: IOException) {
                e.printStackTrace()
            }
            return clothesImg

        }

        override fun onPostExecute(img: Bitmap) {
            mProgressDialog.dismiss()

            iv_clothes.setImageBitmap(clothesImg)
        }


    }



//
//    fun setImg(originURL : String) : Bitmap {
//        try {
//            val myFileUrl = URL(originURL)
//            val conn: HttpURLConnection = myFileUrl.openConnection() as HttpURLConnection
//            conn.setDoInput(true)
//            conn.connect()
//            val iss: InputStream = conn.getInputStream()
//            clothesImg = BitmapFactory.decodeStream(iss)
//        } catch (e : IOException) {
//            e.printStackTrace()
//        }
//        return clothesImg
//    }
//
//

}
