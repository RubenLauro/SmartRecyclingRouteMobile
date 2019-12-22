package com.example.smartrecyclingroute.Model

import com.google.android.gms.maps.model.LatLng

import com.google.maps.android.clustering.ClusterItem


class EcopontoClusterItem(lat: Double, lng: Double, title: String, snippet: String) : ClusterItem {
    private val mPosition: LatLng = LatLng(lat, lng)
    private var mTitle: String = title
    private var mSnippet: String = snippet

    override fun getPosition(): LatLng {
        return mPosition
    }

    override fun getTitle(): String {
        return mTitle
    }

    override fun getSnippet(): String {
        return mSnippet
    }
}