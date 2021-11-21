package com.example.ehs.Admin

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.example.ehs.Login.AutoLogin
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_management_user.*
import java.io.ByteArrayOutputStream

class ManagementUser_Activity : AppCompatActivity() {

    val userManagementlist = mutableListOf<ManagementUser>()
    val adapter = ManagementUserListAdapter(userManagementlist)

    var muserIdArr = ArrayList<String>()
    var muserPwArr = ArrayList<String>()
    var muserNameArr = ArrayList<String>()
    var mnuserEmailArr = ArrayList<String>()
    var muserBirthArr = ArrayList<String>()
    var muserGenderArr = ArrayList<String>()
    var muserLevel2Arr = ArrayList<String>()
    var muserLevelArr = ArrayList<String>()
    var mHashTagArr = ArrayList<String>()
    var muserProfileImgArr = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_management_user)
        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        setSupportActionBar(toolbar_profile)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        //뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 툴바 설정 완료

        Managementchoice_Activity.adminProgressDialog!!.dismiss()

        muserIdArr = AutoManagement.getMuserId(this)
        muserPwArr = AutoManagement.getMuserPw(this)
        muserNameArr = AutoManagement.getMuserName(this)
        mnuserEmailArr = AutoManagement.getMuserEmail(this)
        muserBirthArr = AutoManagement.getMuserBirth(this)
        muserGenderArr = AutoManagement.getMuserGender(this)
        muserLevel2Arr = AutoManagement.getMuserLevel2(this)
        muserLevelArr = AutoManagement.getMuserLevel(this)
        mHashTagArr = AutoManagement.getMuserTag(this)
        muserProfileImgArr = AutoManagement.getMuserProfile(this)

        for (i in 0 until muserIdArr.size) {
            var fuserProfile = AutoLogin.StringToBitmap(muserProfileImgArr[i], 100, 100)
            var user = ManagementUser(muserIdArr[i], mHashTagArr[i], fuserProfile)
            userManagementlist.add(user)
        }

        adapter.setItemClickListener(object : ManagementUserListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                if (userManagementlist[position].name != "") {
                    val intent = Intent(this@ManagementUser_Activity, ManagementUserModify_Activity::class.java)
                    var index = muserIdArr.indexOf(userManagementlist[position].name)
                    intent.putExtra("muserId", userManagementlist[position].name)
                    intent.putExtra("muserPw", muserPwArr[index])
                    intent.putExtra("muserName", muserNameArr[index])
                    intent.putExtra("muserEmail", mnuserEmailArr[index])
                    intent.putExtra("muserBirth", muserBirthArr[index])
                    intent.putExtra("muserGender", muserGenderArr[index])
                    intent.putExtra("muserLevel2", muserLevel2Arr[index])
                    intent.putExtra("muserLevel", muserLevelArr[index])
                    intent.putExtra("muserTag", mHashTagArr[index])

                    val stream = ByteArrayOutputStream()
                    userManagementlist[position].profile!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val byteArray = stream.toByteArray()
                    intent.putExtra("muserProfile", byteArray)
                    Log.d("모지모지,", "ahwlahwl")
                    startActivity(intent)

                }

            }
        })

        rv_usermanagement.adapter = adapter
        adapter.notifyDataSetChanged()



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


}
