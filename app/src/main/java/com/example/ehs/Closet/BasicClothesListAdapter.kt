package com.example.ehs.Closet

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.R
import kotlinx.android.synthetic.main.basic_clothes.view.*

class BasicClothesListAdapter
    (val items: List<BasicClothes>) : RecyclerView.Adapter<BasicClothesListAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.basic_clothes, parent, false)


        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
//        holder.basic.setImageResource(items[position].basicCothes)

        val listener = View.OnClickListener { it ->
            Toast.makeText(it.context, "기본템" + item, Toast.LENGTH_SHORT).show()
            val intent = Intent(holder.itemView?.context, BasicClothesDetailActivity::class.java)
            intent.putExtra("a", item.basicCothes.toString())
            Log.d("넘어가", intent.putExtra("이미지", item.basicCothes).toString())
            ContextCompat.startActivity(holder.itemView.context, intent, null)

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


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var view: View = itemView

        fun bind(listener: View.OnClickListener, item: BasicClothes) {
//            view.iv_basic.setImageBitmap(item.basicCothes)
            item.basicCothes?.let { view.iv_basic.setImageResource(it) }

//            val basic = itemView.findViewById<ImageView>(R.id.iv_basic)
            view.setOnClickListener(listener)
        }

    }

}