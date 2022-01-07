package com.example.stock

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.stock.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import io.socket.client.Socket

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewpager.apply {
            adapter = MyPagerAdapter(context as FragmentActivity)
        }

        TabLayoutMediator(binding.tabs, binding.viewpager) { tab, position ->
            tab.text = "Title $position"
            when (position) {
                0 -> tab.setIcon(R.drawable.ic_launcher_background)
                1 -> tab.setIcon(R.drawable.ic_launcher_background)
                2 -> tab.setIcon(R.drawable.ic_launcher_background)
            }
        }.attach()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}

