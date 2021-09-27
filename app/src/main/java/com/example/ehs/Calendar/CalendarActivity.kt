package com.example.ehs.Calendar

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_calendar.*
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter


class CalendarActivity : AppCompatActivity(){

    companion object {
        var context_calendar: Context? = null

        var calendarNameArr = ArrayList<String>()
        var calendarYearArr = ArrayList<String>()
        var calendarMonthArr = ArrayList<String>()
        var calendarDayArr = ArrayList<String>()

    }

    private var monthYearText: TextView? = null
    private var calendarRecyclerView: RecyclerView? = null
    private var selectedDate: LocalDate? = null
    var calendar = arrayListOf<Calendar>()
    val calendarAdapter = CalendarAdapter(calendar)
    var todaymonth : String? = null



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        Log.d("캘린더 액티비티", "온크리에이트")
        context_calendar = this

        initWidgets()
        selectedDate = LocalDate.now()
        Log.d("현재날짜", "호롤" + selectedDate)
        setMonthView()
        btn_back.setOnClickListener {
            onBackPressed()
        }
        CalendarSaveCodyActivity.calendarsaveActivity_Dialog = ProgressDialog(this)

        calendarAdapter.notifyDataSetChanged()
        calendarAdapter.setItemClickListener(object : CalendarAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                if (calendar[position].day != "") {
                    var selectday = monthYearFromDate(selectedDate) + " " + calendar[position].day + "일"
                    Toast.makeText(this@CalendarActivity, selectday, Toast.LENGTH_SHORT).show()

                    AutoCalendar.setSelectday(this@CalendarActivity, selectday)

                    val intent = Intent(this@CalendarActivity, CalendarChoiceActivity::class.java)
                    startActivity(intent)

                }

            }
        })
    }

    override fun onResume() {
        super.onResume()
        Log.d("캘린더 액티비티", "새로고침")
        CalendarSaveCodyActivity.calendarsaveActivity_Dialog?.dismiss()
        Log.d("a호리a22", "a호리a")
        calendarNameArr = AutoCalendar.getCalendarName(this)
        calendarYearArr = AutoCalendar.getCalendarYear(this)
        calendarMonthArr = AutoCalendar.getCalendarMonth(this)
        calendarDayArr = AutoCalendar.getCalendarDay(this)

        calendarAdapter.notifyDataSetChanged()


    }

    private fun initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView)
        monthYearText = findViewById(R.id.tv_monthYear)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setMonthView() {
        monthYearText!!.text = monthYearFromDate(selectedDate)
        todaymonth = selectedDate?.monthValue.toString() // 현재월 가져오기
        Log.d("gg", todaymonth!!)
        val daysInMonth = daysInMonthArray(selectedDate)
        Log.d("", daysInMonth.toString())
        for (i in daysInMonth) {
            calendar.apply {
                add(Calendar(day = i, photo = null))
            }
        }

        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 7)
        calendarRecyclerView!!.layoutManager = layoutManager
        calendarRecyclerView!!.adapter = calendarAdapter
        calendarAdapter.notifyDataSetChanged()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun daysInMonthArray(date: LocalDate?): ArrayList<String> {
        val daysInMonthArray = ArrayList<String>()
        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth = selectedDate!!.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value
        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("")
            } else {
                daysInMonthArray.add((i - dayOfWeek).toString())
            }
        }
        return daysInMonthArray
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun monthYearFromDate(date: LocalDate?): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy년 MMMM")
        return date!!.format(formatter)
    }

    // 전월
    @RequiresApi(Build.VERSION_CODES.O)
    fun previousMonthAction(view: View?) {
        calendar.clear()
        selectedDate = selectedDate!!.minusMonths(1)
        setMonthView()
    }

    // 다음월
    @RequiresApi(Build.VERSION_CODES.O)
    fun nextMonthAction(view: View?) {
        calendar.clear()
        selectedDate = selectedDate!!.plusMonths(1)
        setMonthView()
    }



//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onItemClick(position: Int, dayText: String?) {
//        if (dayText != "") {
//            var selectday =
//                "Selected Date" + " " + monthYearFromDate(selectedDate) + " " + dayText + "일"
//            Toast.makeText(this, selectday, Toast.LENGTH_SHORT).show()
//
//
//            val intent = Intent(this@CalendarActivity, CalendarChoiceActivity::class.java)
//            intent.putExtra("selectday", selectday)
//            startActivity(intent)
//
//        }
//    }

}