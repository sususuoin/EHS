package com.example.ehs.Calendar

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.Closet.Clothes
import com.example.ehs.Feed.Community
import com.example.ehs.R
import kotlinx.android.synthetic.main.clothes.view.*
import kotlinx.android.synthetic.main.fragment_community_item.view.*


class CalendarClothesListAdapter(private val items: MutableList<Clothes>)
    : RecyclerView.Adapter<CalendarClothesListAdapter.ViewHolder>() {

    var clothesclicked = false
    var clickList = ArrayList<String>()


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v

        fun bind(listener: View.OnClickListener, item: Clothes) {
            view.clothes.setImageBitmap(item.clothes)
            view.setOnClickListener(listener)


        }
    }



    override fun getItemCount(): Int {
        return items.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.clothes, parent, false)


        return ViewHolder(inflatedView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
//---------------------------------------------------------------------------------
//        fun clicked(it : View ) {
//       if (!clothesclicked){
//           Toast.makeText(it.context, "Clicked" + item, Toast.LENGTH_SHORT).show()
//           holder.itemView.setBackgroundResource(R.drawable.cody_background)
//
//
////           view.findViewById<MenuView.ItemView>(R.id.clothes).setBackgroundResource(R.drawable.cody_background)
//
//
//
//       }
//
//    }
//---------------------------------------------------------------------------------



        val listener = View.OnClickListener{ it ->
//            holder.itemView.setBackgroundResource(R.drawable.cody_background)

            //--------------------------------------------------------------

            if(clickList.size == 0) {
                holder.itemView.setBackgroundResource(R.drawable.cody_background)
                clickList.add(item.toString())

            } else{
                for(i in 0 until clickList.size) {
                    if (item.toString() == clickList[i]) {
                        holder.itemView.setBackgroundResource(R.drawable.button_background)
                        holder.itemView.setBackgroundColor(Color.parseColor("#E7E7E7"))
                        clickList.remove(item.toString())
                    } else {
                        holder.itemView.setBackgroundResource(R.drawable.cody_background)
                        clickList.add(item.toString())

                    }
                }
            }
            Log.d("zzzzzqqzaaa", clickList.toString())


//            if (!clothesclicked) {
//                //clothesclicked false일떄 실행
//                Log.d("aa22", clothesclicked.toString())
//                holder.itemView.setBackgroundResource(R.drawable.cody_background)
//                clickList.add(item.toString())
//                Log.d("aa22", clickList.toString())
//                clothesclicked=true
//            } else {
//                Log.d("aa33", clothesclicked.toString())
//                Toast.makeText(it?.context, "Cancel" + item, Toast.LENGTH_SHORT).show()
//                holder.itemView.setBackgroundResource(R.drawable.button_background)
//                holder.itemView.setBackgroundColor(Color.parseColor("#E7E7E7"))
//                clickList.remove(item.toString())
//                Log.d("aa33", clickList.toString())
//                clothesclicked=false
//            }
            //==========================================================================

        }

        //리스트사이간격조절
        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 350
        layoutParams.width = 350
        holder.itemView.requestLayout()




        holder.apply {
            bind(listener, item)
            itemView.tag = item
//---------------------------------------------------------------------------------
//            View.OnClickListener { it ->
//                if (it == null) {
//                    if (!clothesclicked) {
//                        Toast.makeText(it?.context, "Clicked" + item, Toast.LENGTH_SHORT).show()
//                        holder.itemView.setBackgroundResource(R.drawable.cody_background)
//                    } else {
//                            Toast.makeText(it?.context, "Cancel" + item, Toast.LENGTH_SHORT).show()
//                        holder.itemView.setBackgroundResource(R.drawable.button_background)
//                        it == null
//                    }
//
//                }
//            }
//---------------------------------------------------------------------------------
        }

    }}
