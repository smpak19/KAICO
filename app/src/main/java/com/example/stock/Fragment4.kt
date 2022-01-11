package com.example.stock

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.stock.GlobalApplication.Companion.user_id
import com.example.stock.GlobalApplication.Companion.mSocket
import com.example.stock.databinding.FragmentTab4Binding
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.emitter.Emitter
import org.json.JSONArray

class Pwd(var userid : String? , var current: String, var newp: String)

class Fragment4: Fragment() {

    private var _binding: FragmentTab4Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTab4Binding.inflate(inflater, container, false)
        super.onCreate(savedInstanceState)
        binding.username.text = user_id


        binding.btnPerson.setOnClickListener {
            mSocket.emit("delete_account", user_id)
            Toast.makeText(context, "계정이 삭제되었습니다", Toast.LENGTH_SHORT).show()
            mSocket.disconnect()
            user_id = null
            mSocket = IO.socket("http://192.249.18.155:80") // Go to login screen; disconnect socket and re-initialize global socket
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()

        }


        binding.btnPw.setOnClickListener {

            val mDialogView =
                LayoutInflater.from(context).inflate(R.layout.change_pw, null)

            val currentpw: EditText = mDialogView.findViewById(R.id.current_pw)
            val changepw: EditText = mDialogView.findViewById(R.id.change_pw)
            val changeBtn: Button = mDialogView.findViewById(R.id.changeBtn)

            val mBuilder = AlertDialog.Builder(requireContext()).setView(mDialogView).setTitle("비밀번호 변경")
            val ad : AlertDialog = mBuilder.create()

            changeBtn.setOnClickListener {

                val current = currentpw.text.toString()
                val new = changepw.text.toString()
                val gson = Gson()

                mSocket.emit("change_pwd", gson.toJson(Pwd(user_id, current, new)))
                mSocket.on("change_complete", Emitter.Listener {
                    Handler(Looper.getMainLooper()).postDelayed({
                        Toast.makeText(context, "비밀번호 변경 성공", Toast.LENGTH_SHORT).show()
                    }, 0)
                    ad.dismiss()
                })
                mSocket.on("wrong_pass", Emitter.Listener {
                    Handler(Looper.getMainLooper()).postDelayed({
                        Toast.makeText(context, "비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show()
                    }, 0)
                })

            }

            ad.show()

        }




        binding.btnLogout.setOnClickListener { //로그아웃
            mSocket.disconnect()
            Toast.makeText(context, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
            user_id = null
            mSocket = IO.socket("http://192.249.18.155:80") // Go to login screen; disconnect socket and re-initialize global socket
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }


        binding.btnReset.setOnClickListener {  //
            mSocket.emit("reset", user_id)
            Toast.makeText(context, "초기화 되었습니다", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}