package com.example.ehs.Closet

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.BottomSheet.BottomSheet_fashion
import com.example.ehs.Login.AutoLogin
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_cody_save.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.*


class CodySaveActivity : AppCompatActivity(), BottomSheet_fashion.BottomSheetButtonClickListener {

    lateinit var bitmap : Bitmap


    lateinit var codyStyle : String
    var userId : String= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cody_save)
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


        iv_cody.setImageResource(R.drawable.exfirst)
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.exfirst)


        // 완료하기 버튼 클릭 시
        btn_complete_cody.setOnClickListener{
            uploadCody(bitmap)
        }



    }



    fun getFileDataFromDrawable(bitmap: Bitmap): ByteArray? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    fun uploadCody(bitmap: Bitmap) {
        val codyUploadRequest: CodyUpload_Request = object : CodyUpload_Request(
            Method.POST, "http://54.180.101.123/codyupload.php",
            Response.Listener<NetworkResponse> { response ->
                try {

                    val obj = JSONObject(String(response!!.data))
                    var codyImgName = obj.get("file_name") as String

                    Log.d("서버에 저장되어진 파일이름", codyImgName)

                    Toast.makeText(ClosetFragment.a, codyImgName, Toast.LENGTH_SHORT).show()

                    uploadDB(userId, codyImgName)


                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(ClosetFragment.a, error.message, Toast.LENGTH_LONG).show()
                Log.e("GotError", "" + error.message)
            }) {
            override fun getByteData(): Map<String, DataPart>? {
                val params: MutableMap<String, DataPart> = HashMap()
                val imagename = System.currentTimeMillis()
                val uploadImgName = imagename.toString()
                Log.d("하늘이는 민재이모친구라네", uploadImgName)
                params["image"] = DataPart("$uploadImgName.JPEG", getFileDataFromDrawable(bitmap)!!)
                return params
            }
        }

        //adding the request to volley
        Volley.newRequestQueue(ClosetFragment.a).add(codyUploadRequest)

    }

    fun uploadDB(userId: String, codyImgName: String ) {
        val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
            override fun onResponse(response: String?) {
                try {
                    val jsonObject = JSONObject(response)
                    var success = jsonObject.getBoolean("success")

                    if(success) {
                        Toast.makeText(this@CodySaveActivity, jsonObject.toString(), Toast.LENGTH_LONG).show()
                        finish()

                    } else {
                        Toast.makeText(this@CodySaveActivity, "실패 두둥탁", Toast.LENGTH_LONG).show()
                        return
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }

        val codyImgPath = "http://54.180.101.123/img/cody/"
        val codySave_Request = CodySave_Request(userId, codyImgPath, codyImgName, codyStyle, responseListener)
        val queue = Volley.newRequestQueue(this@CodySaveActivity)
        queue.add(codySave_Request)
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