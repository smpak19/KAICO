package com.example.stock

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.stock.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val tabIcon = listOf( //탭의 아이콘 리스트
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewpager.apply {
            adapter = MyPagerAdapter(context as FragmentActivity)
        }

        //TabLayout과 ViewPager2연결
        TabLayoutMediator(binding.tabs, binding.viewpager) { tab, position ->
            if(position==0) tab.text = "코인목록"
            if(position==1) tab.text = "투자내역"
            if(position==2) tab.text = "랭킹"
            tab.setIcon(tabIcon[position])  //탭 아이콘은 위에 만들었던 리스트에서 가져와서 포지션에 따라 다르게 나옴
        }.attach()
    }
}