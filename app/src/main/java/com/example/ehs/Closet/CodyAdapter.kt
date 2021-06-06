package com.example.ehs.Closet

import android.content.Context
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.R
import kotlinx.android.synthetic.main.recycler_cody.view.*

class CodyAdapter(private val context: Context) : RecyclerView.Adapter<CodyAdapter.ViewHolder>() {

    var datas = mutableListOf<CodyData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(context).inflate(R.layout.recycler_cody,parent,false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = datas[position]
        val listener = View.OnClickListener { it ->
            Toast.makeText(it.context, "Clicked:"+ item, Toast.LENGTH_SHORT).show()
        }

        //리스트사이간격조절
        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 360
        holder.itemView.requestLayout()

        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        fun bind(listener: View.OnClickListener, item: CodyData) {
            view.iv_cody.setImageBitmap(item.cody)
            view.setOnClickListener(listener)
        }
    }


}