package com.example.ehs.Fashionista

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.example.ehs.Login.AutoLogin
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_pro_recommend.*

class ProRecommendActivity : AppCompatActivity() {

    var userId : String= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pro_recommend)
        userId = AutoLogin.getUserId(this@ProRecommendActivity)
        tv_userid.text = userId

        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        val toolbar = findViewById(R.id.toolbar_prorecommend) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        //뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 툴바 설정 완료
    }

    /**
     * 툴바 뒤로가기 버튼 기능 구현
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