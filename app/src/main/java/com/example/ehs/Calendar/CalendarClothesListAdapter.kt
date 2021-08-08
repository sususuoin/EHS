package com.example.ehs.Calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.Closet.Clothes
import com.example.ehs.R
import kotlinx.android.synthetic.main.clothes.view.*


class CalendarClothesListAdapter(private val items: MutableList<Clothes>)
    : RecyclerView.Adapter<CalendarClothesListAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.clothes, parent, false)

        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener { it ->
            Toast.makeText(it.context, "Clicked" + item, Toast.LENGTH_SHORT).show()
        }

        //리스트사이간격조절
        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 350
        layoutParams.width = 350
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
            view.setOnClickListener(listener)

        }
    }

//    // (2) 리스너 인터페이스
//    interface OnItemClickListener {
//        fun onClick(v: View, position: Int)
//    }
//    // (3) 외부에서 클릭 시 이벤트 설정
//    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
//        this.itemClickListener = onItemClickListener
//    }
//    // (4) setItemClickListener로 설정한 함수 실행
//    private lateinit var itemClickListener : OnItemClickListener
}
