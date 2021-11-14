package com.example.ehs.Calendar

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.Calendar.CalendarActivity.Companion.todaymonth
import com.example.ehs.R
import kotlinx.android.synthetic.main.calendar_cell.view.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*


class CalendarAdapter(private val calendar: ArrayList<Calendar>) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return calendar.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calendar_cell, parent, false)
        val layoutParams = view.layoutParams
        layoutParams.height = (parent.height * 0.14).toInt()
        return ViewHolder(view)


    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        var item = calendar[position]
        val listener = View.OnClickListener { it ->
            Toast.makeText(holder.itemView.context,
                "호로로롤 ${calendar[position].day}",
                Toast.LENGTH_SHORT).show()
        }

        holder.apply {
            bind(item)
            itemView.tag = item
        }
    }


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        fun bind(item: Calendar) {
            view.cellDayText.text = item.day
            view.iv_calendarcody.setImageBitmap(item.photo)

            var today: LocalDate = LocalDate.now() // 현재 날짜 받아오기
            val ddformatter = DateTimeFormatter.ofPattern("dd")

            var nowday = today.format(ddformatter).toString() // 현재날짜에서의 일만 표시
            val nowmonth = today.monthValue.toString()

            if(nowday[0].toString() == "0") {
                nowday =nowday.replace("0", "")

            }
            if (item.day == nowday && todaymonth.toString() == nowmonth ) { // 현재 일이고 뿌려지는 월과 오늘 월이 같다면
                view.cellDayText.setTextColor(Color.parseColor("#521b93")) // 날짜 보라색으로 표시
            }

        }

    }

    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener: OnItemClickListener

}