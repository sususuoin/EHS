package com.example.ehs.Fashionista

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.R
import kotlinx.android.synthetic.main.pro_recommend_cody.view.*


class ProRecommendyListAdapter(private val itemList: List<ProRecommend>)
    : RecyclerView.Adapter<ProRecommendyListAdapter.ViewHolder>()  {

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.pro_recommend_cody, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        val layoutParams = holder.itemView.layoutParams

        holder.itemView.requestLayout()


        
        holder.apply {
            bind(item)
            itemView.setOnClickListener {
            }

        }

    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var view : View = v

        fun bind(item: ProRecommend) {
            view.iv_proRecommendcody.setImageBitmap(item.procody)
        }
    }



}