package com.example.ehs.Closet

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.BottomSheet.BottomSheet_fashion
import com.example.ehs.Fashionista.ProfilePlusSave_Request
import com.example.ehs.Feed.FeedCodySave_Request
import com.example.ehs.Login.AutoLogin
import com.example.ehs.MainActivity
import com.example.ehs.R
import com.example.ehs.ml.ColorModel
import kotlinx.android.synthetic.main.activity_cody_save.*
import kotlinx.android.synthetic.main.fragment_closet.*
import org.json.JSONException
import org.json.JSONObject
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.ByteArrayOutputStream
import java.util.*


class CodySaveActivity : AppCompatActivity(), BottomSheet_fashion.BottomSheetButtonClickListener {

    var codyStyle : String = ""
    var userId : String= ""

    var codyOpen : Boolean = false

    companion object {
        const val TAG : String = "코디세이브 액티비티"

        var codySaveContext: Context? = null
        var codysaveActivity_Dialog : ProgressDialog? = null
    }

    lateinit var codycolorRecommend : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cody_save)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
            this.window.statusBarColor = ContextCompat.getColor(this,R.color.white)
        }

        codySaveContext=this
        userId = AutoLogin.getUserId(this@CodySaveActivity)
        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        val toolbar = findViewById(R.id.toolbar_codysave) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        //뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 툴바 설정 완료


        // 바텀시트 열기
        tv_fashion.setOnClickListener {
            val bottomSheet = BottomSheet_fashion()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }


        val intent = intent
        var arr = intent.getByteArrayExtra("saveBitmap")
        var image : Bitmap = BitmapFactory.decodeByteArray(arr, 0, arr!!.size)

        iv_cody.setImageBitmap(image)


        swich_open.setOnClickListener {
            codyOpen = swich_open.isChecked
            Log.d("zzzzdafd", codyOpen.toString())
        }

        // 완료하기 버튼 클릭 시
        btn_complete_cody.setOnClickListener{

            codycolor(image)

            if(codyStyle == "" || codyStyle == null ) {
                Toast.makeText(this@CodySaveActivity, "스타일을 꼭 설정해주세요", Toast.LENGTH_LONG).show()
            }
            else {
                codysaveActivity_Dialog = ProgressDialog(this)
                codysaveActivity_Dialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                codysaveActivity_Dialog!!.setMessage("업로드 중입니다.")
                codysaveActivity_Dialog!!.setCanceledOnTouchOutside(false)
                codysaveActivity_Dialog!!.show()

                uploadCody(image)
            }

        }



    }

    fun codycolor(bitmap : Bitmap) {

        val resized1 : Bitmap = Bitmap.createScaledBitmap(bitmap!!, 224, 224, true)
        val model = ColorModel.newInstance(this)

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)

        val tbuffer1 = TensorImage.fromBitmap(resized1)
        val byteBuffer1 = tbuffer1.buffer

        inputFeature0.loadBuffer(byteBuffer1)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        var asdf = ArrayList<Double>()
        var colorLabelArr = ArrayList<String>()
        colorLabelArr.add("경쾌한")
        colorLabelArr.add("고상한")
        colorLabelArr.add("귀여운")
        colorLabelArr.add("내추럴한")
        colorLabelArr.add("다이나믹한")
        colorLabelArr.add("맑은")
        colorLabelArr.add("모던한")
        colorLabelArr.add("온화한")
        colorLabelArr.add("우아한")
        colorLabelArr.add("은은한")
        colorLabelArr.add("점잖은")
        colorLabelArr.add("화려한")

        for(i in 0 until outputFeature0.floatArray.size) {
            asdf.add(outputFeature0.floatArray[i].div(255.0)*100)
        }
        var max = asdf.indexOf(asdf.max())
        codycolorRecommend = colorLabelArr[max]
        Log.d("컬러추천", colorLabelArr[max])
        Log.d("컬러추천", asdf.max().toString())

        // Releases model resources if no longer used.
        model.close()

    }



    fun getFileDataFromDrawable(bitmap: Bitmap): ByteArray? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    fun uploadCody(bitmap: Bitmap) {

        val codyUploadRequest: CodyUpload_Request = object : CodyUpload_Request(
            Method.POST, "http://13.125.7.2/CodyUpload_Request.php",
            Response.Listener<NetworkResponse> { response ->
                try {

                    val obj = JSONObject(String(response!!.data))
                    var codyImgName = obj.get("file_name") as String

                    Log.d("서버에 저장되어진 파일이름", codyImgName)

                    Toast.makeText(this, codyImgName, Toast.LENGTH_SHORT).show()
                    uploadDB(userId, codyImgName)


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
                val uploadImgName = imagename.toString()
                Log.d("하늘이는 민재이모친구라네", uploadImgName)
                params["image"] = DataPart("$uploadImgName.PNG", getFileDataFromDrawable(bitmap)!!)
                return params
            }
        }

        //adding the request to volley
        Volley.newRequestQueue(this).add(codyUploadRequest)

    }

    fun uploadDB(userId: String, codyImgName: String) {
        val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
            override fun onResponse(response: String?) {
                try {
                    val jsonObject = JSONObject(response)
                    var success = jsonObject.getBoolean("success")

                    if(success) {
//                        Toast.makeText(this@CodySaveActivity, jsonObject.toString(), Toast.LENGTH_LONG).show()

                        var userLevel = AutoLogin.getUserLevel(this@CodySaveActivity)
                        if(codyOpen) {
                            if(userLevel == "전문가"){
                                Log.d("sdfㅁㅁㅁㅁㅁㅋ", "프로필에 저장")
                                uploadDB_fashionistaprofile(userId, codyImgName)

                            } else if(userLevel == "일반인"){
                                Log.d("sdfㅁㅁㅁㅁㅁㅋㅋㅋ1", "피드에 저장")
                                uploadDB_feed(userId, codyImgName)
                            }
                        }
                        (CodyMakeActivity.codyContext as CodyMakeActivity).finish()
                        (MainActivity.mContext as MainActivity).CodyImg()
                        (MainActivity.mContext as MainActivity).FeedImg()


                    } else {
                        Toast.makeText(this@CodySaveActivity, "실패 두둥탁", Toast.LENGTH_LONG).show()
                        return
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }

        val codyImgPath = "http://13.125.7.2/img/cody/"
        val codySave_Request = CodySave_Request(userId, codyImgPath, codyImgName, codyStyle, codyOpen, responseListener)
        val queue = Volley.newRequestQueue(this@CodySaveActivity)
        queue.add(codySave_Request)
    }


    fun uploadDB_feed(userId: String, codyImgName: String) {
        val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
            override fun onResponse(response: String?) {
                try {
                    val jsonObject = JSONObject(response)
                    var success = jsonObject.getBoolean("success")

                    if(success) {
                        Log.d(TAG, "피드테이블에 코디저장")

                    } else {
                        Log.d(TAG, "피드테이블에 코디저장 실패")
                        return
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }

        var feed_userId = userId
        var feed_ImgName = codyImgName
        var feed_style = codyStyle

        val feedCodySave_Request = FeedCodySave_Request(feed_userId, feed_ImgName, feed_style, responseListener)
        val queue = Volley.newRequestQueue(this@CodySaveActivity)
        queue.add(feedCodySave_Request)
    }


    fun uploadDB_fashionistaprofile(userId: String, codyImgName: String) {
        val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
            override fun onResponse(response: String?) {
                try {
                    val jsonObject = JSONObject(response)
                    var success = jsonObject.getBoolean("success")

                    if(success) {
                        Log.d(TAG, "fashionistaprofile 테이블에 코디저장")

                    } else {
                        Log.d(TAG, "fashionistaprofile 테이블에 코디저장 실패")
                        return
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }

        val plusImgPath = "http://13.125.7.2/img/cody/"
        var plusImgName = codyImgName
        var plusContent = ""
        var plusImgStyle = codyStyle

        val profilePlusSave_Request = ProfilePlusSave_Request(userId, plusImgPath, plusImgName, plusImgStyle, plusContent, codycolorRecommend, responseListener)
        val queue = Volley.newRequestQueue(this@CodySaveActivity)
        queue.add(profilePlusSave_Request)
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
    
    // 바텀시트에서 선택한 항목 보여주기
    override fun onFashionButtonClicked(text: String) {
        tv_fashion.text = text
        codyStyle = text

    }
}