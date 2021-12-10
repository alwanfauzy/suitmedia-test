package com.alwan.suitmediascreening.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alwan.suitmediascreening.R
import com.alwan.suitmediascreening.databinding.FragmentGuestBinding
import com.alwan.suitmediascreening.helpers.*
import com.alwan.suitmediascreening.helpers.adapter.GuestAdapter
import com.alwan.suitmediascreening.repository.model.Guest
import kotlinx.coroutines.flow.collect

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class GuestFragment : Fragment(), GuestAdapter.OnGuestClickListener,
    SwipeRefreshLayout.OnRefreshListener {
    private var _binding: FragmentGuestBinding? = null
    private val binding get() = _binding!!
    private val eventAdapter = GuestAdapter(this)
    private lateinit var mainViewModel: MainViewModel
    private lateinit var guestViewModel: GuestViewModel
    private lateinit var rvGuest: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGuestBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupRecyclerView()

        binding.swipeGuest.setOnRefreshListener(this)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = "Guest"
    }

    private fun setupRecyclerView() {
        rvGuest = requireView().findViewById(R.id.rv_guest)
        rvGuest.setHasFixedSize(true)
        rvGuest.layoutManager = GridLayoutManager(context, 2)
        guestViewModel.guests.observe(viewLifecycleOwner, {
            eventAdapter.setData(it)
        })
        rvGuest.adapter = eventAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupViewModel() {
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(pref)
        )[MainViewModel::class.java]
        guestViewModel = ViewModelProvider(this)[GuestViewModel::class.java]
        guestViewModel.setGuest()
        onRefresh()
    }

    override fun onItemClicked(data: Guest) {
        mainViewModel.saveGuestName(data.name)
        val action = GuestFragmentDirections.actionGuestFragmentToDashboardFragment()
        Navigation.findNavController(requireView()).navigate(action)
        Navigation.findNavController(requireView())
            .popBackStack(R.id.guestFragment, true)
        calculateBirthdate(data.birthdate)
    }

    private fun showToast(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun calculateBirthdate(birthdate: String){
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

        if(dateMonth.isPrime()){
            showToast("$message, Prime")
        }else{
            showToast("$message, Not Prime")
        }
    }

    override fun onRefresh() {
        guestViewModel.setGuest()
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            guestViewModel.loadingState.collect {
                when(it){
                    is LoadingState.Loading -> {
                        binding.swipeGuest.isRefreshing = true
                    }
                    is LoadingState.Success -> {
                        showToast("Guest Loaded")
                        binding.swipeGuest.isRefreshing = false
                    }
                    is LoadingState.Error -> {
                        showToast(it.message)
                        binding.swipeGuest.isRefreshing = false
                    }
                    else -> Unit
                }
            }
        }
    }
}