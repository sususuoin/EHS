package com.example.ehs.Feed

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_community_detail.*
import kotlinx.android.synthetic.main.activity_community_detail_item.view.*
import kotlinx.android.synthetic.main.fragment_community_item.view.*

class CommunityDetailAdapter (private val items: List<CommunityDetails>)
    : RecyclerView.Adapter<CommunityDetailAdapter.ViewHolder>() {

    lateinit var tv_detail_talkNum: TextView




    override fun getItemCount(): Int {
        return items.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_community_detail_item, parent, false)





            return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.talkphoto.setImageResource(items[position].userphoto)
        holder.talkUser.text = items[position].userID
        holder.detailtalk.text = items[position].detailtext



        val item = items[position]

        //리스트사이간격조절
        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 155
        holder.itemView.requestLayout()



    }


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        val talkphoto = itemView.findViewById<ImageView>(R.id.iv_detail_talkPhoto)
        val talkUser = itemView.findViewById<TextView>(R.id.tv_detail_talkUser)
        val detailtalk = itemView.findViewById<TextView>(R.id.tv_detail_talkText)

    }
}
