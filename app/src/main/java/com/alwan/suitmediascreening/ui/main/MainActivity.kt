package com.alwan.suitmediascreening.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.ViewModelProvider
import com.alwan.suitmediascreening.databinding.ActivityMainBinding
import com.alwan.suitmediascreening.helpers.TransactionResult
import com.alwan.suitmediascreening.ui.event.EventActivity
import com.alwan.suitmediascreening.ui.guest.GuestActivity

private const val USER_NAME = "username"

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private val guestContract = registerForActivityResult(GuestActivity.Contract()){
        viewModel.setGuestName(it.message)
    }
    private val eventContract = registerForActivityResult(EventActivity.Contract()){
        viewModel.setEventName(it.message)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnPilihEvent.setOnClickListener(this)
        binding.btnPilihGuest.setOnClickListener(this)
        setupDashboard()
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.eventName.observe(this, {
            binding.btnPilihEvent.text = it
        })

        viewModel.guestName.observe(this, {
            binding.btnPilihGuest.text = it
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupDashboard() {
        binding.tvUsername.text = intent.getStringExtra(USER_NAME)
    }

    override fun onBackPressed() {
        setResult(RESULT_OK, Intent().putExtra(USER_NAME, binding.tvUsername.text))
        super.onBackPressed()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnPilihEvent -> {
                eventContract.launch(binding.btnPilihEvent.text.toString())
            }
            binding.btnPilihGuest -> {
                guestContract.launch(binding.btnPilihGuest.text.toString())
            }
        }
    }

    class Contract : ActivityResultContract<String, TransactionResult>() {
        override fun createIntent(context: Context, input: String?) =
            Intent(context, MainActivity::class.java).putExtra(USER_NAME, input)

        override fun parseResult(resultCode: Int, intent: Intent?) = TransactionResult(
            resultCode == RESULT_OK,
            intent?.getStringExtra(USER_NAME).orEmpty()
        )
    }
}