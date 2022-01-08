package com.example.stock

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import io.socket.client.IO
import io.socket.client.Socket


//카카오 로그인 API
class GlobalApplication : Application(){

    companion object {
        var mSocket : Socket = IO.socket("http://192.249.18.155:80")
    }

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "9c622022056e9ad32285b4c7e9fdac32") //NATIVE APP KEY
    }
}

//import com.example.stock.GlobalApplication.Companion.mSocket