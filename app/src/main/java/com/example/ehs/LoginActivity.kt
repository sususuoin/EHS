package com.example.ehs

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    val TAG: String = "로그인화면"





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val registerIntent = Intent(this, RegisterActivity::class.java) // 인텐트를 생성
        val startIntent = Intent(this, MainActivity::class.java)

        btn_login.setOnClickListener{
            Log.d(TAG, "로그인 버튼 클릭")


            var userId = et_id.text.toString()
            var userPw = et_pw.text.toString()

            val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
                override fun onResponse(response: String?) {
                    try {

                        var jsonObject = JSONObject(response)
                        var success = jsonObject.getBoolean("success")
                        Log.d(TAG, userId)

                        if(success) {
                            var userId = jsonObject.getString("userId")
                            var userPw = jsonObject.getString("userPw")
                            var userName = jsonObject.getString("userName")
                            var userEmail = jsonObject.getString("userEmail")
                            var userBirth = jsonObject.getString("userBirth")
                            var userGender = jsonObject.getString("userGender")
                            var userLevel = jsonObject.getString("userLevel")


                            Log.d(TAG, userId)

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra( "userId", userId );
                            intent.putExtra( "userPw", userPw );
                            intent.putExtra( "userName", userName );
                            intent.putExtra( "userEmail", userEmail );
                            intent.putExtra( "userBirth", userBirth );
                            intent.putExtra( "userGender", userGender );
                            intent.putExtra( "userLevel", userLevel );

                            Log.d(TAG, userId)
                            Toast.makeText(this@LoginActivity, "로그인성공", Toast.LENGTH_SHORT).show()
                            startActivity(intent)

                            //dialog("성공")



                            //회원가입 실패시
                        } else {
                            dialog("실패")
                            return
                        }


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
            val loginRequest = LoginRequest(userId, userPw, responseListener)
            val queue = Volley.newRequestQueue(this)
            queue.add(loginRequest)



        }

        tv_register.setOnClickListener {
            Log.d(TAG, "회원가입 클릭")
            startActivity(registerIntent)

        }

    }


    fun dialog(type: String){
        val dialog = AlertDialog.Builder(this)
        val startIntent = Intent(this, MainActivity::class.java)

        if(type.equals("성공")){
            dialog.setTitle("로그인")
            dialog.setMessage("성공")
            startActivity(startIntent)
        }
        else if(type.equals("실패")){
            dialog.setTitle("로그인")
            dialog.setMessage("실패")
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