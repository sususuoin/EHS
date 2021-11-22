package com.example.ehs.Admin


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.R
import kotlinx.android.synthetic.main.management_feed.view.*

class ManagementFeedListAdapter(private val itemList: List<ManagementFeed>)
    : RecyclerView.Adapter<ManagementFeedListAdapter.ViewHolder>()  {

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.management_feed, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 220
        holder.itemView.requestLayout()

//        holder.itemView.setOnClickListener {
//            itemClickListener.onClick(it, position)
//        }
        
        holder.apply {
            bind(item)
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var view : View = v

        fun bind(item: ManagementFeed) {
            view.feeduserId.text = item.userId
            view.feedImg.setImageBitmap(item.feedImg)
            view.feedDate.text = item.date
            view.tv_feedlikecount.text = item.likecnt
            view.tv_feedunlikecount.text = item.unlikecnt
            view.feedNo.text = item.feedNo
        }
    }

    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener: OnItemClickListener



}