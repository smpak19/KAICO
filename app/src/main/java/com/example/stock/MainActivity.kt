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
import com.example.stock.GlobalApplication.Companion.user_id
import com.google.android.material.snackbar.Snackbar
import io.socket.client.IO

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mBackWait: Long = 0

    private val tabIcon = listOf( //탭의 아이콘 리스트
        R.drawable.ic_launcher_bitcoin1,
        R.drawable.ic_launcher_bitcoin2,
        R.drawable.ic_launcher_bitcoin3,
        R.drawable.ic_launcher_bitcoin4
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewpager.offscreenPageLimit = 4
        binding.viewpager.apply {
            adapter = MyPagerAdapter(context as FragmentActivity)
        }

        //TabLayout과 ViewPager2연결
        TabLayoutMediator(binding.tabs, binding.viewpager) { tab, position ->
            if(position==0) tab.text = "코인목록"
            if(position==1) tab.text = "투자내역"
            if(position==2) tab.text = "랭킹"
            if(position==3) tab.text = "사용자"
            tab.setIcon(tabIcon[position])  //탭 아이콘은 위에 만들었던 리스트에서 가져와서 포지션에 따라 다르게 나옴
        }.attach()
    }

    override fun onBackPressed() {

        if(System.currentTimeMillis() - mBackWait >= 2000) {
            mBackWait = System.currentTimeMillis()
            Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 로그아웃됩니다", Toast.LENGTH_SHORT).show()
        } else {
            super.onBackPressed()
            mSocket.disconnect()
            user_id = null
            mSocket = IO.socket("http://192.249.18.155:80") // Go to login screen; disconnect socket and re-initialize global socket
            finish()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}

