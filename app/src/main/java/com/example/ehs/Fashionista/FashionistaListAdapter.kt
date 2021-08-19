package com.example.ehs.Fashionista

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
import com.example.ehs.R
import kotlinx.android.synthetic.main.fashionista.view.*
import java.io.ByteArrayOutputStream


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

        holder.apply {
            bind(item)
            itemView.setOnClickListener {

                Toast.makeText(holder.itemView.context,
                    "asdf ${itemList[position].name}",
                    Toast.LENGTH_SHORT).show()
                var fashionistaId = itemList[position].name
                var fashionistaProfile = itemList[position].profile

                val stream = ByteArrayOutputStream()
                fashionistaProfile!!.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                val byteArray = stream.toByteArray()

                Log.d("전문가 아이디", itemList[position].name)
                val intent = Intent(holder.itemView?.context,
                    FashionistaProfile_Activity::class.java)
                intent.putExtra("fashionistaId", fashionistaId)
                intent.putExtra("fashionistaProfile", byteArray)
                ContextCompat.startActivity(holder.itemView.context, intent, null)
            } // item 클릭하면 FashionistaProfile_Activity로 이동

            itemView.btn_Star_empty.setOnClickListener {
                //비어있는 스타 클릭시 채어지는 스타로변경 (즐겨찾기에 등록)
                star_fill(itemView)

            }

            itemView.btn_Star_fill.setOnClickListener {
                Log.d("클릭","즐겨찾기 취소버튼클릭")
                //채워져있는 스타를 클릭시 비워져있는 스타로 변경 (즐겨찾기 취소)
                star_empty(itemView)

            }
        }

    }

    private fun star_fill(it : View) {
        it.findViewById<Button>(R.id.btn_Star_empty).visibility = View.GONE;
        it.findViewById<Button>(R.id.btn_Star_fill).visibility = View.VISIBLE;

        //디비저장


    }

    private fun star_empty(it : View ) {
        it.findViewById<Button>(R.id.btn_Star_empty).visibility = View.VISIBLE;
        it.findViewById<Button>(R.id.btn_Star_fill).visibility = View.GONE;
    }





}