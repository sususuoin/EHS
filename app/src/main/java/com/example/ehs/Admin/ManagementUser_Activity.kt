package com.example.ehs.Admin

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.Login.AutoLogin
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_management_user.*
import kotlinx.android.synthetic.main.activity_management_user.nsprogress
import kotlinx.android.synthetic.main.fragment_closet.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
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
    var muserRegisterDateArr = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_management_user)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
            this.window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        }
        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        setSupportActionBar(toolbar_profile)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        //뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 툴바 설정 완료

        Managementchoice_Activity.adminProgressDialog!!.dismiss()

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

    override fun onResume() {
        super.onResume()
        var muserId: String
        var muserPw: String
        var muserName : String
        var mnuserEmail : String
        var muserBirth : String
        var muserGender : String
        var muserLevel2 : String
        var muserLevel : String
        var mHashTag : String
        var muserProfileImg : String
        var muserRegisterDate : String

        nsprogress.isVisible = true
        userManagementlist.clear()
        muserIdArr.clear()
        muserPwArr.clear()
        muserNameArr.clear()
        mnuserEmailArr.clear()
        muserBirthArr.clear()
        muserGenderArr.clear()
        muserLevel2Arr.clear()
        muserLevelArr.clear()
        mHashTagArr.clear()
        muserProfileImgArr.clear()

        val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
            override fun onResponse(response: String?) {
                try {

                    var jsonObject = JSONObject(response)

                    val arr: JSONArray = jsonObject.getJSONArray("response")

                    for (i in 0 until arr.length()) {
                        val fuserObject = arr.getJSONObject(i)

                        muserId = fuserObject.getString("userId")
                        muserPw = fuserObject.getString("userPw")
                        muserName = fuserObject.getString("userName")
                        mnuserEmail = fuserObject.getString("userEmail")
                        muserBirth = fuserObject.getString("userBirth")
                        muserGender = fuserObject.getString("userGender")
                        muserLevel2 = fuserObject.getString("userLevel2")
                        muserLevel = fuserObject.getString("userLevel")
                        mHashTag = fuserObject.getString("HashTag")
                        muserProfileImg = fuserObject.getString("userProfileImg")
                        muserRegisterDate = fuserObject.getString("userRegister_date")

                        muserIdArr.add(muserId)
                        muserPwArr.add(muserPw)
                        muserNameArr.add(muserName)
                        mnuserEmailArr.add(mnuserEmail)
                        muserBirthArr.add(muserBirth)
                        muserGenderArr.add(muserGender)
                        muserLevel2Arr.add(muserLevel2)
                        muserLevelArr.add(muserLevel)
                        mHashTagArr.add(mHashTag)
                        muserProfileImgArr.add(muserProfileImg)
                        muserRegisterDateArr.add(muserRegisterDate)
                    }

                    for (i in 0 until muserIdArr.size) {
                        var fuserProfile = AutoLogin.StringToBitmap(muserProfileImgArr[i], 100, 100)
                        var user = ManagementUser(muserIdArr[i], muserLevelArr[i], fuserProfile, muserRegisterDateArr[i])
                        userManagementlist.add(user)
                        adapter.notifyDataSetChanged()
                    }

                    nsprogress.isVisible = false

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
        val managementUser_Request = ManagementUser_Request(responseListener)
        val queue = Volley.newRequestQueue(this)
        queue.add(managementUser_Request)
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
