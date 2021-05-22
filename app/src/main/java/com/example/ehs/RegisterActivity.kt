package com.example.ehs


import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONException
import org.json.JSONObject


class RegisterActivity : AppCompatActivity() {
    val TAG: String = "회원가입화면"

    var isExistBlank = false //회원가입 빈칸이 있을 때



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val mainIntent = Intent(this, LoginActivity::class.java) // 인텐트를 생성



        rg_gender.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.rb_woman -> tv_gender.text = "여성"
                R.id.rb_man -> tv_gender.text = "남성"
            }
        }

        btn_register.setOnClickListener {
            Log.d(TAG, "회원가입성공 클릭")


            var userId = et_id.text.toString()
            var userPw = et_pw.text.toString()
            var userName = et_name.text.toString()
            var userEmail = et_email.text.toString()
            var userBirth = et_birth.text.toString()

            var userGender = tv_gender.text.toString()
            var userLevel = tv_level.text.toString()



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
                                dialog("회원가입")


                                //회원가입 실패시
                            } else {

                                dialog("회원가입 실패")
                                return
                            }


                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }

                }

                //서버로 Volley를 이용해서 요청
                val registerRequest = Register_Request(userId, userPw, userName, userEmail, userBirth, userGender, userLevel, responseListener)
                val queue = Volley.newRequestQueue(this@RegisterActivity)
                queue.add(registerRequest)

            }
            else {
                // 상태에 따라 다른 다이얼로그 띄워주기
                if(isExistBlank){   // 작성 안한 항목이 있을 경우
                    dialog("blank")
                }
            }

        }


        tv_back.setOnClickListener {
            Log.d(TAG, "뒤로가기클릭")

            startActivity(mainIntent)

        }

    }





    // 회원가입 실패시 다이얼로그를 띄워주는 메소드
    fun dialog(type: String){
        val dialog = AlertDialog.Builder(this)

        // 작성 안한 항목이 있을 경우
        if(type.equals("blank")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("입력란을 모두 작성해주세요")
        }
        else if(type.equals("회원가입")){
            dialog.setTitle("회원가입성공")
            dialog.setMessage("가입을 축하드립니다 ~")
        }
        else if(type.equals("회원가입 실패")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("통신오류")
        }

        val dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG, "다이얼로그")
                }
            }
        }

        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()
    }


}