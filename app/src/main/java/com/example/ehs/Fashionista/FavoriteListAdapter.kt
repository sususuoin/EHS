package com.example.ehs.Fashionista

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.R
import kotlinx.android.synthetic.main.fragment_community_item.view.*
import kotlinx.android.synthetic.main.fragment_favorite_item.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.ArrayList

class FavoriteListAdapter(private val items: List<Favorite>)
    : RecyclerView.Adapter<FavoriteListAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_favorite_item, parent, false)

        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener { it ->
            Toast.makeText(it.context, "Clicked", Toast.LENGTH_SHORT).show()
        }

        // 리스트사이간격조절
        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 220
        holder.itemView.requestLayout()


        holder.apply {
            bind(listener, item)
//            itemView.tag = item
            itemView.setOnClickListener {

                var dialog = ProgressDialog(holder.itemView.context)
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                dialog.setMessage("업로드 중입니다.")
                dialog.show()

                Toast.makeText(holder.itemView.context,
                    "asdf ${items[position].proId}",
                    Toast.LENGTH_SHORT).show()
                var fashionistaId = items[position].proId
                var fashionistaProfile = items[position].profile

                val stream = ByteArrayOutputStream()
                fashionistaProfile!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray = stream.toByteArray()

                var userId = fashionistaId
                var cuserId: String
                var plusImgName: String
                var plusImgPath: String

                var plusImgNameArr = ArrayList<String>()
                var plusImgPathArr = ArrayList<String>()

                val responseListener: Response.Listener<String?> =
                    Response.Listener<String?> { response ->
                        try {

                            var jsonObject = JSONObject(response)
                            val arr: JSONArray = jsonObject.getJSONArray("response")

                            if(arr.length() == 0) {
                                plusImgNameArr.clear()
                            }
                            else {
                                for (i in 0 until arr.length()) {
                                    val plusObject = arr.getJSONObject(i)
                                    cuserId = plusObject.getString("userId")
                                    plusImgPath = plusObject.getString("plusImgPath")
                                    plusImgName = plusObject.getString("plusImgName")

                                    plusImgPathArr.add(plusImgPath)
                                    plusImgNameArr.add(plusImgName)

                                    Log.d("으음없는건가,..?", plusImgName)
                                }
                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                        AutoPro.setplusImgPath(holder.itemView.context, plusImgPathArr)
                        AutoPro.setplusImgName(holder.itemView.context, plusImgNameArr)

                        Log.d("전문가 아이디", items[position].proId)
                        val intent = Intent(holder.itemView.context, FashionistaProfile_Activity::class.java)
                        intent.putExtra("fashionistaId", fashionistaId)
                        intent.putExtra("fashionistaProfile", byteArray)

                        dialog.dismiss()
                        ContextCompat.startActivity(holder.itemView.context, intent, null)

                    }
                val fashionistaProfileServer_Request = FashionistaProfileServer_Request(userId, responseListener)
                val queue = Volley.newRequestQueue(holder.itemView.context)
                queue.add(fashionistaProfileServer_Request)


            } // item 클릭하면 FashionistaProfile_Activity로 이동

        }
    }


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v


        fun bind(listener: View.OnClickListener, item: Favorite) {

            view.tv_proId.setText(item.proId)
            view.tv_proStyle.setText(item.proStyle)
            view.iv_favorite.setImageBitmap(item.profile)
            view.setOnClickListener(listener)
        }
    }
}

