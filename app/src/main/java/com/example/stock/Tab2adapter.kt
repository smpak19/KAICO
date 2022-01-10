package com.example.stock

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Tab2adapter(private val context: Context) : RecyclerView.Adapter<Tab2adapter.ViewHolder>() {

    private lateinit var info : MutableList<CoinListInfo>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_tab2_coininfo, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = info.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(info[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val coinname: TextView = itemView.findViewById(R.id.buyBtn)
        private val ticker: TextView = itemView.findViewById(R.id.buyBtn)
        private val gain: TextView = itemView.findViewById(R.id.buyBtn)
        private val yields: TextView = itemView.findViewById(R.id.buyBtn)
        private val amount: TextView = itemView.findViewById(R.id.buyBtn)
        private val avg: TextView = itemView.findViewById(R.id.buyBtn)
        private val total: TextView = itemView.findViewById(R.id.buyBtn)
        private val buy: TextView = itemView.findViewById(R.id.buyBtn)

        fun bind(item: CoinListInfo) {
            coinname.text = item.coinname
            ticker.text = item.ticker
            gain.text = item.gain
            yields.text = item.yield
            amount.text = item.amount
            avg.text = item.avg
            total.text = item.total
            buy.text = item.buy

            // settextcolorchange
        }
    }

}