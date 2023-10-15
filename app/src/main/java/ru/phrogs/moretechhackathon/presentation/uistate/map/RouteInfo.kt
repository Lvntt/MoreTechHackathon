package ru.phrogs.moretechhackathon.presentation.uistate.map

import com.yandex.mapkit.geometry.Polyline

data class RouteInfo(
    val geometry: Polyline,
    val destinationAddress: String,
    val routeTimeMinutes: Int
)
