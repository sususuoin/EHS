package com.example.ehs.Feed

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.ehs.Login.AutoLogin
import com.example.ehs.MainActivity
import com.example.ehs.R
import kotlinx.android.synthetic.main.fragment_feed_item.view.*
import org.json.JSONException
import org.json.JSONObject


class FeedsListAdapter2(private val itemList: List<Feed>)
    : RecyclerView.Adapter<FeedsListAdapter2.ViewHolder>() {

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_feed_item, parent, false)

        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        //리스트사이간격조절
        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 790
        layoutParams.width = 500
        holder.itemView.requestLayout()


        holder.apply {
            bind(item)
//            itemView.tag = item

        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun bind(item: Feed) {
            view.iv_userphoto.setImageBitmap(item.userprofileImg)
            view.tv_userID.setText(item.userID)
            view.tv_styletag.setText(item.styletag)
            view.iv_feedphoto.setImageBitmap(item.feedImg)

            view.tv_feedlikecount.setText(item.feedLikeCount)
            view.tv_feedunlikecount.setText(item.feedUnlikeCount)


        }

    }




}