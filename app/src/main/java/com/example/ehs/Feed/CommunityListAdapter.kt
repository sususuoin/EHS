package com.example.ehs.Feed

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.R
import kotlinx.android.synthetic.main.fragment_community_item.view.*

class CommunityListAdapter(private val items: List<Community>)
    : RecyclerView.Adapter<CommunityListAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v


        fun bind(listener: View.OnClickListener, item: Community) {

            view.tv_C_userID.setText(item.CummunityUserID)
            view.tv_commu_text.setText(item.Cummunitytext)
            view.setOnClickListener(listener)
            view.btn_C_nolike.setOnClickListener { nolike(it) }
            view.btn_C_like.setOnClickListener { like(it) }
        }

        private fun like(it: View?) {
            view.findViewById<Button>(R.id.btn_C_like).setVisibility(View.GONE);
            view.findViewById<Button>(R.id.btn_C_nolike).setVisibility(View.VISIBLE);
            Toast.makeText(it?.context, "좋아요를 취소하셨습니다.", Toast.LENGTH_SHORT).show()

        } // 좋아요

        private fun nolike(it: View?) {
            view.findViewById<Button>(R.id.btn_C_like).setVisibility(View.VISIBLE);
            view.findViewById<Button>(R.id.btn_C_nolike).setVisibility(View.GONE);
            Toast.makeText(it?.context, "좋아요를 누르셨습니다.", Toast.LENGTH_SHORT).show()
        } //좋아요 취소
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_community_item, parent, false)

        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener { it ->
            Toast.makeText(it.context, "Clicked", Toast.LENGTH_SHORT).show()
        }

        //리스트사이간격조절
        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 220
        holder.itemView.requestLayout()


        holder.apply {
            bind(listener, item)
//            itemView.tag = item
            itemView.setOnClickListener {

                val intent = Intent(holder.itemView?.context, CommunityDetailActivity::class.java)
//                intent.putExtra("fashionistaId", fashionistaId)
                ContextCompat.startActivity(holder.itemView.context,intent,null)
                Toast.makeText(it.context, "Clicked", Toast.LENGTH_SHORT).show()
            } // item 클릭하면 FashionistaProfile_Activity로 이동


        }
    }



}

