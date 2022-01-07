package com.example.stock

import android.app.Application
import com.kakao.sdk.common.KakaoSdk


//카카오 로그인 API
class GlobalApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "9c622022056e9ad32285b4c7e9fdac32") //NATIVE APP KEY
    }
}