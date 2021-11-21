package com.example.ehs.Admin

import android.app.ProgressDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.ehs.BottomSheet.BottomSheet_fashion
import com.example.ehs.BottomSheet.BottomSheet_gender
import com.example.ehs.BottomSheet.BottomSheet_level
import com.example.ehs.BottomSheet.BottomSheet_level2
import com.example.ehs.Login.AutoLogin
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_management_usermodify.*
import kotlinx.android.synthetic.main.activity_management_usermodify.et_email
import kotlinx.android.synthetic.main.activity_management_usermodify.tv_level
import kotlinx.android.synthetic.main.activity_register.*

class ManagementUserModify_Activity : AppCompatActivity(), BottomSheet_level2.BottomSheetButtonClickListener,  BottomSheet_level.BottomSheetButtonClickListener, BottomSheet_gender.BottomSheetButtonClickListener, BottomSheet_fashion.BottomSheetButtonClickListener{

    var userModifyActivity_Dialog : ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_management_usermodify)
        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        setSupportActionBar(toolbar_modify)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        //뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 툴바 설정 완료

        //키보드입력시 다른 곳 클릭시 키보드 내려감
        layout_usermodify.setOnClickListener{
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(et_userName.windowToken, 0)
        }
        Log.d("모지모지,", "ahwlahwl")
        val intent = intent
        val muserId = intent.getStringExtra("muserId")
        val muserPw = intent.getStringExtra("muserPw")
        val muserName = intent.getStringExtra("muserName")
        val muserEmail = intent.getStringExtra("muserEmail")
        val muserBirth = intent.getStringExtra("muserBirth")
        val muserGender = intent.getStringExtra("muserGender")
        val muserLevel2 = intent.getStringExtra("muserLevel2")
        val muserLevel = intent.getStringExtra("muserLevel")
        val muserTag = intent.getStringExtra("muserTag")
        val muserProfile = intent.getByteArrayExtra("muserProfile")

        val muserProfile2 = BitmapFactory.decodeByteArray(muserProfile, 0, muserProfile!!.size)
//        var userProfile = AutoLogin.StringToBitmap(muserProfile, 100, 100)

        title_id.setText(muserId)
        et_userId.setText(muserId)
        et_userName.setText(muserName)
        et_userPassword.setText(muserPw)
        et_userBirth.setText(muserBirth)
        tv_userGender.setText(muserGender)
        et_email.setText(muserEmail)
        iv_userProfileImg.setImageBitmap(muserProfile2)

        tv_level.setText(muserLevel)
        if(muserLevel == "일반인") {
            tvv_level2.isVisible = true
            tv_level2.isVisible = true
            tv_level2.setText(muserLevel2)
        }

        tv_level.setOnClickListener {
            val bottomSheet = BottomSheet_level()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }

        tv_level2.setOnClickListener {
            val bottomSheet = BottomSheet_level2()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }

        tv_userGender.setOnClickListener {
            val bottomSheet = BottomSheet_gender()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }


        //수정완료
        btn_modify.setOnClickListener {

//            if (et_userName == null || et_userPassword == null) {
//                //빈칸이 잇을 때
//                Toast.makeText(this, "빈칸을 채워주세요", Toast.LENGTH_SHORT).show()
//            }
//            else {
//
//                userModifyActivity_Dialog = ProgressDialog(this)
//                userModifyActivity_Dialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
//                userModifyActivity_Dialog?.setMessage("업로드 중입니다.")
//                userModifyActivity_Dialog?.setCanceledOnTouchOutside(false)
//                userModifyActivity_Dialog?.show()
//
//                setData()
//                finish()
//            }

        }

    }

//    fun setData() {
//        userName = et_userName.text.toString()
//        userPw = et_userPassword.text.toString()
//        userBirth =  et_userBirth.text.toString()
//        userEmail = et_email.text.toString()
//        userGender =  tv_userGender.text.toString()
//
//        // AutoLogin set
//        AutoLogin.setUserName(this@ManagementUserModify_Activity, userName)
//        AutoLogin.setUserPw(this@ManagementUserModify_Activity, userPw)
//        AutoLogin.setUserBirth(this@ManagementUserModify_Activity, userBirth)
//        AutoLogin.setUserEmail(this@ManagementUserModify_Activity, userEmail)
//        AutoLogin.setUserGender(this@ManagementUserModify_Activity, userGender)
//
//        userModifyActivity_Dialog?.dismiss()
//    }

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

    override fun onLevel2ButtonClicked(text: String) {
        tv_level2.setText(text)
    }

    override fun onLevelButtonClicked(text: String) {
        tv_level.setText(text)
    }

    override fun onGenderButtonClicked(text: String) {
        tv_gender.setText(text)
    }

    override fun onFashionButtonClicked(text: String) {
        TODO("Not yet implemented")
    }

}