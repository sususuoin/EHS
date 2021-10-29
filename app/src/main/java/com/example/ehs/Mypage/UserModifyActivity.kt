package com.example.ehs.Mypage

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.ehs.BottomSheet.BottomSheet_gender
import com.example.ehs.Login.AutoLogin
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_usermodify.*

class UserModifyActivity : AppCompatActivity(), BottomSheet_gender.BottomSheetButtonClickListener{

    var userModifyActivity_Dialog : ProgressDialog? = null

    var userName : String = ""
    var userPw : String = ""
    var userBirth : String = ""
    var userGender : String = ""
    var userEmail : String = ""
    var userProfileImg : String = ""

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

        //키보드입력시 다른 곳 클릭시 키보드 내려감
        layout_usermodify.setOnClickListener{
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(et_userName.windowToken, 0)
        }

        userName = AutoLogin.getUserName(this)
        userPw = AutoLogin.getUserPw(this)
        userBirth = AutoLogin.getUserBirth(this)
        userGender = AutoLogin.getUserGender(this)
        userEmail = AutoLogin.getUserEmail(this)
        userProfileImg = AutoLogin.getUserProfileImg(this)
        var userProfile = AutoLogin.StringToBitmap(userProfileImg, 100, 100)

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


        //수정완료
        btn_modify.setOnClickListener {

            if (et_userName == null || et_userPassword == null) {
                //빈칸이 잇을 때
                Toast.makeText(this, "빈칸을 채워주세요", Toast.LENGTH_SHORT).show()
            }
            else {

                userModifyActivity_Dialog = ProgressDialog(this)
                userModifyActivity_Dialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                userModifyActivity_Dialog?.setMessage("업로드 중입니다.")
                userModifyActivity_Dialog?.setCanceledOnTouchOutside(false)
                userModifyActivity_Dialog?.show()

                setData()
                finish()
            }

        }

    }

    fun setData() {
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

        userModifyActivity_Dialog?.dismiss()
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