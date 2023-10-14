package ru.phrogs.moretechhackathon.presentation.ui.screen.map

import android.content.Context
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.Lifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.ClusterListener
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider
import org.koin.androidx.compose.koinViewModel
import ru.phrogs.moretechhackathon.R
import ru.phrogs.moretechhackathon.presentation.ui.common.lifecycle.observeAsState
import ru.phrogs.moretechhackathon.presentation.ui.common.state.LoadingProgress
import ru.phrogs.moretechhackathon.presentation.ui.screen.map.components.ClusterView
import ru.phrogs.moretechhackathon.presentation.ui.screen.map.components.MapView
import ru.phrogs.moretechhackathon.presentation.ui.theme.BANK_ICON_SCALE
import ru.phrogs.moretechhackathon.presentation.ui.theme.PaddingMedium
import ru.phrogs.moretechhackathon.presentation.uistate.map.MapState
import ru.phrogs.moretechhackathon.presentation.viewmodel.MapViewModel

private val placeMarkTapListener = MapObjectTapListener { _, _ ->
    true
}
private val iconStyle = IconStyle().setScale(BANK_ICON_SCALE)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(context: Context) {
    val bankImageProvider = ImageProvider.fromResource(context, R.drawable.placemark)
    val geoLocationImageProvider = ImageProvider.fromResource(context, R.drawable.geolocation)
    val clusterListener = ClusterListener { cluster ->
        cluster.appearance.setView(ViewProvider(ClusterView(context).apply {
            setData(cluster.placemarks.size)
        }))
    }

    val mapViewModel: MapViewModel = koinViewModel()
    val mapState by remember { mapViewModel.mapState }
    val locationState by remember { mapViewModel.currentLocationState }

    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    Crossfade(targetState = mapState, label = "") { state ->
        when (state) {
            MapState.Loading -> LoadingProgress()
            is MapState.Content -> {
                MapContent(state, locationState, bankImageProvider, geoLocationImageProvider, clusterListener, locationPermissionsState)
            }

            MapState.Error -> Unit
        }
    }

    val lifeCycleState by LocalLifecycleOwner.current.lifecycle.observeAsState()

    LaunchedEffect(lifeCycleState) {
        when (lifeCycleState) {
            Lifecycle.Event.ON_PAUSE -> mapViewModel.stopLocationTracking()

            Lifecycle.Event.ON_RESUME -> if (locationPermissionsState.allPermissionsGranted) {
                mapViewModel.startLocationTracking(context)
            }

            else -> Unit
        }
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun MapContent(
    state: MapState.Content,
    locationState: Point?,
    bankImageProvider: ImageProvider,
    geoLocationImageProvider: ImageProvider,
    clusterListener: ClusterListener,
    locationPermissionsState: MultiplePermissionsState
) {
    val points = state.bankCoordinates.map { bankCoordinates ->
        Point(
            bankCoordinates.latitude, bankCoordinates.longitude
        )
    }

    MapView(
        placeMarks = points,
        placeMarkImageProvider = bankImageProvider,
        geoLocationImageProvider = geoLocationImageProvider,
        iconStyle = iconStyle,
        clusterListener = clusterListener,
        placeMarkTapListener = placeMarkTapListener,
        locationPoint = locationState
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding()
            .padding(PaddingMedium),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(onClick = {
            if (!locationPermissionsState.allPermissionsGranted) {
                locationPermissionsState.launchMultiplePermissionRequest()
            } else {
                //Move to position
            }
        }) {
            Icon(
                painter = painterResource(id = R.drawable.navigation_icon),
                contentDescription = null
            )
        }
    }
}