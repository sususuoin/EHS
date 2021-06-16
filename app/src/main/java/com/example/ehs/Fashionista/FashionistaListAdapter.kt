package com.example.ehs.Fashionista

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.Calendar.CalendarActivity
import com.example.ehs.R
import kotlin.coroutines.coroutineContext

class FashionistaListAdapter (private val itemList : List<Fashionista>)
    : RecyclerView.Adapter<FashionistaViewHolder>()  {


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
        layoutParams.height = 200
        holder.itemView.requestLayout()


        holder.apply {
            bind(item)
            itemView.setOnClickListener {

                val intent = Intent(holder.itemView?.context, FashionistaProfile_Activity::class.java)

                ContextCompat.startActivity(holder.itemView.context,intent,null)
            } // item 클릭하면 FashionistaProfile_Activity로 이동
        }

    }







}