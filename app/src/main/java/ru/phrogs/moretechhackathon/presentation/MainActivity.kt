package ru.phrogs.moretechhackathon.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.ClusterListener
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider
import ru.phrogs.moretechhackathon.R
import ru.phrogs.moretechhackathon.presentation.ui.theme.BANK_ICON_SCALE
import ru.phrogs.moretechhackathon.presentation.ui.theme.MoreTechHackathonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapKitFactory.initialize(this)
        val points = listOf(
            Point(59.935493, 30.327392),
            Point(59.938185, 30.32808),
            Point(59.937376, 30.33621),
            Point(59.934517, 30.335059),
        )
        val imageProvider = ImageProvider.fromResource(this, R.drawable.placemark)
        val clusterListener = ClusterListener { cluster ->
            cluster.appearance.setView(ViewProvider(ClusterView(this).apply {
                setData(cluster.placemarks.size)
            }))
        }
        val iconStyle = IconStyle().setScale(BANK_ICON_SCALE)
        val placeMarkTapListener = MapObjectTapListener { _, _ ->
            true
        }

        setContent {
            MoreTechHackathonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    MapView(
                        placeMarks = points,
                        placeMarkImageProvider = imageProvider,
                        bankIconStyle = iconStyle,
                        clusterListener = clusterListener,
                        placeMarkTapListener = placeMarkTapListener
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()
    }
}

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
        val mapView = MapView(it)
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