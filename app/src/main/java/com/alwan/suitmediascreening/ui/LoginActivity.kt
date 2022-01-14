package com.alwan.suitmediascreening.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alwan.suitmediascreening.databinding.ActivityLoginBinding
import com.alwan.suitmediascreening.helpers.isPalindrome
import com.alwan.suitmediascreening.ui.main.MainActivity

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private val mainContract = registerForActivityResult(MainActivity.Contract()) {
        binding.editName.setText(it.message)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnNext -> {
                val userName = binding.editName.text.toString()
                if (userName.isBlank()) {
                    binding.editName.error = "Input your name first!"
                } else {
                    if(userName.isPalindrome()){
                        showToast("isPalindrome")
                    }else{
                        showToast("not a palindrome")
                    }
                    mainContract.launch(userName)
                }
            }
        }
    }

    private fun showToast(message : String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

