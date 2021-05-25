package com.example.ehs

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun replaseFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, tag)
            .addToBackStack(null)
            .commit()
    }

    /* 자바에서 와일드카드로 쓰이는 <?>는 코틀린에선 <*>로 쓰인다 */
    fun startNextActivity(className: Class<*>) {
        var intent = Intent(this, className);
        startActivity(intent)
    }


}