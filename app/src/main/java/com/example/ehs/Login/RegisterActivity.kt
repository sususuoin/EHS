package com.example.ehs.Login


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.BottomSheet.BottomSheet_fashion
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_ai.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.btn_register
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream


class RegisterActivity : AppCompatActivity(), BottomSheet_fashion.BottomSheetButtonClickListener {
    val TAG: String = "회원가입화면"

    var isExistBlank = false //회원가입 빈칸이 있을 때
    var bitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        val toolbar = findViewById(R.id.toolbar_register) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        //뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 툴바 설정 완료


        val loginintent = Intent(this, LoginActivity::class.java) // 인텐트를 생성

        val aiIntent = intent
        var airesult = aiIntent.getStringExtra("airesult")



        bitmap = BitmapFactory.decodeResource(this.resources, R.drawable.profile_castle)

        var userProfileImg = BitmapToString(bitmap!!)
        Log.d(TAG + "asdfasdF", userProfileImg!!)


        Log.d("인텐트 잘 받아와졌니~~?", airesult!!)

        if(airesult.toFloat() > 80) {
            Log.d("원투쓰리", "은정이 원투쓰리")
            tv_level.text = "전문가"
        }
        else {
            tv_level.text = "일반인"
            tv_HashTag.isVisible = false
        }

        rg_gender.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.rb_woman -> tv_gender.text = "여성"
                R.id.rb_man -> tv_gender.text = "남성"
            }
        }

        tv_HashTag.setOnClickListener {
            val bottomSheet = BottomSheet_fashion()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }

        btn_register.setOnClickListener {
            Log.d(TAG, "회원가입성공 클릭")
            Log.d(TAG, airesult!!)

            var userId = et_id.text.toString()
            var userPw = et_pw.text.toString()
            var userName = et_name.text.toString()
            var userEmail = et_email.text.toString()
            var userBirth = et_birth.text.toString()
            var userGender = tv_gender.text.toString()
            var userLevel = tv_level.text.toString()

            var HashTag: String = if(userLevel=="전문가") {
                tv_HashTag.text.toString()
            }else{
                "기본값"
            }



            if(userId.isEmpty() || userPw.isEmpty() || userName.isEmpty() || userEmail.isEmpty() || userBirth.isEmpty() || userGender.isEmpty() || userLevel.isEmpty()){
                //하나라도 빈칸이 있을 경우
                isExistBlank = true
            }

            if(!isExistBlank) {
                //빈칸이 없을경우

                val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
                    override fun onResponse(response: String?) {
                        try {
                            val jsonObject = JSONObject(response)
                            var success = jsonObject.getBoolean("success")

                            if(success) {
                                Toast.makeText(this@RegisterActivity, "회원가입 성공", Toast.LENGTH_SHORT).show()

                                startActivity(loginintent)
                                finish()

                                //회원가입 실패시
                            } else {

                                Toast.makeText(this@RegisterActivity, "회원가입 실패", Toast.LENGTH_SHORT).show()
                                return
                            }


                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }

                }

                //서버로 Volley를 이용해서 요청

                val registerRequest = Register_Request(userId,
                    userPw,
                    userName,
                    userEmail,
                    userBirth,
                    userGender,
                    userLevel,
                    HashTag,
                    userProfileImg,
                    responseListener)
                val queue = Volley.newRequestQueue(this@RegisterActivity)
                queue.add(registerRequest)

            }
            else {
                // 상태에 따라 다른 다이얼로그 띄워주기
                if(isExistBlank){   // 작성 안한 항목이 있을 경우
                    Toast.makeText(this@RegisterActivity, "빈칸을 채워주세요", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }


    fun BitmapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream() //바이트 배열을 차례대로 읽어 들이기위한 ByteArrayOutputStream클래스 선언
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos) //bitmap을 압축 (숫자 70은 70%로 압축한다는 뜻)
        val bytes = baos.toByteArray() //해당 bitmap을 byte배열로 바꿔준다.
        return Base64.encodeToString(bytes, Base64.DEFAULT) //String을 retrurn
    }

    /**
     * 툴바 뒤로가기 기능
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

    override fun onFashionButtonClicked(text: String) {
        tv_HashTag.text = text
    }


}