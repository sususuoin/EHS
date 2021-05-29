package com.example.ehs.Home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.R

class CalendarlistAdapter(val context: Context, val calendarList: ArrayList<Calendarlist>) : RecyclerView.Adapter<CalendarlistAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val view = LayoutInflater.from(context).inflate(R.layout.calendar_main, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return calendarList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(calendarList[position], context)
    }



    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val cody = itemView?.findViewById<ImageButton>(R.id.btn_cody)
        val day = itemView?.findViewById<TextView>(R.id.tv_day)
        val yoil = itemView?.findViewById<TextView>(R.id.tv_yoil)


        fun bind (calendar: Calendarlist, context: Context) {
            /* dogPhoto의 setImageResource에 들어갈 이미지의 id를 파일명(String)으로 찾고,
            이미지가 없는 경우 안드로이드 기본 아이콘을 표시한다.*/
            if (calendar.photo != "") {
                val resourceId = context.resources.getIdentifier(calendar.photo, "drawable", context.packageName)
                cody?.setImageResource(resourceId)
            } else {
                cody?.setImageResource(R.drawable.ic_add)
            }

            /* 나머지 TextView와 String 데이터를 연결한다. */
            day?.text = calendar.day
            yoil?.text = calendar.yoil
        }
    }
}