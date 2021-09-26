package com.example.ehs.Closet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.R
import kotlinx.android.synthetic.main.cody.view.*

class CalendarCodyListAdapter(private val items: List<Cody>) : RecyclerView.Adapter<CalendarCodyListAdapter.ViewHolder>() {

    var codyclicked = false;

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cody, parent, false)

        return ViewHolder(inflatedView)
    }


    // 아이템 클릭 리스너
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener { it ->
        }

        //리스트사이간격조절
        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 505
        layoutParams.width = 505
        holder.itemView.requestLayout()


        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }
    }


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        fun bind(listener: View.OnClickListener, item: Cody) {
            view.cody.setImageBitmap(item.cody)
            view.setOnClickListener(listener)
        }
    }
}