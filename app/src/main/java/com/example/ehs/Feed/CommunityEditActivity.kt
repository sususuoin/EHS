package com.example.ehs.Feed

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.ehs.Fashionista.ProfilePlus_Activity
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_community_detail.*
import kotlinx.android.synthetic.main.activity_community_edit.*
import kotlinx.android.synthetic.main.activity_profile_plus_.*
import kotlinx.android.synthetic.main.fragment_community.*

class CommunityEditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_edit)

        btn_editOk.setOnClickListener {
            val returnIntent = Intent()
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        val toolbar = findViewById(R.id.toolbar_community_Edit) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        // 툴바에 뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 여기까지 툴바 설정 완료


    }

    /**
     * 툴바 뒤로가기 버튼 액션 설정
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
    } // 툴바 뒤로가기 액션 설정 끝

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
        }
    }
}