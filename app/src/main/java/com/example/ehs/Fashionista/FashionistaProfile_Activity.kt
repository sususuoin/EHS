package com.example.ehs.Fashionista

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ehs.Calendar.CalendarActivity
import com.example.ehs.R
import com.example.ehs.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_fashionista_profile.*

class FashionistaProfile_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fashionista_profile)



        btn_profilePlus.setOnClickListener({

                val intent = Intent(this, ProfilePlus_Activity::class.java)
                startActivity(intent) })



        val feedList = arrayListOf(
            FashionistaUserProfiles(R.drawable.test_userfeed),
            FashionistaUserProfiles(R.drawable.test_userfeed_b),
            FashionistaUserProfiles(R.drawable.test_userfeed_c),
            FashionistaUserProfiles(R.drawable.test_userfeed_d)
        )

        val gridLayoutManager = GridLayoutManager(applicationContext, 3)
        rv_feed.layoutManager = gridLayoutManager
        rv_feed.setHasFixedSize(true)

        rv_feed.adapter = FashionistaProfileAdapter(feedList)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
        }
    }
}