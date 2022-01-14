package com.alwan.suitmediascreening.ui.event

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import com.alwan.suitmediascreening.R
import com.alwan.suitmediascreening.databinding.ActivityEventBinding
import com.alwan.suitmediascreening.helpers.TransactionResult

const val EVENT_NAME = "eventName"

class EventActivity : AppCompatActivity(), View.OnClickListener {
    private var _binding: ActivityEventBinding? = null
    private val binding get() = _binding!!
    private var btnMapState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.imgMap.setOnClickListener(this)
        binding.toolbar.imgBack.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onBackPressed() {
        setResult(RESULT_OK, Intent().putExtra(EVENT_NAME, intent.getStringExtra(EVENT_NAME)))
        super.onBackPressed()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.toolbar.imgMap -> {
                btnMapState = !btnMapState
                showMapFragment(btnMapState)
            }
            binding.toolbar.imgBack -> {
                onBackPressed()
            }
        }
    }

    private fun showMapFragment(state: Boolean) {
        if (state) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_event, EventMapFragment()).commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_event, EventFragment()).commit()
        }
    }

    class Contract : ActivityResultContract<String, TransactionResult>() {
        override fun createIntent(context: Context, input: String?) =
            Intent(context, EventActivity::class.java).putExtra(EVENT_NAME, input)

        override fun parseResult(resultCode: Int, intent: Intent?) = TransactionResult(
            resultCode == RESULT_OK,
            intent?.getStringExtra(EVENT_NAME).orEmpty()
        )
    }
}