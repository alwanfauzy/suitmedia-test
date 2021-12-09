package com.alwan.suitmediascreening.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.alwan.suitmediascreening.R
import com.alwan.suitmediascreening.databinding.FragmentDashboardBinding
import com.alwan.suitmediascreening.helpers.SettingPreferences
import com.alwan.suitmediascreening.helpers.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DashboardFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDataStore()
        setupDashboard()
        binding.btnPilihEvent.setOnClickListener(this)
        binding.btnPilihGuest.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = "Dashboard"
    }


    private fun setupDashboard() {
        mainViewModel.getUserName().observe(viewLifecycleOwner, {
            binding.tvUsername.text = it
        })
        mainViewModel.getGuestName().observe(viewLifecycleOwner, {
            binding.btnPilihGuest.text = it
        })
        mainViewModel.getEventName().observe(viewLifecycleOwner, {
            binding.btnPilihEvent.text = it
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupDataStore() {
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(pref)
        )[MainViewModel::class.java]
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnPilihEvent -> {
                val action = DashboardFragmentDirections.actionDashboardFragmentToEventFragment()
                Navigation.findNavController(requireView()).navigate(action)
            }
            binding.btnPilihGuest -> {
                val action = DashboardFragmentDirections.actionDashboardFragmentToGuestFragment()
                Navigation.findNavController(requireView()).navigate(action)
            }
        }
    }
}