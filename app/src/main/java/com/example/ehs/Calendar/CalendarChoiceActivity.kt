package com.example.ehs.Calendar

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.ehs.R
import kotlinx.android.synthetic.main.fragment_closet.*


class CalendarChoiceActivity : AppCompatActivity() {

    private lateinit var calendarclothesFragment: CalendarClothesFragment

    var userId: String? = ""
    val bundle = Bundle()

    companion object {
        var calendarChoiceContext: Context? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_choice)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
            this.window.statusBarColor = ContextCompat.getColor(this,R.color.white)
        }

        calendarChoiceContext = this

        calendarclothesFragment = CalendarClothesFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.fragments_frame_calendar, calendarclothesFragment)
            .commit() // add는 프레그먼트 추가해주는 것

    }

    fun replaceFragment(fragment: Fragment?) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragments_frame_calendar, fragment!!)
        fragmentTransaction.commit()
    }


}