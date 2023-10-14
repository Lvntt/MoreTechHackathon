package ru.phrogs.moretechhackathon.presentation.ui.screen.map

import android.content.Context
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.ClusterListener
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider
import org.koin.androidx.compose.koinViewModel
import ru.phrogs.moretechhackathon.R
import ru.phrogs.moretechhackathon.presentation.ui.common.state.LoadingProgress
import ru.phrogs.moretechhackathon.presentation.ui.screen.map.components.ClusterView
import ru.phrogs.moretechhackathon.presentation.ui.screen.map.components.MapView
import ru.phrogs.moretechhackathon.presentation.ui.theme.BANK_ICON_SCALE
import ru.phrogs.moretechhackathon.presentation.uistate.map.MapState
import ru.phrogs.moretechhackathon.presentation.viewmodel.MapViewModel

private val placeMarkTapListener = MapObjectTapListener { _, _ ->
    true
}
private val iconStyle = IconStyle().setScale(BANK_ICON_SCALE)

@Composable
fun MapScreen(context: Context) {
    val imageProvider = ImageProvider.fromResource(context, R.drawable.placemark)
    val clusterListener = ClusterListener { cluster ->
        cluster.appearance.setView(ViewProvider(ClusterView(context).apply {
            setData(cluster.placemarks.size)
        }))
    }

    val mapViewModel: MapViewModel = koinViewModel()
    val mapState by remember { mapViewModel.mapState }
    Crossfade(targetState = mapState, label = "") {
        when (it) {
            MapState.Loading -> LoadingProgress()
            is MapState.Content -> {
                val points = it.bankCoordinates.map { bankCoordinates ->
                    Point(
                        bankCoordinates.latitude,
                        bankCoordinates.longitude
                    )
                }
                MapView(
                    placeMarks = points,
                    placeMarkImageProvider = imageProvider,
                    bankIconStyle = iconStyle,
                    clusterListener = clusterListener,
                    placeMarkTapListener = placeMarkTapListener
                )
            }

            MapState.Error -> Unit
        }
    }

}