package com.example.stock

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.stock.databinding.FragmentCoinlistBinding
import org.json.JSONArray
import org.json.JSONException
import java.text.DecimalFormat

class Fragment1 : Fragment() {
    private var _binding: FragmentCoinlistBinding? = null
    private val binding get() = _binding!!

    companion object {
        var requestQueue: RequestQueue?=null
    }
    private val TAG = "Main"

    private lateinit var t1 : TextView
    private lateinit var btn : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCoinlistBinding.inflate(inflater, container, false)

        t1 = binding.jsonview
        btn = binding.getjson
        btn.setOnClickListener {
            send()
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

    fun send() : Unit {
        val url = "https://api.upbit.com/v1/market/all"
        val request = object : StringRequest(
            Request.Method.GET, url, {
                getCoinList(it)
            }, {
                Log.d(TAG, it.toString())
            }
        ){}
        request.setShouldCache(false)
        requestQueue?.add(request)
    }

    fun getCoinList(data: String): Unit {
        try {
            val jsonArray = JSONArray(data)
            var coinList = JSONArray()

            for(i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val s1 = jsonObject.get("market").toString()
                if(s1.contains("KRW")) {
                    coinList.put(jsonObject)
                } else {
                    continue
                }
                t1.text = coinList.toString()
            }
        } catch(e: JSONException) {
            e.printStackTrace()
        }
    }

}