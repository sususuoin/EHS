package com.example.ehs.Calendar

import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.Closet.Clothes
import com.example.ehs.R
import kotlinx.android.synthetic.main.clothes.view.*


class CalendarClothesListAdapter(private val items: MutableList<Clothes>)
    : RecyclerView.Adapter<CalendarClothesListAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.clothes, parent, false)
        return ViewHolder(inflatedView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // (1) 리스트 내 항목 클릭 시 onClick() 호출
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position, holder)
        }
        val item = items[position]
        val listener = View.OnClickListener { it ->
        }

        //리스트사이간격조절
        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 325
        holder.itemView.requestLayout()


        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }

    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v

        fun bind(listener: View.OnClickListener, item: Clothes) {
            view.clothes.setImageBitmap(item.clothes)
            // view.setOnClickListener(listener)
        }
    }

    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int, holder: ViewHolder)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener
}
