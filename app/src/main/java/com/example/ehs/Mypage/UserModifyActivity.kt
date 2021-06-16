package com.example.ehs.Mypage

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import com.example.ehs.Login.AutoLogin
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_usermodify.*

class UserModifyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usermodify)
        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        val toolbar = findViewById(R.id.toolbar_modify) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        //뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 툴바 설정 완료


        var userName = AutoLogin.getUserName(this)
        var userPw = AutoLogin.getUserPw(this)
        var userBirth = AutoLogin.getUserBirth(this)
        var userGender = AutoLogin.getUserGender(this)
        var userEmail = AutoLogin.getUserEmail(this)
        var userProfileImg = AutoLogin.getUserProfileImg(this)
        var userProfile = AutoLogin.StringToBitmap(userProfileImg)

        et_userName.setText(userName)
        et_userPassword.setText(userPw)
        et_userBirth.setText(userBirth)
        et_userGender.setText(userGender)
        iv_userProfileImg.setImageBitmap(userProfile)

        btn_modify.setOnClickListener {
            userName = et_userName.text.toString()
            userPw = et_userPassword.text.toString()
            userBirth =  et_userBirth.text.toString()
            userGender =  et_userGender.text.toString()

            // AutoLogin set
            AutoLogin.setUserName(this@UserModifyActivity, userName)
            AutoLogin.setUserPw(this@UserModifyActivity, userPw)
            AutoLogin.setUserBirth(this@UserModifyActivity, userBirth)
            AutoLogin.setUserGender(this@UserModifyActivity, userGender)

            finish()



        }

    }

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

}