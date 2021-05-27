package com.example.ehs

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fashionista.view.*

class FashionistaViewHolder (v: View) : RecyclerView.ViewHolder(v) {
    var view : View = v

    fun bind(item: Fashionista) {
        view.mName.text = item.name
        view.mTel.text = item.tel
    }
}