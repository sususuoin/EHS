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
import com.example.ehs.Login.AutoLogin
import com.example.ehs.MainActivity
import com.example.ehs.R
import kotlinx.android.synthetic.main.fragment_feed_item.view.*
import org.json.JSONException
import org.json.JSONObject


class FeedsListAdapter(private val itemList: List<Feed>)
    : RecyclerView.Adapter<FeedsListAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
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
        layoutParams.height = 820
        holder.itemView.requestLayout()

        var feednumlikeArr = AutoFeed.getFeedNumlike(holder.itemView.context)
        var feedlikeArr = AutoFeed.getFeedliketrue(holder.itemView.context)
        var feednolikeArr = AutoFeed.getFeedlikefalse(holder.itemView.context)

        Log.d("zzzz호호호 정말", feednumlikeArr.toString())
        for (i in 0 until feednumlikeArr.size) {
            if (itemList[position].feedNum == feednumlikeArr[i]) {
                if(feedlikeArr[i] == "like") {
                    holder.itemView.findViewById<Button>(R.id.btn_like_before).visibility = View.GONE
                    holder.itemView.findViewById<Button>(R.id.btn_like_after).visibility = View.VISIBLE
                } else if(feednolikeArr[i] == "unlike"){
                    holder.itemView.findViewById<Button>(R.id.btn_unlike_before).visibility = View.GONE
                    holder.itemView.findViewById<Button>(R.id.btn_unlike_after).visibility = View.VISIBLE
                }
            }
        }

        var feedNum = itemList[position].feedNum
        var feed_userId = itemList[position].userID
        var feed_like_true : String = ""
        var feed_like_false : String = ""


        holder.apply {
            bind(item)
//            itemView.tag = item

            //피드 카드아이템 전체를 클릭시
            itemView.setOnClickListener {
                Toast.makeText(it.context,  item.feedImg.toString(), Toast.LENGTH_SHORT).show()
            }


            var userId = AutoLogin.getUserId(itemView.context)


            itemView.btn_like_before.setOnClickListener {
                //좋아요누르기전
                if(feednumlikeArr.contains(item.feedNum)) {
                    Toast.makeText(it.context, "이미 평가를 하셨습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    itemView.findViewById<Button>(R.id.btn_like_before).visibility = View.GONE
                    itemView.findViewById<Button>(R.id.btn_like_after).visibility = View.VISIBLE

                    Log.d("하이킥보는중", itemView.tv_feedlikecount.text.toString() )
                    var cntplus = (itemView.tv_feedlikecount.text.toString().toInt())+1
                    Log.d("하이킥보는중22222", cntplus.toString())
                    itemView.tv_feedlikecount.setText(cntplus.toString())
                    Toast.makeText(it?.context, "좋아요를 누르셨습니다.", Toast.LENGTH_SHORT).show()

                    val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
                        override fun onResponse(response: String?) {
                            try {
                                val jsonObject = JSONObject(response)
                                var success = jsonObject.getBoolean("success")

                                if(success) {
//                                    Toast.makeText(itemView.context, "좋아요 성공", Toast.LENGTH_SHORT).show()
                                    (itemView.context as MainActivity).Feed_like_check()
                                    (itemView.context as MainActivity).GetFeedLikeTotalcnt()
                                } else {
//                                    Toast.makeText(itemView.context, "좋아요 실패", Toast.LENGTH_SHORT).show()
                                    return
                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                    }

                    feed_like_true = "like"
                    feed_like_false = ""
                    val feedLikeSave_Request = FeedLikeSave_Request(feedNum, feed_userId, userId, feed_like_true, feed_like_false, responseListener)
                    val queue = Volley.newRequestQueue(itemView.context)
                    queue.add(feedLikeSave_Request)
                }
            }

            itemView.btn_unlike_before.setOnClickListener {
                if(feednumlikeArr.contains(item.feedNum)) {
                    Toast.makeText(it.context, "이미 평가를 하셨습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    itemView.findViewById<Button>(R.id.btn_unlike_before).visibility = View.GONE
                    itemView.findViewById<Button>(R.id.btn_unlike_after).visibility = View.VISIBLE

                    Log.d("하이킥보는중", itemView.tv_feedunlikecount.text.toString() )
                    var cntplus = (itemView.tv_feedunlikecount.text.toString().toInt())+1
                    Log.d("하이킥보는중22222", cntplus.toString())
                    itemView.tv_feedunlikecount.setText(cntplus.toString())
                    Toast.makeText(it?.context, "싫어요를 누르셨습니다.", Toast.LENGTH_SHORT).show()

                    val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
                        override fun onResponse(response: String?) {
                            try {
                                val jsonObject = JSONObject(response)
                                var success = jsonObject.getBoolean("success")

                                if(success) {
//                                    Toast.makeText(itemView.context, "싫어요 성공", Toast.LENGTH_SHORT).show()
                                    (itemView.context as MainActivity).Feed_like_check()
                                    (itemView.context as MainActivity).GetFeedLikeTotalcnt()
                                } else {
//                                    Toast.makeText(itemView.context, "싫어요 실패", Toast.LENGTH_SHORT).show()
                                    return
                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                    }

                    feed_like_true = ""
                    feed_like_false = "unlike"
                    val feedLikeSave_Request = FeedLikeSave_Request(feedNum, feed_userId, userId, feed_like_true, feed_like_false, responseListener)
                    val queue = Volley.newRequestQueue(itemView.context)
                    queue.add(feedLikeSave_Request)
                }
            }

            itemView.btn_like_after.setOnClickListener {
                Toast.makeText(it?.context, "이미 평가를 하셨습니다.", Toast.LENGTH_SHORT).show()
            }
            itemView.btn_unlike_after.setOnClickListener {
                Toast.makeText(it?.context, "이미 평가를 하셨습니다.", Toast.LENGTH_SHORT).show()
                //싫어요누른상태
                //                    view.findViewById<Button>(R.id.btn_unlike_before).visibility = View.VISIBLE
                //                    view.findViewById<Button>(R.id.btn_unlike_after).visibility = View.GONE

            }
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun bind(item: Feed) {
            view.iv_userphoto.setImageBitmap(item.userprofileImg)
            view.tv_userID.setText(item.userID)
            view.tv_styletag.setText("# "+item.styletag)
            view.iv_feedphoto.setImageBitmap(item.feedImg)

            view.tv_feedlikecount.setText(item.feedLikeCount)
            view.tv_feedunlikecount.setText(item.feedUnlikeCount)


        }

    }

}