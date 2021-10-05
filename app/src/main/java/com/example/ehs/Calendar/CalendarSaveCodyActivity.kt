package com.example.ehs.Calendar

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.Login.AutoLogin
import com.example.ehs.MainActivity
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_calendar_save_cody.*
import kotlinx.android.synthetic.main.fragment_closet.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.HashMap

class CalendarSaveCodyActivity : AppCompatActivity() {

    lateinit var calendarName : String
    lateinit var calendarcodyImgName : String
    lateinit var userId : String

    lateinit var calendarcodyYear : List<String>
    lateinit var calendarcodyMonth : List<String>
    lateinit var calendarcodyDay : List<String>

    companion object {
        var calendarSaveContext: Context? = null
        var calendarsaveActivity_Dialog : ProgressDialog? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_save_cody)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
            this.window.statusBarColor = ContextCompat.getColor(this,R.color.white)
        }

        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        val toolbar = findViewById(R.id.toolbar_savecody) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        //뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 툴바 설정 완료
        calendarSaveContext=this

        userId = AutoLogin.getUserId(this)

        var intent = intent
        var arr = intent.getByteArrayExtra("savecody")
        var image : Bitmap = BitmapFactory.decodeByteArray(arr, 0, arr!!.size)

        //날짜 자르기
        var calendarcodyDate = AutoCalendar.getSelectday(this).split(" ")
        calendarcodyYear = calendarcodyDate[0].split("년")
        calendarcodyMonth = calendarcodyDate[1].split("월")
        calendarcodyDay = calendarcodyDate[2].split("일")

        iv_savecody.setImageBitmap(image)

        tv_selectday.text = AutoCalendar.getSelectday(this)

        btn_savecody.setOnClickListener {

            calendarsaveActivity_Dialog = ProgressDialog(this)
            calendarsaveActivity_Dialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            calendarsaveActivity_Dialog!!.setMessage("업로드 중입니다.")
            calendarsaveActivity_Dialog!!.setCanceledOnTouchOutside(false)
            calendarsaveActivity_Dialog!!.show()
            (CalendarMakeCodyActivity.calendarMakeContext as CalendarMakeCodyActivity).finish()
            (CalendarChoiceActivity.calendarChoiceContext as CalendarChoiceActivity).finish()
            Calendarsavecody(image)
        }
    }

    /**
     * 툴바 뒤로가기 기능
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun getFileDataFromDrawable(bitmap: Bitmap): ByteArray? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    fun Calendarsavecody(bitmap: Bitmap) {

        val calendarUpload_Request: CalendarUpload_Request = object : CalendarUpload_Request(
            Method.POST, "http://13.125.7.2/CalendarUpload_Request.php",
            Response.Listener<NetworkResponse> { response ->
                try {

                    val obj = JSONObject(String(response!!.data))
                    calendarcodyImgName = obj.get("file_name") as String

                    Log.d("서버에 저장되어진 파일이름", calendarcodyImgName)

                    Toast.makeText(this, calendarcodyImgName, Toast.LENGTH_SHORT).show()
                    uploadDB(userId, calendarcodyImgName)


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
        Volley.newRequestQueue(this).add(calendarUpload_Request)

    }

    fun uploadDB(userId: String, calendarcodyImgName: String) {
        val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
            override fun onResponse(response: String?) {
                try {
                    val jsonObject = JSONObject(response)
                    var success = jsonObject.getBoolean("success")

                    if(success) {

                        (MainActivity.mContext as MainActivity).CalendarImg()
                        Log.d("zz달력이라네zz", CalendarActivity.calendarNameArr.size.toString())

//                        val intent = Intent(this@CalendarSaveCodyActivity, CalendarActivity::class.java)
//                        startActivity(intent)
//                        finish()

                    } else {
                        Toast.makeText(this@CalendarSaveCodyActivity, "실패 두둥탁", Toast.LENGTH_LONG).show()
                        return
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }

        calendarName = calendarcodyImgName
        val calendarPath = "http://13.125.7.2/img/calendar/"
        val calendarCodySave_Request = CalendarCodySave_Request(userId, calendarPath, calendarName, calendarcodyYear[0], calendarcodyMonth[0], calendarcodyDay[0], responseListener)
        val queue = Volley.newRequestQueue(this@CalendarSaveCodyActivity)
        queue.add(calendarCodySave_Request)
    }


}