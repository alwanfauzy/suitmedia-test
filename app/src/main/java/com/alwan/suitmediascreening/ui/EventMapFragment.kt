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
import com.alwan.suitmediascreening.databinding.FragmentEventMapBinding
import com.alwan.suitmediascreening.helpers.SettingPreferences
import com.alwan.suitmediascreening.helpers.ViewModelFactory
import com.alwan.suitmediascreening.helpers.adapter.EventAdapter
import com.alwan.suitmediascreening.repository.model.Event
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import androidx.recyclerview.widget.LinearSnapHelper

import com.alwan.suitmediascreening.helpers.SnapOnScrollListener
import com.alwan.suitmediascreening.helpers.adapter.EventMapAdapter
import com.alwan.suitmediascreening.helpers.attachSnapHelperWithListener


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class EventMapFragment : Fragment(), SnapOnScrollListener.OnSnapPositionChangeListener,
    View.OnClickListener {
    private lateinit var supportMapFragment: SupportMapFragment
    private var _binding: FragmentEventMapBinding? = null
    private val binding get() = _binding!!
    private val eventMapAdapter = EventMapAdapter()
    private lateinit var mainViewModel: MainViewModel
    private lateinit var rvEvent: RecyclerView
    private val data = ArrayList<Event>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventMapBinding.inflate(inflater, container, false)
        setupMap()

        return binding.root
    }

    private fun setupMap() {
        supportMapFragment =
            (childFragmentManager.findFragmentById(R.id.fragment_container_map) as SupportMapFragment?)!!

        supportMapFragment.getMapAsync { googleMap ->
            for (i in data) {
                val marker = MarkerOptions().position(i.latLng).title(i.nama)
                googleMap.addMarker(marker)!!
            }
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(data[0].latLng, 10f))
        }
    }

    private fun changePositionMap(latLng: LatLng) {
        supportMapFragment.getMapAsync { googleMap ->
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData()
        setupRecyclerView()
        setupDataStore()

        binding.toolbar.imgBack.setOnClickListener(this)
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
        rvEvent.addItemDecoration(EventAdapter.MarginItemDecoration(16, true))
        rvEvent.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        setupSnapHelper()
        eventMapAdapter.setData(data)
        rvEvent.adapter = eventMapAdapter
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

    private fun setupSnapHelper() {
        val helper = LinearSnapHelper()
        helper.attachToRecyclerView(rvEvent)
        val behavior = SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE
        rvEvent.attachSnapHelperWithListener(helper, behavior, this)
    }

    override fun onSnapPositionChange(position: Int) {
        val event = eventMapAdapter.getData(position)
        changePositionMap(event.latLng)
    }

    private fun goBack() {
        val action = EventMapFragmentDirections.actionEventMapFragmentToEventFragment()
        Navigation.findNavController(requireView()).navigate(action)
        Navigation.findNavController(requireView())
            .popBackStack(R.id.eventMapFragment, true)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.toolbar.imgBack -> {
                goBack()
            }
        }
    }
}