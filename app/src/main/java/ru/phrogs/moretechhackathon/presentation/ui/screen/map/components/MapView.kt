package ru.phrogs.moretechhackathon.presentation.ui.screen.map.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

object MapHolder {
    var mapView: MapView? = null
}

@Composable
fun MapView(
    startCameraPosition: CameraPosition
) {
    AndroidView(
        factory = {
            val mapView = MapView(it)
            MapHolder.mapView = mapView
            mapView.mapWindow.map.move(startCameraPosition)
            return@AndroidView mapView
        }
    )
}