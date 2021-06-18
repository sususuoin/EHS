package com.example.ehs.Feed

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.R
import kotlinx.android.synthetic.main.fragment_community_item.view.*

class CommunityListAdapter(private val items: List<Community>)
    : RecyclerView.Adapter<CommunityListAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
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
            } // item 클릭하면 FashionistaProfile_Activity로 이동


        }
    }


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v


        fun bind(listener: View.OnClickListener, item: Community) {

            view.tv_C_userID.setText(item.CummunityUserID)
            view.tv_commu_text.setText(item.Cummunitytext)
            view.setOnClickListener(listener)
        }
    }
}

