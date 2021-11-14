package com.example.ehs.Weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_weather_item.view.*
import kotlinx.android.synthetic.main.fragment_feed_item.view.*

class WeatherAdapter(val itemList: List<Weathers>)
    :RecyclerView.Adapter<WeatherAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            WeatherAdapter.CustomViewHolder {
        // 연결될 화면
        val view = LayoutInflater.from(parent.context).
        inflate(R.layout.activity_weather_item, parent, false)
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
        holder.imgcolthes.setImageResource(itemList[position].clothes)
        holder.clothesname.text = itemList[position].clothesname


        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 400
        layoutParams.width = 300
        holder.itemView.requestLayout()
    }




    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgcolthes = itemView.findViewById<ImageView>(R.id.iv_clothes)
        val clothesname = itemView.findViewById<TextView>(R.id.tv_clothesname)


    }

}