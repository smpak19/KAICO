package com.example.stock

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stock.GlobalApplication.Companion.mSocket
import com.example.stock.GlobalApplication.Companion.user_id
import com.example.stock.GlobalApplication.Companion.currentPrice
import com.example.stock.databinding.FragmentTab2Binding
import com.google.gson.Gson
import io.socket.emitter.Emitter
import org.json.JSONArray
import java.text.DecimalFormat
import kotlin.concurrent.timer

class Cur(var userid: String?, var current: String)

class Fragment2 : Fragment() {

    private val top10 : Array<String> = arrayOf("BTC", "LINK", "ETH", "XRP", "SAND", "DOGE", "BORA", "BTT", "ADA", "EOS")
    private val coin10 : Array<String> = arrayOf("비트코인", "체인링크", "이더리움", "리플", "샌드박스", "도지코인", "보라", "비트토렌트", "에이다", "이오스")

    private var _binding: FragmentTab2Binding? = null
    private val binding get() = _binding!!
    private var info = mutableListOf<CoinListInfo>()
    lateinit var tab2adapter: Tab2adapter

    private var array : List<String> = listOf("0", "0", "0", "0", "0", "0", "0", "0", "0", "0")
    private var list : List<String> = listOf("0", "0", "0", "0", "0", "0", "0", "0", "0", "0")
    private var tot = 0.0
    private var sum = 0.0
    private var acc = 0.0
    private var b1 = false
    private var b2 = false
    private var b3 = false
    private var b4 = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTab2Binding.inflate(inflater, container, false)
        tab2adapter = Tab2adapter(requireContext())
        binding.recyclerView.adapter = tab2adapter
        tab2adapter.info = info
        binding.recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        binding.assetTitle.setOnClickListener {

            b1 = false
            b2 = false
            b3 = false
            b4 = false

            mSocket.emit("merge", user_id)

            mSocket.emit("get_account", user_id)
            mSocket.on("give_account", Emitter.Listener {
                acc = JSONArray(it).getDouble(0)
                binding.krwView.text = toDoubleFormat(acc)
                b3 =true
            })

            mSocket.emit("get_total", user_id)
            mSocket.on("give_maesu", Emitter.Listener {
                tot = JSONArray(it).getDouble(0)
                binding.totalBuyView.text = toDoubleFormat(tot)
                b1 = true
            })

            mSocket.emit("array", user_id)
            mSocket.on("arrayget", Emitter.Listener {
                array = JSONArray(it).getString(0).replace("[","").replace("]","").split(",")
                sum = 0.0
                for (i in array.indices) {
                    val cur = currentPrice[i].replace(",", "").toDouble()
                    sum += array[i].toDouble()*cur
                }
                binding.totalEvaluationView.text = toDoubleFormat(sum)
                b2 = true
            })

            mSocket.emit("list", user_id)
            mSocket.on("listget", Emitter.Listener {
                list = JSONArray(it).getString(0).replace("[","").replace("]","").split(",")
                b4 = true
            })

            while(!b1 || !b2 || !b3 || !b4) {
                val rate = (sum - tot)/tot
                binding.totalView.text = DecimalFormat("###,###,###").format(acc + sum)
                binding.gainAndLossView.text = DecimalFormat("###,###,###").format(sum - tot)
                val yield = DecimalFormat("0.00").format(rate*100) + "%"
                binding.yieldView.text = `yield`
                when {
                    sum - tot > 0 -> {
                        binding.gainAndLossView.setTextColor(Color.RED)
                        binding.yieldView.setTextColor(Color.RED)
                    }
                    sum - tot < 0 -> {
                        binding.gainAndLossView.setTextColor(Color.BLUE)
                        binding.yieldView.setTextColor(Color.BLUE)
                    }
                    else -> {
                        binding.gainAndLossView.setTextColor(Color.BLACK)
                        binding.yieldView.setTextColor(Color.BLACK)
                    }
                }
                info.clear()
                getData()
            }
            val gson = Gson()
            mSocket.emit("set_current", gson.toJson(Cur(user_id, (acc+sum).toString())))
        }

        timer(period = 1000, initialDelay = 3000) {
            activity?.runOnUiThread {
                binding.assetTitle.performClick()
            }
        }
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getData() {
        for(i in top10.indices) {
            if(array[i].toDouble() != 0.0) {
                val coinname = coin10[i]
                val tickers = top10[i]
                val ticker = "($tickers)"
                val amount = toDoubleFormat(array[i].toDouble()) + " " + tickers
                val avg = toDoubleFormat(list[i].toDouble()/array[i].toDouble()) + " KRW"

                val num = (array[i].toDouble())*(currentPrice[i].replace(",", "").toDouble())
                val total = toDoubleFormat(num) + " KRW"
                val buy = toDoubleFormat(list[i].toDouble()) + " KRW"

                val num2 = num - (list[i].toDouble())
                val gain = DecimalFormat("###,###,###").format(num2.toInt())
                val yields = DecimalFormat("0.00").format(num2 * 100 / num) + "%"

                info.add(CoinListInfo(coinname, ticker, gain, yields, amount, avg, total, buy))
            }
        }
        tab2adapter.info = info
        tab2adapter.notifyDataSetChanged()
    }

    private fun toDoubleFormat(num: Double): String? {
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