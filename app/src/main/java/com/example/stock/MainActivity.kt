package com.example.stock

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.stock.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import io.socket.client.Socket
import com.example.stock.GlobalApplication.Companion.mSocket
import com.google.android.material.snackbar.Snackbar
import io.socket.client.IO

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mBackWait: Long = 0

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

        if(System.currentTimeMillis() - mBackWait >= 2000) {
            mBackWait = System.currentTimeMillis()
            Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 로그아웃됩니다", Toast.LENGTH_SHORT).show()
        } else {
            super.onBackPressed()
            mSocket.disconnect()
            mSocket = IO.socket("http://192.249.18.155:80") // Go to login screen; disconnect socket and re-initialize global socket
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}

