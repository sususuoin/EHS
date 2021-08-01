package com.example.ehs.Closet

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.BottomSheet.BottomSheet_category
import com.example.ehs.BottomSheet.BottomSheet_color
import com.example.ehs.BottomSheet.BottomSheet_season
import com.example.ehs.Login.AutoLogin
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_clothes_save.*
import kotlinx.android.synthetic.main.bottomsheet_category.*
import kotlinx.android.synthetic.main.fragment_closet.*
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class ClothesSaveActivity : AppCompatActivity(), BottomSheet_category.BottomSheetButtonClickListener, BottomSheet_color.BottomSheetButtonClickListener, BottomSheet_season.BottomSheetButtonClickListener {
    val TAG: String = "옷저장하는 화면"

    lateinit var clothesName : String
    lateinit var clothesImg : Bitmap

    val serverUrl = "http://13.125.7.2/upload3.php"
    var originURL :String = "http://13.125.7.2/img/clothes/origin/"
    lateinit var realURL : String

    lateinit var mProgressDialog: ProgressDialog
    var userId : String= ""

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
        userId = AutoLogin.getUserId(this@ClothesSaveActivity)

        val intent = intent
        val originImgName = getIntent().getStringExtra("originImgName")
        Log.d(TAG, originImgName!!)
        realURL = originURL+originImgName
//
//        clothesImg = setImg(realURL)
//
//        iv_clothes.setImageBitmap(clothesImg)


        var task = back()
        task.execute(realURL);


//        val intent = intent
//        val arr = getIntent().getByteArrayExtra("clothesImg")
//        clothesImg = BitmapFactory.decodeByteArray(arr, 0, arr!!.size)
//        iv_clothes.setImageBitmap(clothesImg)

        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        //뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 툴바 설정 완료
        
        // 처음에 컬러버튼 안보이게
        btn_colorview.visibility = View.INVISIBLE


        tv_category.setOnClickListener {
            val bottomSheet: BottomSheet_category = BottomSheet_category()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }

        ll_color.setOnClickListener {
            val bottomSheet = BottomSheet_color()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }

        tv_season.setOnClickListener {
            val bottomSheet = BottomSheet_season()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }

        // 에디트 버튼 클릭
        btn_edit_clothes.setOnClickListener {

        }


        //완료하기 버튼클릭
        btn_complete.setOnClickListener {


            Log.d(TAG, "서버에 저장을 시작합니다")
            uploadBitmap(clothesImg)


        }

//onCreate() 끝

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
                        clothesName = obj.getString("file_name")

                        Log.d("은정이와 수인이는 호롤로로 ", clothesName)


                        uploadDB(userId)




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
//                    clothesName = "$imagename.JPEG"

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

                        Log.d(TAG, "서버에 저장을 완료했다다")
                        finish()

                    } else {
                        Toast.makeText(this@ClothesSaveActivity, "실패 두둥탁", Toast.LENGTH_LONG).show()

                        return
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

        }


        var clothesColor : String = tv_color.text as String
        Log.e("옷컬러검색", clothesColor)
        val clothesPath = "http://13.125.7.2/img/clothes/"
        val clothesSave_Request = ClothesSave_Request(userId, clothesPath, clothesName, clothesColor, responseListener)
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

    // 바텀시트에서 선택한거 적용
    override fun onCategoryButtonClicked(text: String) {
        tv_category.text = text
    }
    override fun onColorButtonClicked(text: String) {
        tv_color.text = text
        btn_colorview.visibility = View.VISIBLE
        if(text == "흰색"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.white))
        }
        if(text == "크림"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.cream))

        }
        if(text == "연회색"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.lightgray))
        }
        if(text == "진회색"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.darkgray))
        }
        if(text == "검정"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.black))
        }
        if(text == "주황"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.orange))
        }
        if(text == "베이지"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.beige))
        }
        if(text == "노랑"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.yellow))
        }
        if(text == "연두"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.lightgreen))
        }
        if(text == "하늘"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.skyblue))
        }
        if(text == "분홍"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.pink))
        }
        if(text == "연분홍"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.lightpink))
        }
        if(text == "초록"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.green))
        }
        if(text == "카키"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.kaki))
        }
        if(text == "파랑"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.blue))
        }
        if(text == "빨강"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.red))
        }
        if(text == "와인"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.wine))
        }
        if(text == "갈색"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.brown))
        }
        if(text == "보라"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.purple))
        }
        if(text == "네이비"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.navy))
        }
    }
    override fun onSeasonButtonClicked(text: String) {
        tv_season.text = text
    }


}
