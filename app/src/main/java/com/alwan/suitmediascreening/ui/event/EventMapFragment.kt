package com.alwan.suitmediascreening.ui.event

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.alwan.suitmediascreening.R
import com.alwan.suitmediascreening.databinding.FragmentEventMapBinding
import com.alwan.suitmediascreening.helpers.MarginItemDecoration
import com.alwan.suitmediascreening.helpers.SnapOnScrollListener
import com.alwan.suitmediascreening.helpers.adapter.EventMapAdapter
import com.alwan.suitmediascreening.helpers.attachSnapHelperWithListener
import com.alwan.suitmediascreening.repository.model.Event
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class EventMapFragment : Fragment(), SnapOnScrollListener.OnSnapPositionChangeListener,
    EventMapAdapter.OnEventMapClickListener {
    private lateinit var supportMapFragment: SupportMapFragment
    private var _binding: FragmentEventMapBinding? = null
    private val binding get() = _binding!!
    private val eventMapAdapter = EventMapAdapter(this)
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
        rvEvent.addItemDecoration(MarginItemDecoration(16, true))
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

    override fun onItemClicked(data: Event) {
        with(activity as EventActivity){
            setResult(RESULT_OK, Intent().putExtra(EVENT_NAME, data.nama))
            finish()
        }
    }
}