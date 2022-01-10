package com.example.stock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.stock.GlobalApplication.Companion.mSocket
import com.example.stock.GlobalApplication.Companion.user_id
import com.example.stock.databinding.FragmentTab2Binding
import io.socket.emitter.Emitter
import org.json.JSONArray
import java.text.DecimalFormat

class Fragment2 : Fragment() {
    private var _binding: FragmentTab2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTab2Binding.inflate(inflater, container, false)

        binding.assetTitle.setOnClickListener {
            mSocket.emit("get_account", user_id)
            mSocket.on("give_account", Emitter.Listener {
                val account = toDoubleFormat(JSONArray(it).getDouble(0))
                binding.krwView.text = account
            })

            mSocket.emit("get_total", user_id)
            mSocket.on("give_total", Emitter.Listener {
                val total = toDoubleFormat(JSONArray(it).getDouble(0))
                binding.totalView.text = total
            })
            mSocket.on("give_maesu", Emitter.Listener {
                val maesu = toDoubleFormat(JSONArray(it).getDouble(0))
                binding.totalBuyView.text = maesu
            })
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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