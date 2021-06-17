package com.example.ehs.Mypage

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import com.example.ehs.BottomSheet.BottomSheet_fashion
import com.example.ehs.BottomSheet.BottomSheet_gender
import com.example.ehs.Login.AutoLogin
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_usermodify.*

class UserModifyActivity : AppCompatActivity(), BottomSheet_gender.BottomSheetButtonClickListener{

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
        tv_userGender.setText(userGender)
        et_email.setText(userEmail)
        iv_userProfileImg.setImageBitmap(userProfile)


        tv_userGender.setOnClickListener {
            val bottomSheet = BottomSheet_gender()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }

        btn_modify.setOnClickListener {
            userName = et_userName.text.toString()
            userPw = et_userPassword.text.toString()
            userBirth =  et_userBirth.text.toString()
            userEmail = et_email.text.toString()
            userGender =  tv_userGender.text.toString()

            // AutoLogin set
            AutoLogin.setUserName(this@UserModifyActivity, userName)
            AutoLogin.setUserPw(this@UserModifyActivity, userPw)
            AutoLogin.setUserBirth(this@UserModifyActivity, userBirth)
            AutoLogin.setUserEmail(this@UserModifyActivity, userEmail)
            AutoLogin.setUserGender(this@UserModifyActivity, userGender)

            finish()



        }

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

    // 성별 바텀 시트에서 값 받아오기
    override fun onGenderButtonClicked(text: String) {
        tv_userGender.setText(text)
    }

}