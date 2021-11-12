package com.example.ehs.Calendar

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.Home.HomeFragment
import com.example.ehs.Home.HomeFragment.Companion.customProgressDialog
import com.example.ehs.MainActivity
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.calendar_cell.view.*
import kotlinx.android.synthetic.main.fragment_closet.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import kotlin.properties.Delegates


class CalendarActivity : AppCompatActivity(){

    companion object {
        var context_calendar: Context? = null

        var calendarNameArr = ArrayList<String>()
        var calendarYearArr = ArrayList<String>()
        var calendarMonthArr = ArrayList<String>()
        var calendarDayArr = ArrayList<String>()

        var todaymonth : String? = null
        var daysInMonth = ArrayList<String>()

    }

    private var selectedDate: LocalDate? = null
    var calendar = arrayListOf<Calendar>()
    val calendarAdapter = CalendarAdapter(calendar)

    var customProgressDialog2: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        Log.d("캘린더 액티비티", "온크리에이트")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
            this.window.statusBarColor = ContextCompat.getColor(this,R.color.white)
        }

        context_calendar = this

        selectedDate = LocalDate.now()
        Log.d("현재날짜", "호롤" + selectedDate)

        btn_back.setOnClickListener {
            onBackPressed()
        }
        CalendarSaveCodyActivity.calendarsaveActivity_Dialog = ProgressDialog(this)

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


        customProgressDialog2 = ProgressDialog(this)
        customProgressDialog2!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //전달클릭
        ib_premonth.setOnClickListener{
            calendar.clear()

            Log.d("pre", selectedDate.toString())
            Log.d("pre", todaymonth!!)
            var premonth = 0
            if(todaymonth == "1") {
                premonth == 12
            } else {
                premonth = todaymonth!!.toInt()-1
            }
            Log.d("pre", premonth.toString())

            selectedDate = selectedDate!!.minusMonths(1)
            (MainActivity.mContext as MainActivity).CalendarImg(premonth.toString())

            GlobalScope.launch(Dispatchers.Main) {
                launch(Dispatchers.Main) {
                    customProgressDialog2!!.show()
                }
                delay(1000L)

                setMonthView()
            }
        }

        //다음달클릭
        ib_nextmonth.setOnClickListener{
            calendar.clear()

            Log.d("next", selectedDate.toString())
            Log.d("next", todaymonth!!)
            var premonth = 0
            if(todaymonth == "12") {
                premonth == 1
            } else {
                premonth = todaymonth!!.toInt()+1
            }
            selectedDate = selectedDate!!.plusMonths(1)
            Log.d("next", premonth.toString())
            (MainActivity.mContext as MainActivity).CalendarImg(premonth.toString())

            GlobalScope.launch(Dispatchers.Main) {
                launch(Dispatchers.Main) {
                    customProgressDialog2!!.show()
                }
                delay(1000L)

                setMonthView()
            }
        }

        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 7)
        calendarRecyclerView!!.layoutManager = layoutManager
        calendarRecyclerView!!.adapter = calendarAdapter
        calendarAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        calendar.clear()
        Log.d("캘린더 액티비티", "새로고침")

        setMonthView()
        calendarAdapter.notifyDataSetChanged()

    }

    private fun setMonthView() {
        calendarNameArr = AutoCalendar.getCalendarName(this)
        calendarYearArr = AutoCalendar.getCalendarYear(this)
        calendarMonthArr = AutoCalendar.getCalendarMonth(this)
        calendarDayArr = AutoCalendar.getCalendarDay(this)
        Log.d("ghghghazz22", calendarDayArr.toString())

        CalendarSaveCodyActivity.calendarsaveActivity_Dialog?.dismiss()
        customProgressDialog?.dismiss()
        customProgressDialog2?.dismiss()

        tv_monthYear!!.text = monthYearFromDate(selectedDate)
        todaymonth = selectedDate?.monthValue.toString() // 현재월 가져오기
        Log.d("gg1", todaymonth!!)
        daysInMonth = daysInMonthArray(selectedDate)
        Log.d("gg2", daysInMonth.toString())

        for (i in daysInMonth) {
            calendar.apply {
                if(calendarDayArr.contains(i)) {
                    var a_bitmap : Bitmap? = null

                    val uThread: Thread = object : Thread() {
                        override fun run() {
                            try {
                                val url = URL("http://13.125.7.2/img/calendar/" + calendarNameArr[calendarDayArr.indexOf(i)])

                                val conn: HttpURLConnection = url.openConnection() as HttpURLConnection

                                conn.setDoInput(true)
                                conn.connect()
                                val iss: InputStream = conn.getInputStream()
                                a_bitmap = BitmapFactory.decodeStream(iss)

                            } catch (e: MalformedURLException) {
                                e.printStackTrace()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }
                    uThread.start() // 작업 Thread 실행

                    try {

                        uThread.join()

                        add(Calendar(day = i, photo = a_bitmap))

                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                } else {
                    add(Calendar(day = i, photo = null))
                }
            }
        }
        calendarAdapter.notifyDataSetChanged()
    }

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

    private fun monthYearFromDate(date: LocalDate?): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy년 MMMM")
        return date!!.format(formatter)
    }



}