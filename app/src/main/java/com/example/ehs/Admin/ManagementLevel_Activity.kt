package com.example.ehs.Admin

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_management_level.*
import kotlinx.android.synthetic.main.fragment_closet.*
import kotlinx.android.synthetic.main.activity_management_level.nsprogress
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class ManagementLevel_Activity : AppCompatActivity() {

    val items = arrayOf("전체","대기중","LV1","LV2","LV3","LV4","LV5")

    val levelManagementlist = mutableListOf<ManagementLevel>()
    val levelManagementlist2 = mutableListOf<ManagementLevel>()

    val adapter = ManagementLevelListAdapter(levelManagementlist)

    var mluserProfileImgArr = ArrayList<String>()
    var mluserIdArr = ArrayList<String>()
    var mluserLevel2Arr = ArrayList<String>()
    var mluserLevelArr = ArrayList<String>()
    var mllike_cntArr = ArrayList<String>()
    var mlnolike_cntArr = ArrayList<String>()

    companion object {
        var levelModifyActivity_Dialog : ProgressDialog? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_management_level)
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

        levelModifyActivity_Dialog = ProgressDialog(this)
        levelModifyActivity_Dialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        levelModifyActivity_Dialog?.setMessage("업로드 중입니다.")
        levelModifyActivity_Dialog?.setCanceledOnTouchOutside(false)

        rv_levelmanagement.adapter = adapter
        adapter.notifyDataSetChanged()

        val myAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        sp_level.adapter = myAdapter
        sp_level.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
                levelManagementlist.clear()
                //아이템이 클릭 되면 맨 위부터 position 0번부터 순서대로 동작하게 됩니다.
                when (position) {
                    0 -> {
                        levelManagementlist.addAll(levelManagementlist2)
                        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
                        adapter.notifyDataSetChanged()
                    }
                    1 -> {
                        filter("대기중")
                    }
                    2 -> {
                        filter("LV1")
                    }
                    3 -> {
                        filter("LV2")
                    }
                    4 -> {
                        filter("LV3")
                    }
                    5 -> {
                        filter("LV4")
                    }
                    6 -> {
                        filter("LV5")
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // 문자 입력이 없을때는 모든 데이터를 보여준다.
                levelManagementlist.addAll(levelManagementlist2)
                adapter.notifyDataSetChanged()
            }

        }

    }

    override fun onResume() {
        super.onResume()

        nsprogress.isVisible = true

        var mluserProfileImg: String
        var mluserId: String
        var mluserLevel2: String
        var mluserLevel: String
        var mllike_cnt: String
        var mlnolike_cnt: String

        levelManagementlist.clear()
        mluserProfileImgArr.clear()
        mluserIdArr.clear()
        mluserLevel2Arr.clear()
        mluserLevelArr.clear()
        mllike_cntArr.clear()
        mlnolike_cntArr.clear()

        Log.d("level관라, ", "dlflda")

        val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
            override fun onResponse(response: String?) {
                try {
                    Log.d("level관라, ", "dlflda")
                    var jsonObject = JSONObject(response)

                    val arr: JSONArray = jsonObject.getJSONArray("response")

                    for (i in 0 until arr.length()) {
                        val fuserObject = arr.getJSONObject(i)

                        mluserProfileImg = fuserObject.getString("userProfileImg")
                        mluserId = fuserObject.getString("userId")
                        mluserLevel2 = fuserObject.getString("userLevel2")
                        mluserLevel = fuserObject.getString("userLevel")
                        mllike_cnt = fuserObject.getString("like_cnt")
                        mlnolike_cnt = fuserObject.getString("nolike_cnt")

                        mluserProfileImgArr.add(mluserProfileImg)
                        mluserIdArr.add(mluserId)
                        mluserLevel2Arr.add(mluserLevel2)
                        mluserLevelArr.add(mluserLevel)
                        mllike_cntArr.add(mllike_cnt)
                        mlnolike_cntArr.add(mlnolike_cnt)

                    }

                    for (i in 0 until mluserIdArr.size) {
                        var fuserProfile = AutoLogin.StringToBitmap(mluserProfileImgArr[i], 100, 100)
                        var level = ManagementLevel(mluserIdArr[i], mluserLevel2Arr[i], mluserLevelArr[i], fuserProfile, mllike_cntArr[i], mlnolike_cntArr[i])
                        levelManagementlist.add(level)
                        adapter.notifyDataSetChanged()
                    }

                    nsprogress.isVisible = false
                    levelManagementlist2.addAll(levelManagementlist)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
        val managementLevel_Request = ManagementLevel_Request(responseListener)
        val queue = Volley.newRequestQueue(this)
        queue.add(managementLevel_Request)

    }

    fun filter(level: String) {
        // 리스트의 모든 데이터를 검색한다.
        for (i in 0 until levelManagementlist2.size) {
            // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true 를 반환한다.
            if (levelManagementlist2[i].level2 == level) {
                // 검색된 데이터를 리스트에 추가한다.
                levelManagementlist.add(levelManagementlist2[i])
            } else if (levelManagementlist2[i].level == level) {
                // 검색된 데이터를 리스트에 추가한다.
                levelManagementlist.add(levelManagementlist2[i])
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
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
