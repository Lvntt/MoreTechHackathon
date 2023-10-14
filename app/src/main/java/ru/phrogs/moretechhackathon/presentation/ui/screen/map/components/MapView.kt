package ru.phrogs.moretechhackathon.presentation.ui.screen.map.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.ClusterListener
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.image.ImageProvider

private var previousLocationPoint: PlacemarkMapObject? = null

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
    placeMarkTapListener: MapObjectTapListener
) {
    AndroidView(factory = {
        val mapView = com.yandex.mapkit.mapview.MapView(it)
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
        return@AndroidView mapView
    }, update = { mapView ->
        if (locationPoint != null) {
            previousLocationPoint?.let {
                mapView.mapWindow.map.mapObjects.remove(it)
            }
            previousLocationPoint = mapView.mapWindow.map.mapObjects.addPlacemark().apply {
                geometry = locationPoint
                setIcon(geoLocationImageProvider, iconStyle)
            }
        }
    })
}