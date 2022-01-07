package com.example.stock

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.stock.databinding.KakaoLoginBinding
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.util.Utility
import com.google.gson.Gson
import io.socket.client.Socket
import io.socket.client.IO
import io.socket.emitter.Emitter
import java.net.URISyntaxException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: KakaoLoginBinding
    private lateinit var id : String
    private lateinit var pwd : String
    lateinit var mSocket : Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = KakaoLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //로그인 버튼 코드


        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱입니다", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태입니다", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없습니다", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (token != null) {
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)  //로그인 성공 시 다음 화면
                startActivity(intent)
            }
        }
        binding.kakaoLoginButton.setOnClickListener {
            if(LoginClient.instance.isKakaoTalkLoginAvailable(this)){
                LoginClient.instance.loginWithKakaoTalk(this, callback = callback)
            }else{
                LoginClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }

        binding.signin.setOnClickListener {
            val signintent = Intent(this, SigninActivity::class.java)
            startActivity(signintent)
        }

        binding.loginbtn.setOnClickListener {

            id = binding.idtext.text.toString()
            pwd = binding.pwdtext.text.toString()

            if(id.equals("") || pwd.equals("")) {
                Toast.makeText(applicationContext, "텍스트를 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else {
                login()
            }
        }
    }

    private fun login(): Unit {
        try {
            mSocket = IO.socket("http://172.20.10.2:3000")
            Log.d("SOCKET", "Connection success : " + mSocket.id())
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

        mSocket.connect()

        mSocket.on(Socket.EVENT_CONNECT, onConnect)
        mSocket.on("login_true", onTrue)
        mSocket.on("login_false", onFalse)
    }

    val onConnect : Emitter.Listener = Emitter.Listener {
        val gson = Gson()
        mSocket.emit("login", gson.toJson(PersonInfo(id, pwd)))

    }

    val onTrue : Emitter.Listener = Emitter.Listener {
        Handler(Looper.getMainLooper()).postDelayed({
            Toast.makeText(this, "환영합니다", Toast.LENGTH_SHORT).show()
        }, 0)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    val onFalse : Emitter.Listener = Emitter.Listener {
        Handler(Looper.getMainLooper()).postDelayed({
            Toast.makeText(this, "아이디/비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
        }, 0)
        mSocket.disconnect()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}