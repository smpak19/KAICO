package com.example.stock

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.stock.databinding.ActivitySigninBinding
import com.google.gson.Gson
import io.socket.client.Socket
import io.socket.client.IO
import io.socket.emitter.Emitter
import java.net.URISyntaxException
import com.example.stock.GlobalApplication.Companion.mSocket

public class PersonInfo(var id: String, var pwd: String)

class SigninActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySigninBinding
    private lateinit var id : String
    private lateinit var pwd : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.okbutton.setOnClickListener {
            id = binding.newid.text.toString()
            pwd = binding.newpwd.text.toString()

            if(id.equals("") || pwd.equals("")) {
                Toast.makeText(applicationContext, "텍스트를 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else {
                signin()
            }
        }
    }

    private fun signin(): Unit {

        mSocket.connect()
        Log.d("SOCKET", "Connection success : " + mSocket.id())
        mSocket.on(Socket.EVENT_CONNECT, onConnect)
        mSocket.on("sign_in_success", onSuc)
        mSocket.on("duplicate_id", onDup)

    }

    private val onConnect : Emitter.Listener = Emitter.Listener {
        val gson = Gson()
        mSocket.emit("signin", gson.toJson(PersonInfo(id,pwd)))
    }

    private val onSuc : Emitter.Listener = Emitter.Listener {
        Handler(Looper.getMainLooper()).postDelayed({
            Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
        }, 0)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private val onDup : Emitter.Listener = Emitter.Listener {
        Handler(Looper.getMainLooper()).postDelayed({
            Toast.makeText(this, "아이디가 중복됩니다.", Toast.LENGTH_SHORT).show()
        }, 0)
        mSocket.disconnect()
        mSocket = IO.socket("http://192.249.18.155:80")
    }
}