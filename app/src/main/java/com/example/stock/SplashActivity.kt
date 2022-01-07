package com.example.stock

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView

class SplashActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        var loadingImage = findViewById(R.id.loading_image) as LottieAnimationView

        // 애니메이션 시작
        loadingImage.playAnimation()

        val handler: Handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, LoginActivity::class.java) //다음페이지
            startActivity(intent)
            finish()
        },3000) //3초간 지연
    }

}