package com.example.ehs.Fashionista

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.R

class FashionistaListAdapter (private val itemList : List<Fashionista>) : RecyclerView.Adapter<FashionistaViewHolder>()  {

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FashionistaViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fashionista, parent, false)
        return FashionistaViewHolder(inflatedView);
    }

    override fun onBindViewHolder(holder: FashionistaViewHolder, position: Int) {
        val item = itemList[position]

        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 250
        holder.itemView.requestLayout()

        holder.apply {
            bind(item)
        }

    }





}