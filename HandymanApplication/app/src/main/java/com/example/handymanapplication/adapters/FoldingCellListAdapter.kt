package com.example.handymanapplication.adapters

import android.app.Activity
import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.handymanapplication.Models.ItemCell
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.IActionsOngoing
import com.example.handymanapplication.Utils.Utils
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.ramotion.foldingcell.FoldingCell
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import java.util.*
import kotlin.collections.HashSet


class FoldingCellListAdapter(
    context: Context,
    objects: List<ItemCell>,
    var iActions: IActionsOngoing
) :
    ArrayAdapter<ItemCell>(context, 0, objects), OnMapReadyCallback {
    var mapView: MapView? = null
    var title: TextView? = null
    var map: GoogleMap? = null

    var layout: View? = null
    var lat: Double? = null
    var longit: Double? = null
    private val unfoldedIndexes = HashSet<Int>()
    var defaultRequestBtnClickListener: View.OnClickListener? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // get item for selected view
        val item = getItem(position)
        // if cell is exists - reuse it, if not - create the new one from resource
        var cell = convertView as FoldingCell?

        val viewHolder: ViewHolder
        if (cell == null) {
            viewHolder = ViewHolder()
            val vi = LayoutInflater.from(context)
            cell = vi.inflate(R.layout.cell, parent, false) as FoldingCell

            viewHolder.pos = cell.findViewById(R.id.pos)
            viewHolder.map = cell.findViewById(R.id.mapView)

            viewHolder.building = cell.findViewById(R.id.building_name)

            viewHolder.floor = cell.findViewById(R.id.floor_nmbr)
            this.mapView = viewHolder.map
            viewHolder.client_profile_picture = cell.findViewById(R.id.client_profile_picture)
            viewHolder.client_name = cell.findViewById(R.id.client_name)
            viewHolder.service_image = cell.findViewById(R.id.service_image)
            viewHolder.service_name = cell.findViewById(R.id.service_name)
            viewHolder.request_title = cell.findViewById(R.id.request_title)
            viewHolder.request_from = cell.findViewById(R.id.content_from_time)
            viewHolder.request_to = cell.findViewById(R.id.request_to_time)
            viewHolder.client_street = cell.findViewById(R.id.client_street)
            viewHolder.client_state = cell.findViewById(R.id.client_state)
            viewHolder.request_to_date = cell.findViewById(R.id.request_to_date)
            viewHolder.created_at_request = cell.findViewById(R.id.created_at_request)


            viewHolder.viewimages = cell.findViewById(R.id.click_images)
            viewHolder.liste = cell.findViewById(R.id.list)

            viewHolder.description = cell.findViewById(R.id.description)
            viewHolder.acceptBtn = cell.findViewById(R.id.request_accept_btn)
            viewHolder.rejectBtn = cell.findViewById(R.id.request_reject_btn)
            cell.tag = viewHolder

        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true)
            } else {
                cell.fold(true)
            }
            viewHolder = cell.tag as ViewHolder
        }

        if (null == item)
            return cell

        // bind data from selected element to view through view holder
        // viewHolder.client!!.setText((item.client))
        viewHolder.pos!!.setText(item.pos)
        viewHolder.description!!.setText(item.description)
        viewHolder.client_name?.setText(item.client)
        viewHolder.request_from!!.setText(item.request_from)
        viewHolder.request_to!!.setText(item.request_to)
        viewHolder.request_to_date!!.setText(item.request_to_date)
        viewHolder.created_at_request!!.setText(item.created_at_date)
        viewHolder.service_name!!.setText(item.service_name)
        viewHolder.request_title!!.setText(item.request_title)

        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(item.latitude, item.longitude, 1)


        viewHolder.client_street!!.setText(item.street)
        viewHolder.client_state!!.setText(addresses[0].adminArea)
        viewHolder . building !!. text = item . building
                viewHolder.floor!!.text = item.floor_



        if (mapView != null) {
            // Initialise the MapView
            mapView!!.onCreate(null);
            // Set the map ready callback to receive the GoogleMap object
            mapView!!.getMapAsync(this);
        }
        lat = item.latitude
        longit = item.longitude
        setMapLocation()
        if (item.image != "") {
            Glide
                .with(context!!)
                .load(Utils.BASE_IMAGE_URL.plus(item.image))
                .into(viewHolder.client_profile_picture!!)
        }
        viewHolder.viewimages!!.setOnClickListener {
            iActions.onViewImageClick(item)
        }
        viewHolder.acceptBtn!!.setOnClickListener {
            iActions.onAccept(item)
        }

        viewHolder.liste!!.setOnClickListener {
            iActions.onListClick(item)
        }

        viewHolder.rejectBtn!!.setOnClickListener {
            iActions.onDelete(item)
        }
        return cell
    }

    fun addItem(item: ItemCell) {
    }

    fun unfoldNext(position: Int) {
        registerToggle(position)
    }

    // simple methods for register cell state changes
    fun registerToggle(position: Int) {
        if (unfoldedIndexes.contains(position))
            registerFold(position)
        else
            registerUnfold(position)
    }

    fun registerFold(position: Int) {
        unfoldedIndexes.remove(position)
    }

    fun registerUnfold(position: Int) {
        unfoldedIndexes.add(position)
    }


    // View lookup cache
    private class ViewHolder {
        internal var pos: TextView? = null
        internal var client_profile_picture: ImageView? = null
        internal var client_name: TextView? = null
        internal var created_at_request: TextView? = null
        internal var request_to_date: TextView? = null
        internal var head_image: ImageView? = null
        internal var service_image: ImageView? = null
        internal var request_title: TextView? = null
        internal var description: TextView? = null
        internal var client_street: TextView? = null
        internal var client_state: TextView? = null
        internal var request_from: TextView? = null
        internal var request_to: TextView? = null
        internal var viewimages: TextView? = null
        internal var acceptBtn: TextView? = null
        internal var rejectBtn: TextView? = null
        internal var map: MapView? = null
        internal var liste: LinearLayout? = null
        internal var service_name: TextView? = null
        internal var building: TextView? = null
        internal var floor: TextView? = null


    }

    override fun onMapReady(googleMap: GoogleMap) {
        MapsInitializer.initialize(context!!)
        map = googleMap
        setMapLocation()
    }

    private fun setMapLocation() {
        if (map == null) return

        val loc = LatLng(lat!!, longit!!)
        map!!.addMarker(MarkerOptions().position(loc).title("Here is it"))
        map!!.moveCamera(CameraUpdateFactory.newLatLng(loc))
        map!!.setMinZoomPreference(6.0f);
        map!!.setMaxZoomPreference(14.0f);
        map!!.setMapType(GoogleMap.MAP_TYPE_NORMAL)
    }
}