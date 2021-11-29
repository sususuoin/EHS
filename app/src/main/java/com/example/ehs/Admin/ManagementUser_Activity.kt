package com.example.ehs.Admin

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
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

    val items = arrayOf("전체","전문가","일반인")



    val userManagementlist = mutableListOf<ManagementUser>()
    val userManagementlist2 = mutableListOf<ManagementUser>()

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
                    val intent = Intent(this@ManagementUser_Activity,
                        ManagementUserModify_Activity::class.java)
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
                    userManagementlist[position].profile!!.compress(Bitmap.CompressFormat.PNG,
                        100,
                        stream)
                    val byteArray = stream.toByteArray()
                    intent.putExtra("muserProfile", byteArray)
                    Log.d("모지모지,", "ahwlahwl")
                    startActivity(intent)

                }

            }
        })

        rv_usermanagement.adapter = adapter
        adapter.notifyDataSetChanged()


        et_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                val text: String = et_search.text.toString()
                search(text)
            }
        })

        val myAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        sp_grade.adapter = myAdapter
        sp_grade.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
                userManagementlist.clear()
                //아이템이 클릭 되면 맨 위부터 position 0번부터 순서대로 동작하게 됩니다.
                when (position) {
                    0 -> {
                        userManagementlist.addAll(userManagementlist2)
                        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
                        adapter.notifyDataSetChanged()
                    }
                    1 -> {
                        filter("전문가")
                    }
                    2 -> {
                        filter("일반인")
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // 문자 입력이 없을때는 모든 데이터를 보여준다.
                userManagementlist.addAll(userManagementlist2)
                adapter.notifyDataSetChanged()
            }

        }
    }

    fun search(charText: String) {
        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        userManagementlist.clear()
        if (charText.isEmpty()) {
            userManagementlist.addAll(userManagementlist2)
        } else {
            // 리스트의 모든 데이터를 검색한다.
            for (i in 0 until userManagementlist2.size) {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (userManagementlist2[i].name.toLowerCase().contains(charText)) {
                    // 검색된 데이터를 리스트에 추가한다.
                    userManagementlist.add(userManagementlist2[i])
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged()
    }

    fun filter(level: String) {
            // 리스트의 모든 데이터를 검색한다.
            for (i in 0 until userManagementlist2.size) {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true 를 반환한다.
                if (userManagementlist2[i].level == level) {
                    // 검색된 데이터를 리스트에 추가한다.
                    userManagementlist.add(userManagementlist2[i])
                }
            }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        var muserId: String
        var muserPw: String
        var muserName: String
        var mnuserEmail: String
        var muserBirth: String
        var muserGender: String
        var muserLevel2: String
        var muserLevel: String
        var mHashTag: String
        var muserProfileImg: String
        var muserRegisterDate: String

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
                        var user = ManagementUser(muserIdArr[i],
                            muserLevelArr[i],
                            fuserProfile,
                            muserRegisterDateArr[i])
                        userManagementlist.add(user)
                        adapter.notifyDataSetChanged()
                    }

                    nsprogress.isVisible = false
                    userManagementlist2.addAll(userManagementlist)


                    Log.d("유저매니지 리스트1111", userManagementlist[2].name)
                    Log.d("유저매니지 리스트1111", userManagementlist2[2].name)
                    for (i in 0 until userManagementlist.size) {
                        Log.d("유저매니지 리스트1111", userManagementlist[i].name)
                        Log.d("유저매니지 리스트2222", userManagementlist2[i].name)
                    }

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
