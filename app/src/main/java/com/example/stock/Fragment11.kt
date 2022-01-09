package com.example.stock

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.stock.databinding.FragmentTab1CoinlistBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat


class Fragment11: Fragment() {

    // 거래량 상위 10위 코인
    private val top10 : Array<String> = arrayOf("BTC", "LINK", "ETH", "XRP", "SAND", "DOGE", "BORA", "BTT", "ADA", "EOS")
    private val coin10 : Array<String> = arrayOf("비트코인", "체인링크", "이더리움", "리플", "샌드박스", "도지코인", "보라", "비트토렌트", "에이다", "이오")

    private var _binding: FragmentTab1CoinlistBinding? = null
    private val binding get() = _binding!!

    lateinit var tab1adapter: Tab1adapter
    val datas = mutableListOf<CoinInfo>()

    companion object {
        var requestQueue: RequestQueue?=null
    }
    private val TAG = "Main"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTab1CoinlistBinding.inflate(inflater, container, false)

        tab1adapter = Tab1adapter(requireContext())
        binding.recyclerView.adapter = tab1adapter
        binding.recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        binding.search.setOnClickListener {
            datas.clear()
            getData()
        }

        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getData() {
        var url = "https://api.upbit.com/v1/ticker?markets="
        for(i in top10.indices) {
            url += "KRW-" + top10[i]
            if (i != top10.size - 1 ) {
                url += ","
            }
        }
        val request = object : StringRequest(
            Request.Method.GET, url, {
                convert(it)
            }, {
                Log.d(TAG, it.toString())
            }
        ) {}
        request.setShouldCache(false)
        requestQueue?.add(request)
    }

    fun convert(data: String): Unit {
        try {
            val jsonArray = JSONArray(data)
            for(i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val name = coin10[i]
                val ticker = jsonObject.get("market").toString().replace("KRW-", "")
                val trade = toDoubleFormat(jsonObject.getDouble("trade_price")).toString()
                var rate = DecimalFormat("0.00").format((jsonObject.getDouble("change_rate"))*100)
                val sign = jsonObject.get("change").toString()
                if (sign == "RISE") {
                    rate = "+$rate%"
                } else if(sign == "FALL") {
                    rate = "-$rate%"
                }
                val amount = jsonObject.get("signed_change_price").toString()
                val total = toDoubleFormat(jsonObject.getDouble("acc_trade_price_24h")/1000000) + "백만"
                datas.add(CoinInfo(name, ticker, trade, rate, total))
            }
            tab1adapter.datas = datas
            tab1adapter.notifyDataSetChanged()
        } catch(e: JSONException) {
            e.printStackTrace()
        }
    }

    fun toDoubleFormat(num: Double): String? {
        var df: DecimalFormat? = null
        df = when {
            num in 100.0..999.9 -> {
                DecimalFormat("000.0")
            }
            num in 10.0..99.99 -> {
                DecimalFormat("00.00")
            }
            num in 1.0..9.9999 -> {
                DecimalFormat("0.000")
            }
            num < 1 -> {
                DecimalFormat("0.0000")
            }
            else -> {
                DecimalFormat("###,###,###")
            }
        }
        return df.format(num)
    }

}