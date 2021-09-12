package com.example.ehs.Feed

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.R
import kotlinx.android.synthetic.main.fragment_feed_item.view.*


class FeedsListAdapter(private val items: List<Feeds>)
    : RecyclerView.Adapter<FeedsListAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v


        fun bind(listener: View.OnClickListener, item: Feeds) {

            view.tv_userID.setText(item.userID)
            view.tv_styletag.setText(item.styletag)
            view.iv_feedphoto.setImageBitmap(item.feedImg)
            view.btn_nolike.setOnClickListener { nolike(it) }
            view.btn_like.setOnClickListener { like(it) }
            view.setOnClickListener(listener)

        }

        private fun like(it: View?) {
            view.findViewById<Button>(R.id.btn_like).setVisibility(View.GONE);
            view.findViewById<Button>(R.id.btn_nolike).setVisibility(View.VISIBLE);
            Toast.makeText(it?.context, "좋아요를 취소하셨습니다.", Toast.LENGTH_SHORT).show()
        } // 좋아요

        private fun nolike(it: View?) {
            view.findViewById<Button>(R.id.btn_like).setVisibility(View.VISIBLE);
            view.findViewById<Button>(R.id.btn_nolike).setVisibility(View.GONE);
            Toast.makeText(it?.context, "좋아요를 누르셨습니다.", Toast.LENGTH_SHORT).show()
        } //좋아요 취소

    }

    override fun getItemCount(): Int {
        return items.size
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_feed_item, parent, false)

        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener { it ->
            Toast.makeText(it.context, "Clicked", Toast.LENGTH_SHORT).show()
        }

        //리스트사이간격조절
        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 790
        layoutParams.width = 500
        holder.itemView.requestLayout()


        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }
    }


}