package com.example.ehs.Login


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.AI.AIActivity
import com.example.ehs.MainActivity
import com.example.ehs.R
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


        tv_register.setOnClickListener {
            Log.d(TAG, "회원가입 클릭")
            val aiActivity = Intent(this, AIActivity::class.java) // 인텐트를 생성
            startActivity(aiActivity)

        }


        if (AutoLogin.getUserId(this)==""|| AutoLogin.getUserId(this)==null || AutoLogin.getUserPw(this).isBlank()) {
            //아이디입력창이 비어있다면
            Login()
        } else {
            // SharedPreferences 안에 값이 저장되어 있을 때 -> MainActivity로 이동
            Toast.makeText(this, "${AutoLogin.getUserId(this)}님 자동 로그인 되었습니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        //키보드입력시 다른 곳 클릭시 키보드 내려감
        layout_login.setOnClickListener{
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(et_id.windowToken, 0)
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
                            var HashTag = jsonObject.getString("HashTag")
                            var userProfileImg = jsonObject.getString("userProfileImg")


                            Log.d(TAG, userId)

                            val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)

                            AutoLogin.setUserId(this@LoginActivity, userId)
                            AutoLogin.setUserPw(this@LoginActivity, userPw)
                            AutoLogin.setUserName(this@LoginActivity, userName)
                            AutoLogin.setUserEmail(this@LoginActivity, userEmail)
                            AutoLogin.setUserBirth(this@LoginActivity, userBirth)
                            AutoLogin.setUserGender(this@LoginActivity, userGender)
                            AutoLogin.setUserLevel(this@LoginActivity, userLevel)
                            AutoLogin.setUserProfileImg(this@LoginActivity, userProfileImg)

                            Toast.makeText(this@LoginActivity, "${AutoLogin.getUserId(this@LoginActivity)}님 로그인 되었습니다.", Toast.LENGTH_SHORT).show()

                            Log.d(TAG, userId)
                            startActivity(mainIntent)
                            finish()



                        } else {
                            Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
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



}