package com.example.ehs.Fashionista

import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.R
import java.io.ByteArrayOutputStream


class FashionistaListAdapter(private val itemList: List<Fashionista>)
    : RecyclerView.Adapter<FashionistaViewHolder>()  {


    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FashionistaViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fashionista, parent, false)
        return FashionistaViewHolder(inflatedView);
    }

    override fun onBindViewHolder(holder: FashionistaViewHolder, position: Int) {
        val item = itemList[position]

        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 200
        holder.itemView.requestLayout()

        holder.bind(FashionistaList[position])

        holder.apply {
            bind(item)
            itemView.setOnClickListener {

                Toast.makeText(holder.itemView.context,
                    "asdf ${FashionistaList[position].name}",
                    Toast.LENGTH_SHORT).show()
                var fashionistaId = FashionistaList[position].name
                var fashionistaProfile = FashionistaList[position].profile

                val stream = ByteArrayOutputStream()
                fashionistaProfile!!.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                val byteArray = stream.toByteArray()

                Log.d("전문가 아이디", FashionistaList[position].name)
                val intent = Intent(holder.itemView?.context,
                    FashionistaProfile_Activity::class.java)
                intent.putExtra("fashionistaId", fashionistaId)
                intent.putExtra("fashionistaProfile", byteArray)
                ContextCompat.startActivity(holder.itemView.context, intent, null)
            } // item 클릭하면 FashionistaProfile_Activity로 이동
        }

    }





}