package com.example.ehs.Calendar

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.R
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class CalendarViewHolder internal constructor(itemView: View, onItemListener: CalendarAdapter.OnItemListener) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {
    val dayOfMonth: TextView

    private val onItemListener: CalendarAdapter.OnItemListener
    override fun onClick(view: View) {
        onItemListener.onItemClick(adapterPosition, dayOfMonth.text as String)
    }

    init {
        dayOfMonth = itemView.findViewById(R.id.cellDayText)
        this.onItemListener = onItemListener
        itemView.setOnClickListener(this)
    }
}