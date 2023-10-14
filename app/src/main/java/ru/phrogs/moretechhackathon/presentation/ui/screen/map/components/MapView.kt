package ru.phrogs.moretechhackathon.presentation.ui.screen.map.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.ClusterListener
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

private var previousLocationPoint: PlacemarkMapObject? = null
private var previousRoute: PolylineMapObject? = null
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
    forceMapRedraw: Boolean,
    cameraPositionState: CameraPosition?,
    cameraListener: CameraListener,
    route: Polyline?
) {
    AndroidView(
        factory = {
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
            geoLocationImageProvider,
            cameraPositionState,
            cameraListener,
            route
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
    geoLocationImageProvider: ImageProvider,
    cameraPositionState: CameraPosition?,
    cameraListener: CameraListener,
    route: Polyline?
) = { mapView: MapView ->
    mapView.mapWindow.map.removeCameraListener(cameraListener)
    mapView.mapWindow.map.addCameraListener(cameraListener)
    if (cameraPositionState != null) {
        mapView.mapWindow.map.move(cameraPositionState)
    }
    if (forceMapRedraw != prevForceRedrawValue) {
        redrawAllObjects(
            mapView,
            clusterListener,
            placeMarks,
            placeMarkImageProvider,
            iconStyle,
            placeMarkTapListener,
            clusterRadius,
            minZoom,
            locationPoint,
            geoLocationImageProvider,
            route,
            forceMapRedraw
        )
    } else {
        updateRouteAndLocation(route, mapView, locationPoint, geoLocationImageProvider, iconStyle)
    }
}

private fun redrawAllObjects(
    mapView: MapView,
    clusterListener: ClusterListener,
    placeMarks: List<Point>,
    placeMarkImageProvider: ImageProvider,
    iconStyle: IconStyle,
    placeMarkTapListener: MapObjectTapListener,
    clusterRadius: Double,
    minZoom: Int,
    locationPoint: Point?,
    geoLocationImageProvider: ImageProvider,
    route: Polyline?,
    forceMapRedraw: Boolean
) {
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
    if (route != null) {
        previousRoute = mapView.mapWindow.map.mapObjects.addPolyline(route)
    }

    prevForceRedrawValue = forceMapRedraw
}

private fun updateRouteAndLocation(
    route: Polyline?,
    mapView: MapView,
    locationPoint: Point?,
    geoLocationImageProvider: ImageProvider,
    iconStyle: IconStyle
) {
    if (route != null) {
        var routesMatch = false
        try {
            routesMatch = polyLinesMatch(previousRoute?.geometry, route)
        } catch (_: Exception) {

        }
        if (!routesMatch) {
            previousRoute?.let {
                if (it.isValid) {
                    try {
                        mapView.mapWindow.map.mapObjects.remove(it)
                    } catch (_: Exception) {
                    }
                }
            }
            previousRoute = mapView.mapWindow.map.mapObjects.addPolyline(route)
        }
    }
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

private fun polyLinesMatch(first: Polyline?, second: Polyline?): Boolean {
    if (first == null || second == null) return false
    if (first.points.size != second.points.size) return false
    for (i in 0 until first.points.size) {
        if (first.points[i].latitude != second.points[i].latitude || first.points[i].longitude != second.points[i].longitude) return false
    }
    return true
}