package com.alwan.suitmediascreening.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.alwan.suitmediascreening.databinding.FragmentHomeBinding
import com.alwan.suitmediascreening.helpers.SettingPreferences
import com.alwan.suitmediascreening.helpers.ViewModelFactory
import com.alwan.suitmediascreening.helpers.isPalindrome

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDataStore()
        binding.btnNext.setOnClickListener(this)
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
            binding.btnNext -> {
                val userName = binding.editName.text.toString()
                if (userName.isBlank()) {
                    binding.editName.error = "Input your name first!"
                } else {
                    mainViewModel.reset()
                    mainViewModel.saveUserName(userName)
                    val message = if (userName.isPalindrome()) "isPalindrome" else "not palindrome"
                    showToast(message)
                    val action = HomeFragmentDirections.actionHomeFragmentToDashboardFragment()
                    Navigation.findNavController(requireView()).navigate(action)
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}