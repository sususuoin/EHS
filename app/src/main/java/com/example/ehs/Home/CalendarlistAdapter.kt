package com.example.ehs.Home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.R
import kotlinx.android.synthetic.main.calendar_main.view.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import kotlin.collections.ArrayList

class CalendarlistAdapter(
    val context: Context,
    private val calendarList: ArrayList<Calendarlist>)

    : RecyclerView.Adapter<CalendarlistAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val view = LayoutInflater.from(context).inflate(R.layout.calendar_main, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return calendarList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(calendarList[position], context)
    }



    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        fun bind (calendar: Calendarlist, context: Context) {
            /* dogPhoto의 setImageResource에 들어갈 이미지의 id를 파일명(String)으로 찾고,
            이미지가 없는 경우 안드로이드 기본 아이콘을 표시한다.*/

            var today : LocalDate = LocalDate.now() // 현재 날짜 받아오기
            val formatter2 = DateTimeFormatter.ofPattern("MM")
            val formatter = DateTimeFormatter.ofPattern("dd")

            var nowmonth = today.format(formatter2).toString() // 현재날짜에서의 월만 표시
            var nowday = today.format(formatter).toString() // 현재날짜에서의 일만 표시

            if(nowmonth[0].toString() == "0") {
                nowmonth =nowmonth.replace("0", "")
            }
            if(nowday[0].toString() == "0") {
                nowday =nowday.replace("0", "")
            }
            if(calendar.day[0].toString() == "0") {
                calendar.day =calendar.day.replace("0", "")
            }

            if(calendar.day == nowday) { // 현재 날짜라면 해당 날짜 텍스트 컬러 보라색으로 표시
                itemView.tv_day!!.setTextColor(ContextCompat.getColor(context ,R.color.ourcolor))
                itemView.tv_yoil!!.setTextColor(ContextCompat.getColor(context ,R.color.ourcolor))
            }

            var a_bitmap : Bitmap? = null
            for (i in 0 until HomeFragment.calendarNameArr.size) {
                val uThread: Thread = object : Thread() {
                    override fun run() {
                        try {
//                            Log.d("달력", CalendarActivity.calendarNameArr[i])
                            val url = URL("http://13.125.7.2/img/calendar/" + HomeFragment.calendarNameArr[i])

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

                    //지정한 날짜에 이미지 넣기

                    if(nowmonth == HomeFragment.calendarMonthArr[i] &&  calendar.day == HomeFragment.calendarDayArr[i]) {
                        itemView.iv_homecalendarcody!!.setImageBitmap(a_bitmap)
                    }


                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }


            /* 나머지 TextView와 String 데이터를 연결한다. */
            itemView.tv_month.text = calendar.month
            itemView.tv_day.text = calendar.day
            itemView.tv_yoil.text = calendar.yoil
        }
    }
}