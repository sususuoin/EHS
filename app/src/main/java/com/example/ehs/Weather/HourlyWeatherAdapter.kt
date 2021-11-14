package com.example.ehs.Weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.R

class HourlyWeatherAdapter(val itemList: List<HourlyWeathers>)
    :RecyclerView.Adapter<HourlyWeatherAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            HourlyWeatherAdapter.CustomViewHolder {
        // 연결될 화면
        val view = LayoutInflater.from(parent.context).
        inflate(R.layout.activity_weather_h_item, parent, false)
        // view는 fashionista_user_item을 Adapter에 붙여줌


        return CustomViewHolder(view).apply {


        }


    }
    override fun getItemCount(): Int {
        //item들의 총 개수
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.iv_hweather.setImageResource(itemList[position].hicon)
        holder.htemp.text = itemList[position].htemp
        holder.hdt.text = itemList[position].hdt



        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 250
        layoutParams.width = 180
        holder.itemView.requestLayout()
    }




    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv_hweather = itemView.findViewById<ImageView>(R.id.iv_hweather)
        val htemp = itemView.findViewById<TextView>(R.id.tv_htemp)
        val hdt = itemView.findViewById<TextView>(R.id.tv_hdt)



    }

}