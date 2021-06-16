package com.example.ehs.Fashionista

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.R

class FashionistaProfileAdapter (val ProfilefeedList : ArrayList<FashionistaUserProfiles>)
    :RecyclerView.Adapter<FashionistaProfileAdapter.CustomViewHolder>()
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FashionistaProfileAdapter.CustomViewHolder {
    // 연결될 화면
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fashionista_profile_item, parent, false)
    // view는 fashionista_user_item을 Adapter에 붙여줌


        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                val curPos : Int = adapterPosition
                val profiles : FashionistaUserProfiles = ProfilefeedList.get(curPos)
                Toast.makeText(parent.context, "피드 : ${profiles.profilefeed}", Toast.LENGTH_SHORT).show()

            } // 리사이클러를 클릭했을 때

        }


    }
    override fun getItemCount(): Int {
        //item들의 총 개수
        return ProfilefeedList.size
    }
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
//        holder.feed.setImageResource(ProfilefeedList[position].profilefeed)

        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 380
        holder.itemView.requestLayout()
    }




    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val feed = itemView.findViewById<ImageView>(R.id.iv_feed)

    }

}