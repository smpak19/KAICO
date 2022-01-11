package com.example.stock

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import com.example.stock.GlobalApplication.Companion.mSocket
import com.example.stock.GlobalApplication.Companion.user_id
import com.kakao.sdk.user.UserApiClient


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: KakaoLoginBinding
    private lateinit var id : String
    private lateinit var pwd : String

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

                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e(TAG, "사용자 정보 요청 실패", error)
                    }
                    else if (user != null) {
                        //id = user.kakao_account.email
                        id = user.id.toString()
                        pwd = user.kakaoAccount?.email.toString()
                        kakao_login()
                        //id = binding.idtext.text.toString()
                        /*
                        Log.i(TAG, "사용자 정보 요청 성공" +
                                "\n회원번호: ${user.id}" +
                                "\n이메일: ${user.kakaoAccount?.email}" +
                                "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")

                         */


                    }
                }
                //intent.putExtra("name", getKakaoAccount().getProfile().getNickname()) //추가
                //intent.putExtra("id", result.getKakaoAccount().getEmail()) //추가
                //intent.putExtra("imgnumber", result.getKakaoAccount().getProfile().getProfileImageUrl()) //추가
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

        mSocket.connect()
        Log.d("SOCKET", "Connection success : " + mSocket.id())

        mSocket.on(Socket.EVENT_CONNECT, onConnect)
        mSocket.on("login_true", onTrue)
        mSocket.on("login_false", onFalse)
    }

    private fun kakao_login(): Unit {
        mSocket.connect()
        Log.d("SOCKET", "Connection success : " + mSocket.id())
        mSocket.on(Socket.EVENT_CONNECT, onConnect2)
        mSocket.on("kakao_sign", onSign)
        mSocket.on("kakao_true", onKtrue)
    }

    private val onSign : Emitter.Listener = Emitter.Listener {
        Handler(Looper.getMainLooper()).postDelayed({
            Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
        }, 0)
        user_id = id
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private val onKtrue : Emitter.Listener = Emitter.Listener {
        Handler(Looper.getMainLooper()).postDelayed({
            Toast.makeText(this, "환영합니다", Toast.LENGTH_SHORT).show()
        }, 0)
        user_id = id
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private val onConnect2 : Emitter.Listener = Emitter.Listener {
        val gson = Gson()
        mSocket.emit("kakao_signin", gson.toJson(PersonInfo(id,pwd)))
    }

    private val onConnect : Emitter.Listener = Emitter.Listener {
        val gson = Gson()
        mSocket.emit("login", gson.toJson(PersonInfo(id, pwd)))

    }

    private val onTrue : Emitter.Listener = Emitter.Listener {
        Handler(Looper.getMainLooper()).postDelayed({
            Toast.makeText(this, "환영합니다", Toast.LENGTH_SHORT).show()
        }, 0)
        user_id = id
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private val onFalse : Emitter.Listener = Emitter.Listener {
        Handler(Looper.getMainLooper()).postDelayed({
            Toast.makeText(this, "아이디/비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
        }, 0)
        mSocket.disconnect()
        user_id = null
        mSocket = IO.socket("http://192.249.18.140:80") // Login fail, disconnect socket and reinitialize socket 192.249.18.155:80
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}