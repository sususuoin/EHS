package com.example.ehs.Admin

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.BottomSheet.BottomSheet_fashion
import com.example.ehs.BottomSheet.BottomSheet_gender
import com.example.ehs.BottomSheet.BottomSheet_level
import com.example.ehs.BottomSheet.BottomSheet_level2
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_management_usermodify.*
import kotlinx.android.synthetic.main.fragment_closet.*
import org.json.JSONException
import org.json.JSONObject

class ManagementUserModify_Activity : AppCompatActivity(), BottomSheet_level2.BottomSheetButtonClickListener,  BottomSheet_level.BottomSheetButtonClickListener, BottomSheet_gender.BottomSheetButtonClickListener, BottomSheet_fashion.BottomSheetButtonClickListener{

    var userModifyActivity_Dialog : ProgressDialog? = null

    var userId : String = ""
    var userPw : String = ""
    var userName : String = ""
    var userEmail : String = ""
    var userBirth : String = ""
    var userGender : String = ""
    var userLevel : String = ""
    var userLevel2 : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_management_usermodify)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
            this.window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        }
        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        setSupportActionBar(toolbar_modify)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        //뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 툴바 설정 완료

        //키보드입력시 다른 곳 클릭시 키보드 내려감
        ll_manausermodify.setOnClickListener{
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(et_userName.windowToken, 0)
        }

        tv_pwreset.setOnClickListener {
            var pwresetalert = AlertDialog.Builder(this)
            pwresetalert.setTitle("초기화하기")
            pwresetalert.setMessage("비밀번호를 초기화하시겠습니까? (초기화 비밀번호 : asdf1234)")
            var listener = object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    when (p1) {
                        // 확인 버튼 클릭 시
                        DialogInterface.BUTTON_POSITIVE -> {
                            et_userPassword.setText("asdf1234")
                        }
                    }
                }
            }
            pwresetalert.setPositiveButton("확인", listener)
            pwresetalert.setNegativeButton("취소", null)
            pwresetalert.show()

        }

        btn_edit.setOnClickListener {
            var savealert = AlertDialog.Builder(this)
            savealert.setTitle("수정하기")
            savealert.setMessage("수정모드로 변경하시겠습니까?")

            // 버튼 클릭시에 무슨 작업을 할 것인가!
            var listener = object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    when (p1) {
                        // 확인 버튼 클릭 시
                        DialogInterface.BUTTON_POSITIVE -> {
                            //수정하기
                            tv_level.isEnabled = true
                            tv_level2.isEnabled = true
                            et_userName.isEnabled = true
                            et_userPassword.isEnabled = true
                            et_userBirth.isEnabled = true
                            et_email.isEnabled = true
                            tv_userGender.isEnabled = true
                            tv_pwreset.isEnabled = true

                            btn_modify.isVisible = false
                            btn_modify2.isVisible = true
                        }
                    }
                }
            }
            savealert.setPositiveButton("확인", listener)
            savealert.setNegativeButton("취소", null)
            savealert.show()

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
        btn_modify2.setOnClickListener {

            userId = et_userId.text.toString()
            userPw = et_userPassword.text.toString()
            userName = et_userName.text.toString()
            userEmail = et_email.text.toString()
            userBirth = et_userBirth.text.toString()
            userGender = tv_userGender.text.toString()
            userLevel = tv_level.text.toString()

            if(userLevel == "전문가") {
                userLevel2 = "LV5"
            } else {
                userLevel2 = tv_level2.text.toString()
            }

            if (tv_level == null || et_userName == null || et_userPassword == null || et_userBirth == null || et_email == null || tv_userGender == null) {
                //빈칸이 잇을 때
                Toast.makeText(this, "빈칸을 채워주세요", Toast.LENGTH_SHORT).show()
            }
            else {

                userModifyActivity_Dialog = ProgressDialog(this)
                userModifyActivity_Dialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                userModifyActivity_Dialog?.setMessage("업로드 중입니다.")
                userModifyActivity_Dialog?.setCanceledOnTouchOutside(false)
                userModifyActivity_Dialog?.show()

                saveData()

            }

        }

    }

    fun saveData() {
        val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
            override fun onResponse(response: String?) {
                try {
                    val jsonObject = JSONObject(response)
                    var success = jsonObject.getBoolean("success")

                    if(success) {
                        Toast.makeText(this@ManagementUserModify_Activity, "수정 성공", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@ManagementUserModify_Activity, "수정 실패", Toast.LENGTH_SHORT).show()
                        return
                    }

                    tv_level.isEnabled = false
                    tv_level2.isEnabled = false
                    et_userName.isEnabled = false
                    et_userPassword.isEnabled = false
                    et_userBirth.isEnabled = false
                    et_email.isEnabled = false
                    tv_userGender.isEnabled = false
                    tv_pwreset.isEnabled = false

                    btn_modify.isVisible = true
                    btn_modify2.isVisible = false
                    userModifyActivity_Dialog!!.dismiss()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

        }

        //서버로 Volley를 이용해서 요청

        val managementUserModify_Request = ManagementUserModify_Request(userId, userPw, userName, userEmail, userBirth, userGender, userLevel, userLevel2, responseListener)
        val queue = Volley.newRequestQueue(this)
        queue.add(managementUserModify_Request)

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
        tv_userGender.setText(text)
    }

    override fun onFashionButtonClicked(text: String) {
        TODO("Not yet implemented")
    }

}