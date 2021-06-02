package com.example.ehs.Fashionista

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fashionista.view.*

class FashionistaViewHolder (v: View) : RecyclerView.ViewHolder(v) {
    var view : View = v

    fun bind(item: Fashionista) {
        view.mName.text = item.name
        view.mHashtag.text = item.hashtag
    }
}