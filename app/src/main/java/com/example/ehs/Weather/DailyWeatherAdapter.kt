package com.example.ehs.Weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.R

class DailyWeatherAdapter(val itemList: List<DailyWeathers>)
    :RecyclerView.Adapter<DailyWeatherAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            DailyWeatherAdapter.CustomViewHolder {
        // 연결될 화면
        val view = LayoutInflater.from(parent.context).
        inflate(R.layout.activity_weather_d_item, parent, false)
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
        holder.iv_dweather.setImageResource(itemList[position].dicon)
        holder.dmintemp.text = itemList[position].dmintemp
        holder.dmaxtemp.text = itemList[position].dmaxtemp
        holder.ddt.text = itemList[position].ddt



        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 120
        holder.itemView.requestLayout()
    }




    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv_dweather = itemView.findViewById<ImageView>(R.id.iv_dweather)
        val dmintemp = itemView.findViewById<TextView>(R.id.tv_d_mintemp)
        val dmaxtemp = itemView.findViewById<TextView>(R.id.tv_d_maxtemp)
        val ddt = itemView.findViewById<TextView>(R.id.tv_ddt)

    }

}