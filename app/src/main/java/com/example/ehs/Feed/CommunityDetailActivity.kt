package com.example.ehs.Feed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_community_detail.*

class CommunityDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_detail)




        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        val toolbar = findViewById(R.id.toolbar_community_detail) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        // 툴바에 뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 여기까지 툴바 설정 완료

        val talkList = arrayListOf(
            CommunityDetails(R.drawable.test_userfeed_b,"SI","방가방가햄토리~"),
            CommunityDetails(R.drawable.test_userfeed,"HN","난 초이하늘이다"),
            CommunityDetails(R.drawable.test_userfeed_c,"EJ","난 신은쟁ㅇ다~"),
                )



        val gridLayoutManager = GridLayoutManager(applicationContext, 1)
        rv_detail.layoutManager = gridLayoutManager
        rv_detail.setHasFixedSize(true)

        rv_detail.adapter = CommunityDetailAdapter(talkList)

        tv_detail_talkNum.text =talkList.size.toString() // 댓글 개수
        Log.d("나와라~",talkList.size.toString())


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

}