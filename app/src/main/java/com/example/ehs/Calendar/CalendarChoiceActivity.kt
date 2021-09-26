package com.example.ehs.Calendar

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.ehs.R


class CalendarChoiceActivity : AppCompatActivity() {

    private lateinit var calendarclothesFragment: CalendarClothesFragment

    var userId: String? = ""
    val bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_choice)

        calendarclothesFragment = CalendarClothesFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.fragments_frame, calendarclothesFragment)
            .commit() // add는 프레그먼트 추가해주는 것

        val intent = intent
        val selectday = intent.getStringExtra("selectday")

        if (selectday != null) {
            Log.d("여기는 달력 다음다음", selectday)
        }
    }

    fun replaceFragment(fragment: Fragment?) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragments_frame, fragment!!)
        fragmentTransaction.commit()
    }


}