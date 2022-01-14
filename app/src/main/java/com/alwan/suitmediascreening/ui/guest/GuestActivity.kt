package com.alwan.suitmediascreening.ui.guest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alwan.suitmediascreening.databinding.ActivityGuestBinding
import com.alwan.suitmediascreening.helpers.*
import com.alwan.suitmediascreening.helpers.adapter.GuestAdapter
import com.alwan.suitmediascreening.repository.model.Guest

private const val GUEST_NAME = "guestName"

class GuestActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener,
    GuestAdapter.OnGuestClickListener {
    private var _binding: ActivityGuestBinding? = null
    private val binding get() = _binding!!
    private val eventAdapter = GuestAdapter(this)
    private lateinit var guestViewModel: GuestViewModel
    private lateinit var rvGuest: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityGuestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.swipeGuest.setOnRefreshListener(this)
        setupViewModel()
        setupRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupViewModel() {
        guestViewModel = ViewModelProvider(this)[GuestViewModel::class.java]
        guestViewModel.setGuest()

        guestViewModel.loadingState.observe(this, {
            binding.swipeGuest.isRefreshing = it
        })

        guestViewModel.guests.observe(this, {
            eventAdapter.setData(it)
        })
    }

    override fun onItemClicked(data: Guest) {
        calculateBirthdate(data.birthdate)
        setResult(RESULT_OK, Intent().putExtra(GUEST_NAME, data.name))
        finish()
    }

    override fun onBackPressed() {
        setResult(RESULT_OK, Intent().putExtra(GUEST_NAME, intent.getStringExtra(GUEST_NAME)))
        super.onBackPressed()
    }

    private fun setupRecyclerView() {
        rvGuest = binding.rvGuest
        rvGuest.setHasFixedSize(true)
        rvGuest.layoutManager = GridLayoutManager(this, 2)
        rvGuest.adapter = eventAdapter
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun calculateBirthdate(birthdate: String) {
        val dateDay = birthdate.getDateDayInt()
        val dateMonth = birthdate.getDateMonthInt()
        val message: String = when {
            dateDay % 6 == 0 -> {
                "iOS"
            }
            dateDay % 3 == 0 -> {
                "android"
            }
            dateDay % 2 == 0 -> {
                "blackberry"
            }
            else -> {
                "feature phone"
            }
        }

        if (dateMonth.isPrime()) {
            showToast("$message, Prime")
        } else {
            showToast("$message, Not Prime")
        }
    }

    override fun onRefresh() {
        guestViewModel.setGuest()
    }

    class Contract : ActivityResultContract<String, TransactionResult>() {
        override fun createIntent(context: Context, input: String?) =
            Intent(context, GuestActivity::class.java).putExtra(GUEST_NAME, input)

        override fun parseResult(resultCode: Int, intent: Intent?) = TransactionResult(
            resultCode == RESULT_OK,
            intent?.getStringExtra(GUEST_NAME).orEmpty()
        )
    }
}