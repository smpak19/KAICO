package com.example.stock

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Tab1adapter(private val context: Context) : RecyclerView.Adapter<Tab1adapter.ViewHolder>() {

    var datas = mutableListOf<CoinInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.coininfo_tab1, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }



    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = itemView.findViewById(R.id.hangul)
        private val ticker: TextView = itemView.findViewById(R.id.ticker)
        private val price: TextView = itemView.findViewById(R.id.trade_price)
        private val rate: TextView = itemView.findViewById(R.id.rate)
        private val total: TextView = itemView.findViewById(R.id.total)

        fun bind(item: CoinInfo) {
            name.text = item.name
            ticker.text = item.ticker
            price.text = item.price
            rate.text = item.rate
            if(item.rate.contains("+")) {
                rate.setTextColor(Color.RED)
                price.setTextColor(Color.RED)
            } else {
                rate.setTextColor(Color.BLUE)
                price.setTextColor(Color.BLUE)
            }
            total.text = item.total
        }
    }
}