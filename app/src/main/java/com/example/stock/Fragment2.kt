package com.example.stock

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.stock.GlobalApplication.Companion.mSocket
import com.example.stock.GlobalApplication.Companion.user_id
import com.example.stock.GlobalApplication.Companion.currentPrice
import com.example.stock.databinding.FragmentTab2Binding
import io.socket.emitter.Emitter
import org.json.JSONArray
import java.text.DecimalFormat

class Fragment2 : Fragment() {
    private var _binding: FragmentTab2Binding? = null
    private val binding get() = _binding!!
    private var tot = 0.0
    private var sum = 0.0
    private var acc = 0.0
    private var b1 = false
    private var b2 = false
    private var b3 = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTab2Binding.inflate(inflater, container, false)

        binding.assetTitle.setOnClickListener {

            b1 = false
            b2 = false
            b3 = false

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
                val array = JSONArray(it).getString(0).replace("[","").replace("]","").split(",")
                sum = 0.0
                for (i in array.indices) {
                    val cur = currentPrice[i].replace(",", "").toDouble()
                    sum += array[i].toDouble()*cur
                }
                binding.totalEvaluationView.text = toDoubleFormat(sum)
                b2 = true
            })

            while(!b1 || !b2 || !b3) {
                val rate = (sum - tot)/tot
                binding.totalView.text = DecimalFormat("###,###,###").format(acc + sum)
                binding.gainAndLossView.text = DecimalFormat("###,###,###").format(sum - tot)
                val yield = DecimalFormat("0.00").format(rate*100) + "%"
                binding.yieldView.text = `yield`
                if(sum - tot > 0) {
                    binding.gainAndLossView.setTextColor(Color.RED)
                    binding.yieldView.setTextColor(Color.RED)
                } else if(sum - tot < 0) {
                    binding.gainAndLossView.setTextColor(Color.BLUE)
                    binding.yieldView.setTextColor(Color.BLUE)
                } else {
                    binding.gainAndLossView.setTextColor(Color.BLACK)
                    binding.yieldView.setTextColor(Color.BLACK)
                }
            }
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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