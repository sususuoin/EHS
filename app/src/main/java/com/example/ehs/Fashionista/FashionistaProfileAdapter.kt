package com.example.ehs.Fashionista

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.R
import kotlinx.android.synthetic.main.fashionista_profile_item.view.*

class FashionistaProfileAdapter (val items : List<FashionistaUserProfiles>) :RecyclerView.Adapter<FashionistaProfileAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fashionista_profile_item, parent, false)


        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        val listener = View.OnClickListener { it ->
            Toast.makeText(it.context, "피드 : ${item.plusImg}", Toast.LENGTH_SHORT).show()
        }

        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 320

        holder.itemView.requestLayout()
1
        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var view: View = itemView

        fun bind(listener: View.OnClickListener, item: FashionistaUserProfiles) {
            view.iv_plusImg.setImageBitmap(item.plusImg)
            view.setOnClickListener(listener)
        }

    }

}