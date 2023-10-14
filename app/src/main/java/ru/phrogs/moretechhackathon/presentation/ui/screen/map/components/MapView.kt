package ru.phrogs.moretechhackathon.presentation.ui.screen.map.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.ClusterListener
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.runtime.image.ImageProvider

@Composable
fun MapView(
    placeMarks: List<Point>,
    placeMarkImageProvider: ImageProvider,
    bankIconStyle: IconStyle,
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
                setIcon(placeMarkImageProvider, bankIconStyle)
            }
            placeMark.addTapListener(placeMarkTapListener)
        }

        clusterizedCollection.clusterPlacemarks(clusterRadius, minZoom)
        return@AndroidView mapView
    })
}