package com.example.ehs.Admin

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_admin_choice.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Managementchoice_Activity : AppCompatActivity() {

    companion object {
        var adminProgressDialog: ProgressDialog? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_choice)

        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        setSupportActionBar(toolbar_profile)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        //뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 툴바 설정 완료

        adminProgressDialog = ProgressDialog(this)
        adminProgressDialog!!.setCanceledOnTouchOutside(false)
        adminProgressDialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btn_usermanagement.setOnClickListener {

            GlobalScope.launch(Dispatchers.Main) {
                launch(Dispatchers.Main) {
                    adminProgressDialog!!.show()
                }
                delay(1000L)
                getUserData()
            }

        }
    }

    fun getUserData() {
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
                    }

                    AutoManagement.setMuserId(this@Managementchoice_Activity, muserIdArr)
                    AutoManagement.setMuserPw(this@Managementchoice_Activity, muserPwArr)
                    AutoManagement.setMuserName(this@Managementchoice_Activity, muserNameArr)
                    AutoManagement.setMuserEmail(this@Managementchoice_Activity, mnuserEmailArr)
                    AutoManagement.setMuserBirth(this@Managementchoice_Activity, muserBirthArr)
                    AutoManagement.setMuserGender(this@Managementchoice_Activity, muserGenderArr)
                    AutoManagement.setMuserLevel2(this@Managementchoice_Activity, muserLevel2Arr)
                    AutoManagement.setMuserLevel(this@Managementchoice_Activity, muserLevelArr)
                    AutoManagement.setMuserTag(this@Managementchoice_Activity, mHashTagArr)
                    AutoManagement.setMuserProfile(this@Managementchoice_Activity, muserProfileImgArr)

                    val intent = Intent(this@Managementchoice_Activity, ManagementUser_Activity::class.java)
                    startActivity(intent)


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