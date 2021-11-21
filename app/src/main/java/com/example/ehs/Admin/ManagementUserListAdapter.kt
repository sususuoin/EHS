package com.example.ehs.Admin


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.R
import kotlinx.android.synthetic.main.fashionista.view.*

class ManagementUserListAdapter(private val itemList: List<ManagementUser>)
    : RecyclerView.Adapter<ManagementUserListAdapter.ViewHolder>()  {

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.management_user, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 200
        holder.itemView.requestLayout()

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        
        holder.apply {
            bind(item)
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var view : View = v

        fun bind(item: ManagementUser) {
            view.mName.text = item.name
            view.mHashtag.text = item.hashtag
            view.mProfile.setImageBitmap(item.profile)
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