package ru.phrogs.moretechhackathon.presentation.ui.screen.map.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.ClusterListener
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

private var previousLocationPoint: PlacemarkMapObject? = null
private var prevForceRedrawValue: Boolean = false

@Composable
fun MapView(
    placeMarks: List<Point>,
    locationPoint: Point?,
    placeMarkImageProvider: ImageProvider,
    geoLocationImageProvider: ImageProvider,
    iconStyle: IconStyle,
    clusterListener: ClusterListener,
    clusterRadius: Double = 60.0,
    minZoom: Int = 13,
    placeMarkTapListener: MapObjectTapListener,
    forceMapRedraw: Boolean
) {
    AndroidView(factory = {
        val mapView = MapView(it)
        clusterizeMapPoints(
            mapView,
            clusterListener,
            placeMarks,
            placeMarkImageProvider,
            iconStyle,
            placeMarkTapListener,
            clusterRadius,
            minZoom
        )
        return@AndroidView mapView
    }, update = updateMapView(
        forceMapRedraw,
        clusterListener,
        placeMarks,
        placeMarkImageProvider,
        iconStyle,
        placeMarkTapListener,
        clusterRadius,
        minZoom,
        locationPoint,
        geoLocationImageProvider
    )
    )
}

private fun updateMapView(
    forceMapRedraw: Boolean,
    clusterListener: ClusterListener,
    placeMarks: List<Point>,
    placeMarkImageProvider: ImageProvider,
    iconStyle: IconStyle,
    placeMarkTapListener: MapObjectTapListener,
    clusterRadius: Double,
    minZoom: Int,
    locationPoint: Point?,
    geoLocationImageProvider: ImageProvider
) = { mapView: MapView ->
    if (forceMapRedraw != prevForceRedrawValue) {
        mapView.mapWindow.map.mapObjects.clear()
        clusterizeMapPoints(
            mapView,
            clusterListener,
            placeMarks,
            placeMarkImageProvider,
            iconStyle,
            placeMarkTapListener,
            clusterRadius,
            minZoom
        )
        if (locationPoint != null) {
            previousLocationPoint = mapView.mapWindow.map.mapObjects.addPlacemark().apply {
                geometry = locationPoint
                setIcon(geoLocationImageProvider, iconStyle)
            }
        }
        prevForceRedrawValue = forceMapRedraw
    } else {
        if (locationPoint != null) {
            previousLocationPoint?.let {
                if (it.isValid) {
                    try {
                        mapView.mapWindow.map.mapObjects.remove(it)
                    } catch (_: Exception) {
                    }
                }
            }
            previousLocationPoint = mapView.mapWindow.map.mapObjects.addPlacemark().apply {
                geometry = locationPoint
                setIcon(geoLocationImageProvider, iconStyle)
            }
        }
    }
}

private fun clusterizeMapPoints(
    mapView: MapView,
    clusterListener: ClusterListener,
    placeMarks: List<Point>,
    placeMarkImageProvider: ImageProvider,
    iconStyle: IconStyle,
    placeMarkTapListener: MapObjectTapListener,
    clusterRadius: Double,
    minZoom: Int
) {
    val clusterizedCollection =
        mapView.mapWindow.map.mapObjects.addClusterizedPlacemarkCollection(clusterListener)

    placeMarks.forEach { point ->
        val placeMark = clusterizedCollection.addPlacemark().apply {
            geometry = point
            setIcon(placeMarkImageProvider, iconStyle)
        }
        placeMark.addTapListener(placeMarkTapListener)
    }

    clusterizedCollection.clusterPlacemarks(clusterRadius, minZoom)
}