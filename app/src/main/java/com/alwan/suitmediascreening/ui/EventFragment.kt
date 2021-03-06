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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alwan.suitmediascreening.R
import com.alwan.suitmediascreening.databinding.FragmentEventBinding
import com.alwan.suitmediascreening.helpers.SettingPreferences
import com.alwan.suitmediascreening.helpers.ViewModelFactory
import com.alwan.suitmediascreening.helpers.adapter.EventAdapter
import com.alwan.suitmediascreening.repository.model.Event
import com.google.android.gms.maps.model.LatLng

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class EventFragment : Fragment(), EventAdapter.OnEventClickListener, View.OnClickListener {
    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!
    private val eventAdapter = EventAdapter(this)
    private lateinit var mainViewModel: MainViewModel
    private lateinit var rvEvent: RecyclerView
    private val data = ArrayList<Event>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData()
        setupRecyclerView()
        setupDataStore()

        binding.toolbar.imgBack.setOnClickListener(this)
        binding.toolbar.imgMap.setOnClickListener(this)
    }


    private fun setupData() {
        data.clear()
        val nama = resources.getStringArray(R.array.event_name)
        val tanggal = resources.getStringArray(R.array.event_tanggal)
        val photo = resources.obtainTypedArray(R.array.event_photo)
        val latLng = ArrayList<LatLng>()
        latLng.add(LatLng(-8.3672377, 115.021751))
        latLng.add(LatLng(-7.9731144, 112.5972763))
        latLng.add(LatLng(-7.2317579, 112.7909638))
        latLng.add(LatLng(-7.2498354, 112.6302638))

        val listEvent = ArrayList<Event>()
        for (i in nama.indices) {
            val event = Event(nama[i], tanggal[i], photo.getResourceId(i, -1), latLng[i])
            listEvent.add(event)
        }

        data.addAll(listEvent)
    }

    private fun setupRecyclerView() {
        rvEvent = requireView().findViewById(R.id.rv_event)
        rvEvent.setHasFixedSize(true)
        rvEvent.addItemDecoration(EventAdapter.MarginItemDecoration(16, false))
        rvEvent.layoutManager = LinearLayoutManager(context)
        eventAdapter.setData(data)
        rvEvent.adapter = eventAdapter
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

    override fun onItemClicked(data: Event) {
        mainViewModel.saveEventName(data.nama)
        goBack()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.toolbar.imgBack -> {
                goBack()
            }
            binding.toolbar.imgMap -> {
                val action = EventFragmentDirections.actionEventFragmentToEventMapFragment()
                Navigation.findNavController(requireView()).navigate(action)
            }
        }
    }

    private fun goBack() {
        val action = EventFragmentDirections.actionEventFragmentToDashboardFragment()
        Navigation.findNavController(requireView()).navigate(action)
        Navigation.findNavController(requireView())
            .popBackStack(R.id.eventFragment, true)
    }
}