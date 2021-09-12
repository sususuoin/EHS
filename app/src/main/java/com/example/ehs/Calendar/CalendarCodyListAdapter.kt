package com.example.ehs.Closet

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.R
import kotlinx.android.synthetic.main.recycler_cody.view.*

class CalendarCodyListAdapter(private val items: List<Cody>) : RecyclerView.Adapter<CalendarCodyListAdapter.ViewHolder>() {

    var codyclicked = false;

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_cody, parent, false)

        return ViewHolder(inflatedView)
    }


    // 아이템 클릭 리스너
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener { it ->

            //--------------------------------------------------------------

            if (!codyclicked) {
                //clothesclicked false일떄 실행
                Log.d("aa22", codyclicked.toString())
                Toast.makeText(it?.context, "Clicked" + item, Toast.LENGTH_SHORT).show()
                holder.itemView.setBackgroundResource(R.drawable.cody_background)
                codyclicked = true;
            } else {
                Log.d("aa33", codyclicked.toString())
                Toast.makeText(it?.context, "Cancel" + item, Toast.LENGTH_SHORT).show()
                holder.itemView.setBackgroundResource(R.drawable.button_background)
                holder.itemView.setBackgroundColor(Color.parseColor("#E7E7E7"))
                codyclicked = false;
            }
            //==========================================================================



//            Toast.makeText(it.context, "Clicked" + item, Toast.LENGTH_SHORT).show()
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
        fun bind(listener: View.OnClickListener, item: Cody) {
            view.cody.setImageBitmap(item.cody)
            view.setOnClickListener(listener)
        }
    }
}