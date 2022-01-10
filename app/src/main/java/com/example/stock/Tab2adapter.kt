package com.example.stock

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Tab2adapter(private val context: Context) : RecyclerView.Adapter<Tab2adapter.ViewHolder>() {

    lateinit var info : MutableList<CoinListInfo>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.tab2_mycoin, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = info.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(info[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val coinname: TextView = itemView.findViewById(R.id.mycoin_name_k)
        private val ticker: TextView = itemView.findViewById(R.id.mycoin_name)
        private val gain: TextView = itemView.findViewById(R.id.plusminus)
        private val yields: TextView = itemView.findViewById(R.id.percent)
        private val amount: TextView = itemView.findViewById(R.id.num_get)
        private val avg: TextView = itemView.findViewById(R.id.e_price)
        private val total: TextView = itemView.findViewById(R.id.app_amount)
        private val buy: TextView = itemView.findViewById(R.id.buy_price)

        fun bind(item: CoinListInfo) {
            coinname.text = item.coinname
            ticker.text = item.ticker
            gain.text = item.gain
            yields.text = item.yield
            amount.text = item.amount
            avg.text = item.avg
            total.text = item.total
            buy.text = item.buy

            if(gain.text.toString().replace(",", "").toDouble() > 0.0) {
                gain.setTextColor(Color.RED)
                yields.setTextColor(Color.RED)
            } else if(gain.text.toString().replace(",", "").toDouble() < 0.0) {
                gain.setTextColor(Color.BLUE)
                yields.setTextColor(Color.BLUE)
            } else {
                gain.setTextColor(Color.BLACK)
                yields.setTextColor(Color.BLACK)
            }

            // settextcolorchange
        }
    }

}