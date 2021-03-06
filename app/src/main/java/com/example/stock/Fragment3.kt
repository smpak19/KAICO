package com.example.stock

import android.graphics.PointF.length
import android.opengl.Matrix.length
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.stock.databinding.FragmentTab3Binding

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stock.GlobalApplication.Companion.currentPrice
import io.socket.emitter.Emitter
import com.example.stock.GlobalApplication.Companion.mSocket
import com.example.stock.GlobalApplication.Companion.user_id
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import java.text.DecimalFormat
import java.util.*
import kotlin.concurrent.timer
import kotlin.concurrent.timerTask

class Fragment3 : Fragment() {
    private var _binding: FragmentTab3Binding? = null
    private val binding get() = _binding!!
    lateinit var tab3adapter: Tab3adapter
    private var datas = mutableListOf<RankInfo>()
    private var array = JSONArray()
    private lateinit var timer: Timer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {


        timer = timer(period = 2000, initialDelay = 3000) {
            activity?.runOnUiThread {
                val gson = Gson()
                mSocket.emit("get_current", gson.toJson(currentPrice))
                mSocket.on("here", Emitter.Listener {
                    array = JSONArray(it).getJSONArray(0)})
                binding.resetrank.performClick()
            }
        }

        _binding = FragmentTab3Binding.inflate(inflater, container, false)
        tab3adapter = Tab3adapter(requireContext(), datas)
        binding.rankRecyclerView.adapter = tab3adapter
        binding.rankRecyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        //db.geCollection('_id').find({}).sort({"account"})
        //binding.username.text = user_id

        binding.resetrank.setOnClickListener {
            getData()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        timer.cancel()
    }

    private fun getData() {
        val temp = mutableListOf<RankInfo>()
        for (i in 0 until array.length()) {
            val json = array.getJSONArray(i)
            val name = json.getString(0)
            val current = toDoubleFormat(json.getDouble(1))
            temp.add(RankInfo((i+1).toString(), name, current))
        }
        datas.clear()
        datas.addAll(temp)
        tab3adapter.notifyDataSetChanged()
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