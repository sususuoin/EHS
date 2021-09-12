package com.example.ehs.Calendar

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.R
import kotlinx.android.synthetic.main.calendar_cell.view.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class CalendarAdapter(private val calendar: ArrayList<Calendar>) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return calendar.size
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
            bind(listener, item)
            itemView.tag = item
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v

        fun bind(listener: View.OnClickListener, item: Calendar) {
            view.cellDayText.text = item.day
            // val day = itemView?.findViewById<TextView>(R.id.cellDayText) // 캘린더 날짜
            // val daycody = itemView?.findViewById<ImageView>(R.id.iv_calendarcody) // 캘린더 코디

            /* dogPhoto의 setImageResource에 들어갈 이미지의 id를 파일명(String)으로 찾고,
                이미지가 없는 경우 안드로이드 기본 아이콘을 표시한다.*/

            var today: LocalDate = LocalDate.now() // 현재 날짜 받아오기
            val formatter = DateTimeFormatter.ofPattern("dd")
            val nowday = today.format(formatter).toString() // 현재날짜에서의 일만 표시

            if (item.day == nowday) { // 현재 날짜라면 해당 날짜 텍스트 컬러 보라색으로 표시
                view.cellDayText.setTextColor(Color.parseColor("#521b93"))
            }


            if (item.day != "") {
                if (item.photo != null) {
                    view.iv_calendarcody.setImageResource(R.drawable.basicprofile) // 포토가 비어있지 않다면
                } else {
                    view.iv_calendarcody.setImageResource(R.drawable.ic_add) // 포토가 비어있다면
                }
            }

            /* 나머지 TextView와 String 데이터를 연결한다. */
            // day?.text = item.day

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