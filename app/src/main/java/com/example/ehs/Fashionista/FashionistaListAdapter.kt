package com.example.ehs.Fashionista

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.Closet.ClothesSave_Request
import com.example.ehs.Login.AutoLogin
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_clothes_save.*
import kotlinx.android.synthetic.main.fashionista.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.ArrayList


class FashionistaListAdapter(private val itemList: List<Fashionista>)
    : RecyclerView.Adapter<FashionistaViewHolder>()  {

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FashionistaViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fashionista, parent, false)
        return FashionistaViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: FashionistaViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(itemList[position])

        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 200
        holder.itemView.requestLayout()

        var favoriteuserIdArr = AutoPro.getFavoriteuserId(holder.itemView.context)

        for (i in 0 until favoriteuserIdArr.size) {
            if (itemList[position].name == favoriteuserIdArr[i]) {
                holder.itemView.findViewById<Button>(R.id.btn_Star_empty).visibility = View.GONE;
                holder.itemView.findViewById<Button>(R.id.btn_Star_fill).visibility = View.VISIBLE;
            }
        }

        holder.apply {
            bind(item)
            itemView.setOnClickListener {
                var dialog = ProgressDialog(holder.itemView.context)
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                dialog.setMessage("업로드 중입니다.")
                dialog.show()

                Toast.makeText(holder.itemView.context, "asdf ${itemList[position].name}", Toast.LENGTH_SHORT).show()
                var fashionistaId = itemList[position].name
                var fashionistaProfile = itemList[position].profile

                val stream = ByteArrayOutputStream()
                fashionistaProfile!!.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                val byteArray = stream.toByteArray()


                var userId = fashionistaId
                var cuserId: String
                var plusImgName: String
                var FashionistaFeedArr = mutableListOf<String>()

                val responseListener: Response.Listener<String?> =
                    Response.Listener<String?> { response ->
                        try {

                            var jsonObject = JSONObject(response)
                            val arr: JSONArray = jsonObject.getJSONArray("response")

                            if(arr.length() == 0) {
                                FashionistaFeedArr.clear()
                            }
                            else {
                                for (i in 0 until arr.length()) {
                                    val plusObject = arr.getJSONObject(i)
                                    cuserId = plusObject.getString("userId")
                                    plusImgName = plusObject.getString("plusImgName")
                                    FashionistaFeedArr.add(plusImgName)
                                    Log.d("으음없는건가,..?", plusImgName)
                                }
                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                        AutoPro.setplusImgName(holder.itemView.context, FashionistaFeedArr as ArrayList<String>)
                        Log.d("전문가 아이디", fashionistaId)
                        val intent = Intent(holder.itemView.context, FashionistaProfile_Activity::class.java)
                        intent.putExtra("fashionistaId", fashionistaId)
                        intent.putExtra("fashionistaProfile", byteArray)

                        dialog.dismiss()
                        ContextCompat.startActivity(holder.itemView.context, intent, null)

                    }
                val fashionistaProfileServer_Request = FashionistaProfileServer_Request(userId!!, responseListener)
                val queue = Volley.newRequestQueue(holder.itemView.context)
                queue.add(fashionistaProfileServer_Request)
            } // item 클릭하면 FashionistaProfile_Activity로 이동




            itemView.btn_Star_empty.setOnClickListener {

                //비어있는 스타 클릭시 채어지는 스타로변경 (즐겨찾기에 등록)
                itemView.findViewById<Button>(R.id.btn_Star_empty).visibility = View.GONE;
                itemView.findViewById<Button>(R.id.btn_Star_fill).visibility = View.VISIBLE;

                val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
                    override fun onResponse(response: String?) {
                        try {
                            val jsonObject = JSONObject(response)
                            var success = jsonObject.getBoolean("success")

                            if(success) {
                                Toast.makeText(itemView.context, "즐겨찾기 성공", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(itemView.context, "즐겨찾기 실패", Toast.LENGTH_SHORT).show()
                                return
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }

                var userId = AutoLogin.getUserId(itemView.context)
                var prouserId = itemList[position].name
                val favoritesave_Request = FavoriteSave_Request(userId, prouserId, responseListener)
                val queue = Volley.newRequestQueue(itemView.context)
                queue.add(favoritesave_Request)

            }

            itemView.btn_Star_fill.setOnClickListener {
                Log.d("클릭","즐겨찾기 취소버튼클릭")
                //채워져있는 스타를 클릭시 비워져있는 스타로 변경 (즐겨찾기 취소)
                itemView.findViewById<Button>(R.id.btn_Star_empty).visibility = View.VISIBLE;
                itemView.findViewById<Button>(R.id.btn_Star_fill).visibility = View.GONE;


                val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
                    override fun onResponse(response: String?) {
                        try {
                            val jsonObject = JSONObject(response)
                            var success = jsonObject.getBoolean("success")

                            if(success) {
                                Toast.makeText(itemView.context, "즐겨찾기 삭제", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(itemView.context, "즐겨찾기 삭제실패", Toast.LENGTH_SHORT).show()
                                return
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }

                var userId = AutoLogin.getUserId(itemView.context)
                var prouserId = itemList[position].name
                val favoriteupdate_Request = FavoriteUpdate_Request(userId, prouserId, responseListener)
                val queue = Volley.newRequestQueue(itemView.context)
                queue.add(favoriteupdate_Request)

            }
        }

    }

}