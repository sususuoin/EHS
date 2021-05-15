package com.example.ehs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    val TAG: String = "메인화면"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainIntent = Intent(this, LoginActivity::class.java)


        tv_title.setOnClickListener {
            Log.d("메인화면", "화면 잘나옴")
            startActivity(mainIntent)
        }

        val intent = getIntent()
        val userId = intent.getStringExtra("userId")
        val userPw = intent.getStringExtra("userPw")
        val userName = intent.getStringExtra("userName")
        val userEmail = intent.getStringExtra("userEmail")
        val userBirth = intent.getStringExtra("userBirth")
        val userGender = intent.getStringExtra("userGender")
        val userLevel = intent.getStringExtra("userLevel")

        tv_name2.setText(userId)
        tv_id2.setText(userPw)
        tv_pw2.setText(userName)
        tv_name.setText(userEmail)
        tv_id.setText(userBirth)
        tv_pw.setText(userGender)
        tv_level.setText(userLevel)

    }
}