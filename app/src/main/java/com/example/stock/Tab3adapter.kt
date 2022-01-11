package com.example.stock

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView

class Tab3adapter (private val context: Context, var datas: MutableList<RankInfo>) :
RecyclerView.Adapter<Tab3adapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Tab3adapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_ranking, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffd700"))
            holder.bind(datas[position])
        } else if ( position == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#c0c0c0"))
            holder.bind(datas[position])
        } else if (position == 2) {
            holder.itemView.setBackgroundColor(Color.parseColor("#cd7f32"))
            holder.bind(datas[position])
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"))
            holder.bind(datas[position])
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val rank: TextView = itemView.findViewById(R.id.ranking)
        private val username: TextView = itemView.findViewById(R.id.username)
        private val totalkrw: TextView = itemView.findViewById(R.id.totalkrw)

        fun bind(item: RankInfo) {
            rank.text= item.rank
            username.text = item.name
            totalkrw.text = item.total
        }
    }

}