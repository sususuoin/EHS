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


    private var backKeyPressedTime: Long = 0

    // 첫 번째 뒤로 가기 버튼을 누를 때 표시
    private var toast: Toast? = null

    override fun onBackPressed() {
        //super.onBackPressed();
        // 기존 뒤로 가기 버튼의 기능을 막기 위해 주석 처리 또는 삭제

        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지났으면 Toast 출력
        // 2500 milliseconds = 2.5 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 1500) {
            backKeyPressedTime = System.currentTimeMillis()
            Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG).show()
            return
        }
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 1500) {
            Toast.makeText(this, "이용해 주셔서 감사합니다.", Toast.LENGTH_LONG).show()
            moveTaskToBack(true);
            finishAndRemoveTask();

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val registerIntent = Intent(this, RegisterActivity::class.java) // 인텐트를 생성

        tv_register.setOnClickListener {
            Log.d(TAG, "회원가입 클릭")
            startActivity(registerIntent)

        }

        tv_title.setOnClickListener {

        }

        if (AutoLogin.getUserId(this).isNullOrBlank()
            || AutoLogin.getUserPass(this).isNullOrBlank()
        ) {
            Login()
        } else { // SharedPreferences 안에 값이 저장되어 있을 때 -> MainActivity로 이동
            Toast.makeText(this, "${AutoLogin.getUserId(this)}님 자동 로그인 되었습니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    fun Login() {
        btn_login.setOnClickListener {
            Log.d(TAG, "로그인 버튼 클릭")


            var userId = et_id.text.toString()
            var userPw = et_pw.text.toString()

            val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
                override fun onResponse(response: String?) {
                    try {

                        var jsonObject = JSONObject(response)
                        var success = jsonObject.getBoolean("success")
                        Log.d(TAG, userId)

                        if (success) {
                            var userId = jsonObject.getString("userId")
                            var userPw = jsonObject.getString("userPw")
                            var userName = jsonObject.getString("userName")
                            var userEmail = jsonObject.getString("userEmail")
                            var userBirth = jsonObject.getString("userBirth")
                            var userGender = jsonObject.getString("userGender")
                            var userLevel = jsonObject.getString("userLevel")


                            Log.d(TAG, userId)

                            val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
                            mainIntent.putExtra("userId", userId);
                            mainIntent.putExtra("userPw", userPw);
                            mainIntent.putExtra("userName", userName);
                            mainIntent.putExtra("userEmail", userEmail);
                            mainIntent.putExtra("userBirth", userBirth);
                            mainIntent.putExtra("userGender", userGender);
                            mainIntent.putExtra("userLevel", userLevel);


                            AutoLogin.setUserId(this@LoginActivity, userId)
                            AutoLogin.setUserPass(this@LoginActivity, userPw)
                            Toast.makeText(this@LoginActivity, "${AutoLogin.getUserId(this@LoginActivity)}님 로그인 되었습니다.", Toast.LENGTH_SHORT).show()


                            val clothessaveIntent =
                                Intent(this@LoginActivity, ClothesSaveActivity::class.java)
                            clothessaveIntent.putExtra("userId", userId);

                            Log.d(TAG, userId)
                            startActivity(mainIntent)

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
            val loginRequest = Login_Request(userId, userPw, responseListener)
            val queue = Volley.newRequestQueue(this)
            queue.add(loginRequest)




        }

    }


    fun dialog(type: String) {
        val dialog = AlertDialog.Builder(this)
        val startIntent = Intent(this, MainActivity::class.java)

        if (type.equals("성공")) {
            dialog.setTitle("로그인")
            dialog.setMessage("성공")
            startActivity(startIntent)
        } else if (type.equals("실패")) {
            dialog.setTitle("로그인")
            dialog.setMessage("실패")
        }

        val dialog_listener = object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when (which) {
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG, "다이얼로그")
                }
            }
        }

        dialog.setPositiveButton("확인", dialog_listener)
        dialog.show()
    }

}